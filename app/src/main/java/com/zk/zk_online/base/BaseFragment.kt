package com.zk.lpw.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bigkoo.alertview.AlertView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kaopiz.kprogresshud.KProgressHUD
import com.ohmerhe.kolley.request.Http
import com.orhanobut.logger.Logger

import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SomeUtil
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import de.greenrobot.event.ThreadMode
import java.nio.charset.Charset

/**
 * fragment基类
 * Created by mukun on 2017/8/21.
 */
abstract class BaseFragment : Fragment(), View.OnClickListener {


    var baseView: View? = null
    private var toast: Toast? = null
    private var myApplication: Application? = null
    private var inputMethodManager: InputMethodManager? = null
    private var dia: KProgressHUD? = null

    private var bundle = Bundle()

    private var mContext: Context? = null

    //是否显示加载框
    private var isdialogshow = true;
    private var message: Message?=null


    //请求失败弹窗
    private var httperror_view:AlertView?=null;

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            getMessage(msg!!.data);
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      /*  if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }*/
    }

    @SuppressLint("ShowToast")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        baseView = inflater!!.inflate(getLayoutId(), container, false)
        myApplication = activity.application
        mContext = activity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT// 强制竖屏

        httperror_view= AlertView(resources.getString(R.string.dialog_title), "网络连接超时，请检查网络",
                null, arrayOf("确定"), null, mContext, AlertView.Style.Alert,
                null)
        init()
        toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT)// 初始化一个toast，解决多次弹出toast冲突问题
        return baseView!!
    }


    /**
     * 获取布局文件ID
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化
     */
    abstract fun init()

    abstract override fun onClick(v: View)

    @Subscribe(threadMode = ThreadMode.MainThread)
    public abstract fun getMessage(bundle: Bundle)

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(mContext))
            EventBus.getDefault().unregister(mContext)
        super.onDestroy()
    }

    /**
     * 提示框
     */
    fun SToast(message: String) {
        if (!TextUtils.isEmpty(message)) {
            synchronized(this) {
                toast!!.cancel()
                toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
                toast!!.show()
            }
        }
    }

    //获得sp 登陆Id
    fun getLoginid():String
    {
        return SharedPreferencesUtil.getString(activity,Constant.LOGINID,"");
    }
    //获得sp 用户名
    fun getUsername():String
    {
        return SharedPreferencesUtil.getString(activity,Constant.USERNAME,"");
    }
    //获得sp 用户图片
    fun getUserimg():String
    {
        return SharedPreferencesUtil.getString(activity,Constant.USERIMG,"");
    }
    //获得sp 用户编号
    fun getUsercode():String
    {
        return SharedPreferencesUtil.getString(activity,Constant.USERCODE,"");
    }
    //获得sp 用户性别
    fun getUsersex():String
    {
        return SharedPreferencesUtil.getString(activity,Constant.USERSEX,"");
    }
    //获得用户币数
    fun getUserCoins():String
    {
        return SharedPreferencesUtil.getString(activity,Constant.USERCOINS,"0");
    }

    /**
     * 提示框
     */
    fun SToast(id: Int) {
        synchronized(this) {
            toast!!.cancel()
            toast = Toast.makeText(mContext, resources.getString(id), Toast.LENGTH_SHORT)
            toast!!.show()
        }
    }
    //取消对话框显示
    fun dismissDialog()
    {
        try{
            if (dia!=null && dia!!.isShowing)
                dia!!.dismiss()
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
    }
    /**
     * post请求
     */
    open fun httpPost(iurl:String,map: Map<String, String>, what: Int) {
        if (isdialogshow) {
            dia = KProgressHUD.create(mContext).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(getString(R.string.dialog_hint2)).show()
        } else
            isdialogshow = true;


        Http.post {

            url = iurl

            headers { "Content-Type" - "application/json" }

            val gson = Gson()
            val json: String = gson.toJson(map)

            raw = json
            Logger.i(json)

            onSuccess {
                bundle=Bundle();
                Logger.i("on success ${it.toString(Charset.defaultCharset())}")
                var result=GsonUtil.jsonToObject(it.toString(Charset.defaultCharset()),JsonObject::class.java)
                Log.i("code",result!!.get("errorcode").toString());
                bundle.putString("allresult", it.toString(Charset.defaultCharset()))
                if (result!!.get("errorcode").toString().equals("0"))
                {
                    if (result.has("Data"))
                        bundle.putString("msg", result.get("Data").toString())
                    else bundle.putString("msg", "")

                    bundle.putInt("what", what)
                    message= Message.obtain();
                    message!!.data=bundle;
                    handler.sendMessage(message)
                }
                else{
                    onSystemError(what,result.get("msg").asString,result)
                }
                dismissDialog();
            }

            onFail { error ->
                Logger.i("on fail $error")
                onHttpError();
            }

            onFinish {

            }
        }
    }
    //接口错误
    open fun onSystemError(what:Int,errormsg:String,allresult:JsonObject){
        if (!SomeUtil.TextIsEmpey(errormsg))
        {
            SToast(errormsg)
        }
    }

    //请求失败
    open fun onHttpError()
    {
        dismissDialog();
        if (isAdded)
        {
            if (httperror_view!!.isShowing)
            {
                httperror_view!!.dismiss()
                httperror_view!!.show()
            }
            else
                httperror_view!!.show()
        }
    }


    fun setDialogShow(b: Boolean) {
        isdialogshow = b;
    }


}
