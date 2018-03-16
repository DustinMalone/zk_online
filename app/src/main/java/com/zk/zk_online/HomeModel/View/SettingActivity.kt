package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.pda.wph.config.AppManager
import com.zk.lpw.config.Constant
import com.zk.lpw.config.Constant.Companion.MUSICSTATUS
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.lpw.utils.VersionUtil
import com.zk.zk_online.R
import com.zk.zk_online.Utils.update.UpdateUtilListener
import com.zk.zk_online.Utils.update.updateUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by Administrator on 2017/12/18.
 */
public  class  SettingActivity :BaseActivity(){

    private var music_status:Boolean?=null
    private var img_control_music: ImageView?=null
    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_setting)
        setTitle("设置")
        img_control_music=findViewById(R.id.img_control_bgm)
        music_status=SharedPreferencesUtil.getBoolean(this,MUSICSTATUS,true)
        if (music_status!!)
        {
            img_control_music!!.setImageResource(R.mipmap.kai)
        }
        else{
            img_control_music!!.setImageResource(R.mipmap.guan)
        }

        tv_setting_version!!.text= "当前版本: "+VersionUtil.getVersionName(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.relat_checknew ->
            {
                var updateUtil=updateUtil(this);
                updateUtil.requestUpdate(object : UpdateUtilListener{
                    override fun no_update() {
                        SToast("当前已经是最新版本")
                    }
                })
            }
            R.id.relat_logout ->{
                SharedPreferencesUtil.clear(this);
                startActivity(Intent(this,LoginActivity::class.java))
                AppManager.getAppManager().finishAllActivity();
            }
            R.id.img_control_bgm ->{
                 if (music_status!!)
                 {
                     music_status=false;
                     img_control_music!!.setImageResource(R.mipmap.guan)
                 }
                else{
                     music_status=true;
                     img_control_music!!.setImageResource(R.mipmap.kai)
                 }
                 SharedPreferencesUtil.putBoolean(this,Constant.MUSICSTATUS,music_status!!)

            }
        }

    }

    override fun getMessage(bundle: Bundle) {

    }
}