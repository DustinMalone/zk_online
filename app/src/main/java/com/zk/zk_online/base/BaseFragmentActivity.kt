package com.zk.zk_online.base

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bigkoo.alertview.AlertView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kaopiz.kprogresshud.KProgressHUD
import com.ohmerhe.kolley.request.Http
import com.orhanobut.logger.Logger
import com.pda.wph.config.AppManager
import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import de.greenrobot.event.EventBus
import java.nio.charset.Charset

/**
 * Created by ZYB on 2017/11/5 0005.
 */
abstract class BaseFragmentActivity :FragmentActivity(),View.OnClickListener
{
    private var toast: Toast? = null

    private var mContext: Context? = null

    internal var dia: KProgressHUD? = null

    private var hint: String? = null

    private var bundle = Bundle()

    private var message: Message? = null

    //是否显示加载框
    private var isDialogshow = true;

    //请求失败弹窗
    private var httperror_view:AlertView?=null;

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            getMessage(msg!!.data);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this)
        // 去除头部
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT// 强制竖屏
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)// 初始化一个toast，解决多次弹出toast冲突问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        httperror_view= AlertView(resources.getString(R.string.dialog_title), "网络连接超时，请检查网络",
                null, arrayOf("确定"), null, mContext, AlertView.Style.Alert,
                null)
        Http.init(this)
        initPram()
        init(savedInstanceState)
    }

    abstract fun init(savedInstanceState: Bundle?)

    abstract override fun onClick(v: View?)

    /* @Subscribe(threadMode = ThreadMode.MainThread)
     public abstract fun getMessage(bundle: Bundle)*/
    abstract fun getMessage(bundle: Bundle)

    override fun onResume() {
        super.onResume()
//        EventBus.getDefault().register(mContext)

    }

    /*  override fun onPause() {
         super.onPause()
 //        EventBus.getDefault().unregister(mContext)
     }*/

    /**
     * 获取默认参数
     */
    private fun initPram() {
        val url: String = SharedPreferencesUtil.getString(this, "API_URL_", "")
        if (url != "") {
            val localIP: String = Constant.URL.split("/")[2]
            Constant.URL = Constant.URL.replace(localIP, url)
            Logger.i(Constant.URL)
        }
    }

    /**
     * 提示框
     */
    fun SToast(message: String) {
        if (!TextUtils.isEmpty(message.toString())) {
            synchronized(this) {
                toast!!.cancel()
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
                toast!!.show()
            }
        }
    }

    /**
     * 提示框
     */
    fun SToast(id: Int) {
        synchronized(this) {
            toast!!.cancel()
            toast = Toast.makeText(this, resources.getString(id), Toast.LENGTH_SHORT)
            toast!!.show()
        }
    }

    /**
     * 设置标题
     */
    fun setMyTitle(id: Int) {
        (findViewById<TextView>(R.id.tv_title)).text = resources.getString(id)
    }

    /**
     * 设置标题
     */
    fun setTitle(id: String) {
        (findViewById<TextView>(R.id.tv_title)).text = id
    }

    /**
     * 设置标题返回键显示状态
     */
    fun setShowTypeBackInTitle(id: Int) {
        (findViewById<ImageView>(R.id.img_back)).visibility=id
    }

    /**
     * 设置标题右标识
     */
    fun setRight(id: Int) {
        val right: TextView = findViewById<TextView>(R.id.right)
        right.visibility = View.VISIBLE
        right.setBackgroundResource(id)
    }

    /**
     * 提供给titbar 的back 放回事件
     */
    open fun backfinish(view: View) {
        finish()
    }

    /**
     * 隐藏键盘
     */
    fun unkeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow((this as Activity).currentFocus!!.windowToken, 0)
        }
    }


    /**
     * post请求
     */
    open fun httpPost(Url: String, map: Map<String, String>, what: Int) {
        hint = if (what == 520) getString(R.string.dialog_hint1) else getString(R.string.dialog_hint2)
        if (isDialogshow) {
            dia = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(hint).show()
        } else
            isDialogshow = true;

        Http.post {

            url = Url

            headers { "Content-Type" - "application/json" }

            val gson = Gson()
            val json: String = gson.toJson(map)

            raw = json
            Logger.i(json)
            Logger.i(Url)

            onSuccess {
                bundle=Bundle()
                Logger.i("on success ${it.toString(Charset.defaultCharset())}")
                var result = GsonUtil.jsonToObject(it.toString(Charset.defaultCharset()), JsonObject::class.java)
                if (result!!.get("errorcode").toString().equals("0")) {
                    if (result.has("Data"))
                        bundle.putString("msg", result.get("Data").toString())
                    else bundle.putString("msg", "")

                    bundle.putInt("what", what)
                    bundle.putString("allresult", it.toString(Charset.defaultCharset()));
                    message = Message.obtain();
                    message!!.data = bundle;
                    handler.sendMessage(message)
                } else {

                    onSystemError(what,result.get("msg").asString,result)
                }
                dismissDia()

            }

            onFail { error ->
                Logger.i("on fail $error")
                onHttpError()
            }

            onFinish {

            }
        }
    }


    /**
     * post请求
     */
    open fun NoHttpPost(map: Map<String, String>, what: Int) {
        Http.post {

            url = Constant.URL
            headers { "Content-Type" - "application/json" }
            val gson = Gson()
            val json: String = gson.toJson(map)
            raw = json
            Logger.i(json)
            Logger.i(Constant.URL)

            onSuccess {
                bundle.putString("msg", it.toString(Charset.defaultCharset()))
                bundle.putInt("what", what)
                EventBus.getDefault().postSticky(bundle)
                Logger.i("on success ${it.toString(Charset.defaultCharset())}")
            }

            onFail { error ->
                Logger.i("on fail $error")
                AlertView(resources.getString(R.string.dialog_title), "网络连接超时，请检查网络",
                        null, arrayOf("确定"), null, mContext, AlertView.Style.Alert,
                        null).show()
            }

            onFinish {

            }
        }
    }

    //取消加载框
    fun dismissDia()
    {
        try {
            if (dia!=null && dia!!.isShowing)
            {
                dia!!.dismiss()
            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
    }
    //请求数据失败
    open fun onHttpError()
    {
        dismissDia()
        if (isDialogshow) {
            if (httperror_view!!.isShowing)
            {
                httperror_view!!.dismiss()
                httperror_view!!.show()
            }
            else
                httperror_view!!.show()
        }
    }
    //接口错误
    open fun onSystemError(what:Int,errormsg:String,allresult:JsonObject){
        SToast(errormsg)
    }

    fun setShowPro(b: Boolean) {
        isDialogshow = b;
    }



    //获得sp 登陆Id
    fun getLoginid():String
    {
        return SharedPreferencesUtil.getString(this,Constant.LOGINID,"");
    }
    //获得sp 用户名
    fun getUsername():String
    {
        return SharedPreferencesUtil.getString(this,Constant.USERNAME,"");
    }
    //获得sp 用户图片
    fun getUserimg():String
    {
        return SharedPreferencesUtil.getString(this,Constant.USERIMG,"");
    }
    //获得sp 用户编号
    fun getUsercode():String
    {
        return SharedPreferencesUtil.getString(this,Constant.USERCODE,"");
    }
    //获得用户币数
    fun getUserCoins():String
    {
        return SharedPreferencesUtil.getString(this,Constant.USERCOINS,"0");
    }
}