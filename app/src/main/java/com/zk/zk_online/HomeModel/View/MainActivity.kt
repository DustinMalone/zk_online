package com.zk.zk_online.HomeModel.View

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonObject
import com.pda.wph.config.AppManager
import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.zk_online.HomeModel.Model.ChargeMoney
import com.zk.zk_online.HomeModel.Model.UserInfo
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.Utils.update.UpdateUtilListener
import com.zk.zk_online.Utils.update.updateUtil
import com.zk.zk_online.base.BaseFragmentActivity
import com.zk.zk_online.weight.ChargeMoneyDialog
import com.zk.zk_online.weight.GivecoinDialog
import com.zk.zk_online.weight.InputInviteCodeDialog
import com.zk.zk_online.weight.circleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseFragmentActivity() {

    var homefragment: HomeFragment? = null;
    var userfragment: UserFragment? = null;

    var fragmentmananger: FragmentManager? = null;

    var main_topbar: RelativeLayout? = null


    var tv_user: circleImageView? = null;
    var tv_message: ImageView? = null;
    var home: ImageView? = null;

    var lastClickTime: Long = 0

    //查询充值套餐
    val GETPACKAGES = 5
    //创建h5支付订单
    val CREATE_ORDER = 6
    //获取用户信息(充值时调用)
    val GETUSERCOIN = 7

    //输入邀请码获取游戏币
    val INPUTINVICECODE = 8

    //获取已返回申诉条数接口
    val GETFEEDBACKNUMBER = 9

    //判断用户是否领取分享码接口
    val ISGETSHARECODE = 10




    //获取每日签到标志
    val SIGN_COIN = 1;

    //显示的用户币数
    var userCoin=""

    var dialog:Dialog?=null

    var inputDialog:InputInviteCodeDialog?=null

    override fun getMessage(bundle: Bundle) {
        Log.i("ss", "ss")
        var rjson = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        var result=bundle.getString("msg")
        when (bundle.getInt("what")) {
            SIGN_COIN -> {
                var signcoin = rjson!!.get("signcoin").asString
                Log.i("signcoin", signcoin);
                var dialog = GivecoinDialog(this)
                        .createDialog()
                        .setCoinText(signcoin)
                        .setCanclable(false)
                        .show()
            }
            //获取充值套餐
            GETPACKAGES -> {
                var rjson = GsonUtil.jsonToList(result, ChargeMoney::class.java) as ArrayList<ChargeMoney>
                var chargeMoneyDialog = ChargeMoneyDialog(this).create()
                        .sendData(rjson,userCoin)
                        .setItemlistener { item ->
                            createH5order(item.id)
                        }
                        .show()
                //还原币数
                userCoin=""
            }
            //支付订单
            CREATE_ORDER -> {

                openWebView(rjson!!.get("payurl").asString)
            }
        //用户信息(充值时调用)
            GETUSERCOIN -> {
                var userinfo: UserInfo =GsonUtil.jsonToObject(result, UserInfo::class.java) as UserInfo
                userCoin=userinfo.coins.toString()
            }
            //输入邀请码获取游戏币
            INPUTINVICECODE->{
                if(inputDialog!=null) {
                    inputDialog!!.dismiss()
                }
                SToast("成功获得"+rjson!!.get("presentcoin").asString+"币")
                if (dialog!=null){
                    dialog!!.show()
                }else{
                    getsigncoid()
                }
            }
            //是否有反馈数
            GETFEEDBACKNUMBER->{
                if(null!=rjson!!.get("number")&&rjson!!.get("number").asInt>0){
                    iv_main_user_hong!!.visibility=View.VISIBLE
                }else{
                    iv_main_user_hong!!.visibility=View.GONE
                }
            }
            //判断用户是否领取分享码接口
            ISGETSHARECODE->{
                var df = SimpleDateFormat("yyyy-MM-dd")//设置日期格式
                if (SharedPreferencesUtil.getString(this,"input_dialog_time","")!=df.format(Date())){
                 inputDialog= InputInviteCodeDialog(this).create()
                        .setNegativeButton { v->
                            SharedPreferencesUtil.putString(this,"input_dialog_time",df.format(Date()))
                            if (dialog!=null){
                            dialog!!.show()
                            }else{
                            getsigncoid()
                            }
                        }
                        .setPositiveButton{v,s->
                            inputInviceCode(s) }
                        .show()
                }
            }
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        initView()
        homefragment = HomeFragment();
        userfragment = UserFragment();

        fragmentmananger = supportFragmentManager;
        fragmentmananger!!.beginTransaction().add(R.id.fragment_content, homefragment).commit();

        //自动更新
        var updateutil = updateUtil(this);
        updateutil.requestUpdate(object : UpdateUtilListener {
            override fun no_update() {

            }
        })

        if (intent != null && intent.hasExtra("presentcoin")) {
            var coins = intent.getStringExtra("presentcoin")
             dialog = Dialog(this)
            var view = LayoutInflater.from(this).inflate(R.layout.window_presentcoin, null)
            var tv_coins = view!!.findViewById<TextView>(R.id.tv_coins);
            tv_coins!!.text = "赠送" + coins + "币"
            dialog!!.setContentView(view)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }

        //判断用户是否领取分享码接口
        isGetShareCode()

    }

    //获取每日签到赠钱币
    fun getsigncoid() {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("userid", getLoginid())
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getsigncoin, param, SIGN_COIN)
    }

    fun initView() {
        tv_user = findViewById(R.id.tv_user)
//        tv_message = findViewById(R.id.tv_mesasge)
        home = findViewById(R.id.home)
        main_topbar = findViewById(R.id.main_topbar)

        Log.e("asd", getUserimg() + "")
        Glide.with(applicationContext).load(getUserimg())
                .placeholder(R.drawable.default_img)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        tv_user!!.setImageDrawable(resource)
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(tv_user)

//        tv_message!!.setOnClickListener(this)
        tv_user!!.setOnClickListener(this)
        home!!.setOnClickListener(this)
    }


    override fun onBackPressed() {
        if (lastClickTime <= 0) {
            SToast("再按一次退出程序")
            // 把当前点击back键的时间赋值给lastClickTime
            lastClickTime = System.currentTimeMillis()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < 2000) {
                AppManager.getAppManager().AppExit(this)
            } else {
                SToast("再按一次退出程序")
                lastClickTime = System.currentTimeMillis()
            }
        }
    }


    override fun onClick(v: View?) {

        when (v!!.id) {
        /*R.id.tv_mesasge->{
            startActivity(Intent(applicationContext,SystemMessageActivity::class.java))
        }*/
            R.id.tv_pay -> {
                getUserInfoCoin()
                getPackages()
            }

            R.id.tv_user -> {
                main_topbar!!.visibility = View.GONE
                fragmentmananger!!.beginTransaction().replace(R.id.fragment_content, userfragment).commit()
            }

        }
    }

    override fun onSystemError(what: Int, errormsg: String, allresult: JsonObject) {
        when(what){
            SIGN_COIN->{
                return
            }
            ISGETSHARECODE->{
                if (dialog!=null){
                    dialog!!.show()
                }else{
                    getsigncoid()
                }
                return
            }
        }
        super.onSystemError(what, errormsg, allresult)
    }


    //是否显示标题栏
    fun showTitleBar(v: Boolean) {

        if (v) {
            main_topbar!!.visibility = View.VISIBLE
        } else {
            main_topbar!!.visibility = View.GONE
        }

    }

    //查询充值套餐接口
    fun getPackages() {
        var param = HashMap<String, String>()
        param.put("sn", Constant.SN)
        param.put("loginid", getUsercode())
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getpackages, param, GETPACKAGES)
    }

    //请求创建h5支付订单
    fun createH5order(goodsid: String) {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("userid", getLoginid());
        param.put("goodsid", goodsid);
        param.put("apptype", "Android")
        param.put("appname", Constant.apk_name)
        param.put("appid", Constant.package_name)
        param.put("sign", SignParamUtil.getSignStr(param));
        httpPost(Constant.SERVERIP + Constant.createH5order, param, CREATE_ORDER)
    }


    //打开h5支付
    fun openWebView(callbackurl: String) {
        var webview: WebView? = WebView(this)
        webview!!.settings.javaScriptEnabled = true;
        webview!!.setWebViewClient(object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                Log.i("go_pay", "go_pay")
                if (url!!.startsWith("weixin://wap/pay?")) {
                    var intent = Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        })

        webview!!.loadUrl(callbackurl)
        /*
         外置浏览器打开
         Log.i("callbackurl",callbackurl);
         var  intent = Intent();
         intent.setAction("android.intent.action.VIEW");
         var content_url= Uri.parse(callbackurl);
         intent.setData(content_url);
         startActivity(intent);*/
    }

    //请求用户信息(资产)
    fun getUserInfoCoin() {
        var param = java.util.HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("loginid", getUsercode());
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getuserinfo, param, GETUSERCOIN)
    }

    //输入邀请码赠币接口
    fun inputInviceCode(s:String){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sharecode", s)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getsharecode, param, INPUTINVICECODE);
    }

    //获取已返回申诉条数接口
    fun getFeedBackNumber(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getfeedbacknumber, param, GETFEEDBACKNUMBER);
    }


    //判断用户是否领取分享码接口
    fun isGetShareCode(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.isgetsharecode, param, ISGETSHARECODE);
    }

    override fun onResume() {
        super.onResume()
        getFeedBackNumber()
    }
}
