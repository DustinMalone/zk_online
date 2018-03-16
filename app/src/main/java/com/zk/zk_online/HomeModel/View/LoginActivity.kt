package com.zk.zk_online.HomeModel.View

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.JsonObject
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareConfig
import com.umeng.socialize.bean.SHARE_MEDIA
import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.zk_online.HomeModel.Model.UserInfo
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by ZYB on 2017/11/16 0016.
 */
public class LoginActivity:BaseActivity()
{

    var shareapi: UMShareAPI? = null;
    var platform: SHARE_MEDIA? = null;
    //微信用户信息
    var WxUserinfo:Map<String,Any>?=null;
    //登录标识
    var LOGIN_CODE=0;
    //手机号登录标识
    val LOGINBYPHONE=1

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        shareapi = UMShareAPI.get(this);

        tv_login_remeber_mm.isSelected=SharedPreferencesUtil.getBoolean(this,Constant.USERISREMEBERPWD,false)
        //记住密码显示账号信息
        if (SharedPreferencesUtil.getBoolean(this,Constant.USERISREMEBERPWD,false)){
            et_login_zz.setText(SharedPreferencesUtil.getString(this,Constant.USERPHONE,""))
            et_login_mm.setText(SharedPreferencesUtil.getString(this,Constant.USERPHONEPWD,""))
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            //注册页面
            R.id.tv_login_reg->{
                startActivity(Intent(this,RegisterActivity::class.java))

            }
            R.id.bt_login->{

                //每次都要进入授权界面
                val config = UMShareConfig()
                config.isNeedAuthOnGetUserInfo(true)
                UMShareAPI.get(applicationContext).setShareConfig(config)
                //登陆授权微信
                platform = SHARE_MEDIA.WEIXIN;
                shareapi!!.getPlatformInfo(this, platform,
                        umAuthListener);
            }
            //手机号登录
            R.id.btn_phone_login->{
//                SharedPreferencesUtil.putString(this,Constant.LOGINID,"57")
//                SharedPreferencesUtil.putString(this,Constant.USERNAME,"test")
//                SharedPreferencesUtil.putString(this,Constant.USERSEX,"女")
//                SharedPreferencesUtil.putString(this,Constant.USERCODE,"oYVcI1Dtnd_pR8JflatfM6ZHbOv8")
//                startActivity(Intent(this,MainActivity::class.java))

                if (TextUtils.isEmpty(et_login_zz.text.toString())
                        ||TextUtils.isEmpty(et_login_mm.text.toString())){
                    SToast("请填写账号密码！")
                }
                else{
                loginyphone()
                }
            }
            //显示条款信息
            R.id.linear_provision->
            {
                var view=LayoutInflater.from(this).inflate(R.layout.window_provision,null)
                var webView=view!!.findViewById<WebView>(R.id.webview);
                webView.settings.javaScriptEnabled=true;
                webView.webViewClient=object :WebViewClient(){
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        return false;
                    }
                }
                webView.loadUrl("file:///android_asset/provision.html");
                var dialog=Dialog(this)
                dialog.setContentView(view)
                dialog.show();
            }

            R.id.tv_login_remeber_mm->{
                if (tv_login_remeber_mm.isSelected){
                    tv_login_remeber_mm.isSelected=false
                }else{
                    tv_login_remeber_mm.isSelected=true
                }
            }

        }

    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        var allresult=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java);
        when(bundle.getInt("what"))
        {
            LOGIN_CODE ->
            {
                var intent=Intent(this,MainActivity::class.java);
                var userInfo=GsonUtil.jsonToObject(result,UserInfo::class.java);
                SharedPreferencesUtil.putString(this,Constant.LOGINID,userInfo!!.id.toString())
                SharedPreferencesUtil.putString(this,Constant.USERNAME,userInfo!!.username)
                SharedPreferencesUtil.putString(this,Constant.USERIMG,userInfo!!.headimgurl)
                SharedPreferencesUtil.putString(this,Constant.USERSEX,userInfo!!.sex.toString())
                SharedPreferencesUtil.putString(this,Constant.USERCODE,userInfo!!.usercode.toString())
                SharedPreferencesUtil.putString(this,Constant.USERCOINS,userInfo!!.coins.toString())
                SharedPreferencesUtil.putLong(this,Constant.LOGIN_TIME,System.currentTimeMillis());
                if (allresult!!.has("presentcoin")) {
                    intent.putExtra("presentcoin", allresult!!.get("presentcoin").asString);
                }
                startActivity(intent)
            }
            //手机登录接口返回结果
            LOGINBYPHONE->{
                var intent=Intent(this,MainActivity::class.java);
                var userInfo=GsonUtil.jsonToObject(result,UserInfo::class.java);
                if(tv_login_remeber_mm.isSelected){
                    SharedPreferencesUtil.putString(this,Constant.USERPHONE,et_login_zz.text.toString())
                    SharedPreferencesUtil.putString(this,Constant.USERPHONEPWD,et_login_mm.text.toString())
                 }else{
                    SharedPreferencesUtil.putString(this,Constant.USERPHONE,"")
                    SharedPreferencesUtil.putString(this,Constant.USERPHONEPWD,"")
                }
                SharedPreferencesUtil.putBoolean(this,Constant.USERISREMEBERPWD,tv_login_remeber_mm.isSelected)
                SharedPreferencesUtil.putString(this,Constant.LOGINID,userInfo!!.id.toString())
                SharedPreferencesUtil.putString(this,Constant.USERNAME,userInfo!!.username)
                SharedPreferencesUtil.putString(this,Constant.USERIMG,userInfo!!.headimgurl)
                SharedPreferencesUtil.putString(this,Constant.USERSEX,userInfo!!.sex.toString())
                SharedPreferencesUtil.putString(this,Constant.USERCODE,userInfo!!.usercode.toString())
                SharedPreferencesUtil.putString(this,Constant.USERCOINS,userInfo!!.coins.toString())
                SharedPreferencesUtil.putLong(this,Constant.LOGIN_TIME,System.currentTimeMillis());
                if (allresult!!.has("presentcoin")) {
                    intent.putExtra("presentcoin", allresult!!.get("presentcoin").asString);
                }
                startActivity(intent)
            }
        }
        
    }




    private val umAuthListener = object : UMAuthListener  {
        override fun onStart(platform: SHARE_MEDIA) {
            //授权开始的回调
        }

        override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {

            WxUserinfo=data;
            for( item in WxUserinfo!!)
            {
                Log.i("info",item.key+" : "+item.value)
            }
            WXloginRequest();
        }

        override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {

        }

        override fun onCancel(platform: SHARE_MEDIA, action: Int) {

        }
    }

    /**
     * 登陆微信
     */
    fun WXloginRequest()
    {
        var sex="";
        if (WxUserinfo!!.get("gender").toString().equals("男"))
            sex="1"
        else if(WxUserinfo!!.get("gender").toString().equals("女"))
            sex="2"
        else sex="0"


        var param=HashMap<String,String>();
        param.put("city",WxUserinfo!!.get("city").toString())
        param.put("country",WxUserinfo!!.get("country").toString())
        param.put("headimgurl",WxUserinfo!!.get("iconurl").toString())
        param.put("loginid",WxUserinfo!!.get("openid").toString())
        param.put("province",WxUserinfo!!.get("province").toString())
        param.put("sex",sex)
        param.put("sn",Constant.SN);
        param.put("username",WxUserinfo!!.get("name").toString());
        var sign=SignParamUtil.getSignStr(param)
        param.put("sign",sign);
        httpPost(Constant.SERVERIP+Constant.wxlogin,param,LOGIN_CODE)
    }


    //登录接口
    fun loginyphone(){

        var param = HashMap<String, String>()
        param.put("loginid",et_login_zz.text.toString())
        param.put("sn", Constant.SN)
        param.put("loginpwd", et_login_mm.text.toString())
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.loginbyphone, param, LOGINBYPHONE)
    }

}