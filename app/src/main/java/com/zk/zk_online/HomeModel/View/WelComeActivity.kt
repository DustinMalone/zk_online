package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.zk_online.R
import com.zk.zk_online.Utils.SomeUtil
import com.zk.zk_online.base.BaseActivity

/**
 * Created by ZYB on 2017/11/17 0017.
 */
public class WelComeActivity :BaseActivity()
{
    //微信登陆过期时间 7天过期
    val timeout=7*24*3600*1000;
    //现在登陆的时间戳
    val nowrime=System.currentTimeMillis();
    //之前记录在sp中的登陆时间戳
    var logintime:Long?=null;

    override fun init(savedInstanceState: Bundle?) {

            /*window.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)*/
            setContentView(R.layout.activity_welcome)
            logintime=SharedPreferencesUtil.getLong(this,Constant.LOGIN_TIME,0);
            Handler().postDelayed(Runnable()
            {
                kotlin.run {
                    if (SomeUtil.TextIsEmpey(getLoginid()))
                    {
                        startActivity(Intent(this,LoginActivity::class.java))
                    }
                    else
                    {
                        if (nowrime-logintime!!>=timeout)
                        {
                            startActivity(Intent(this,LoginActivity::class.java))
                        }
                        else{
                            startActivity(Intent(this,MainActivity::class.java))
                        }
                    }
                    finish();
                }
            },2000)
    }

    override fun onClick(v: View?) {

    }

    override fun getMessage(bundle: Bundle) {

    }
}