package com.zk.zk_online.HomeModel.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.JsonObject
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMWeb
import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user_invite_code.*




/**
 * Created by Administrator on 2017/11/22.
 */
class UserInviteCodeActivity : BaseActivity(){

    //查询系统参数值
    val GETSYMPOL=1

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_user_invite_code)
        setTitle("我的邀请码")
        //友盟分享SDK调试模式
//        Config.DEBUG = true;

        tv_user_invite_code.text="我的邀请码"

        addCodeText(this)

        //开启线程
//        Thread(Runnable {
//            val bitmap = QRCodeEncoder.syncEncodeQRCode(SharedPreferencesUtil.getString(applicationContext,Constant.SHARE_CODE,""), 160)
//            //把产生的Bitmap赋值到ImageView中,但是要在主线程中运行
//            runOnUiThread { iv_invite_pic.setImageBitmap(bitmap) }
//        }).start()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_user_invite_code_share->{
                getSympol()
            }
        }
    }

    override fun getMessage(bundle: Bundle) {

        var result=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)

        when(bundle.getInt("what")){
            GETSYMPOL->{
                umengShareInit(result!!.get("Data").asString)
            }
        }
    }


    //查询系统参数值接口
    fun getSympol(){

        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sympol", "sharecode_url")
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getsympol, param, GETSYMPOL)
    }

    var  shareListener =object: UMShareListener {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        override fun onStart(p0: SHARE_MEDIA?) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        override fun onResult(p0: SHARE_MEDIA?) {
            SToast("成功")
        }


        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
            SToast("失败:请检查是否安装微信")
            Log.e("失败",p1!!.message)
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        override fun onCancel(p0: SHARE_MEDIA?) {
            SToast("取消")
        }

    };


    //友盟分享到微信
    fun umengShareInit(url :String) {
        val web = UMWeb(url+getUsercode())
        web.title = "抓萌抓娃娃"//标题
        web.description = " "//描述
       var shareAction= ShareAction(this)
                .withMedia(web)
               .withText("分享")
//               .setPlatform(SHARE_MEDIA.WEIXIN)
                .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener)
                .open();


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //显示邀请码
    fun addCodeText(context: Context){
       var code= SharedPreferencesUtil.getString(applicationContext,Constant.SHARE_CODE,"")
       for (c in code){
           var textview= TextView(context)
           ll_user_invite_code_list.addView(textview)

           var lp = LinearLayout.LayoutParams(textview.getLayoutParams())
           textview.setTextColor(ContextCompat.getColor(context,R.color.white))
           textview.textSize=30f
           textview.setBackgroundResource(R.drawable.bg_invitecode)
           textview.gravity=Gravity.CENTER
           textview.setPadding(0,60,0,0)
           lp.width=ViewGroup.LayoutParams.WRAP_CONTENT
           lp.height=ViewGroup.LayoutParams.WRAP_CONTENT
           if(ll_user_invite_code_list.childCount>1){
           lp.leftMargin=20
           }
           textview.layoutParams=lp
           textview.text=c.toString()
        }

    }


}