package com.zk.zk_online.HomeModel.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.PhotoUtils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_advice.*
import java.util.*


/**
 * Created by ZYB on 2017/11/6 0006.
 */
public  class AdviceActivity :BaseActivity(){


    //用户反馈接口
    val FEEDBACK=1
    //用户选择图片
    val SELECT_PHOTO=2
    //检查权限
    val CHECK_PERMISSION=3

    //图片base64格式
    var img_path:String?=""

    //机台名称
    var machinename=""
    //机台ID
    var machineid=""

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_advice);
        setTitle("反馈")

        machineid = intent.getStringExtra("machineid");
        machinename = intent.getStringExtra("machinename")
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_advice_sendmsg->{
                if(!TextUtils.isEmpty(ed_advice.text.toString())){
                    feedBack()
                }else{
                    SToast("请填写反馈信息！")
                }
            }
            R.id.img_choose->{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),CHECK_PERMISSION);
                }else {
                    PhotoUtils.openAlbum(this,SELECT_PHOTO)
                }
            }
        }

    }

    override fun getMessage(bundle: Bundle) {
        var allresult=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)

        when(bundle.getInt("what")){
            FEEDBACK->{
                SToast(allresult!!.get("msg").asString)
                iv_advice_img_choose.visibility=View.GONE
                img_choose.visibility=View.VISIBLE
                ed_advice.setText("")
                finish()
            }
        }

    }

    //用户反馈接口
    fun feedBack(){

        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("username", getUsername())
        param.put("content", ed_advice.text.toString())
        param.put("machinename", machinename)
        param.put("machineid", machineid)
        param.put("sign", SignParamUtil.getSignStr(param))
        if (iv_advice_img_choose.visibility==View.VISIBLE&&!TextUtils.isEmpty(img_path)){
            if (!TextUtils.isEmpty(img_path)) {
                param.put("pic", img_path!!)
            }
        }

//        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.feedback, param, FEEDBACK)
    }

    /**
     * 根据图片路径显示图片的方法
     */
    fun displayImage(imagePath: String){
        Glide.with(this).load(imagePath)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        img_path=PhotoUtils.bitmapToBase64((resource as GlideBitmapDrawable).bitmap)
                        return false
                    }
                })
                .into(iv_advice_img_choose)
//        if (imagePath != null) {
//            var bitmap=BitmapFactory.decodeFile(imagePath)
//            img_path=PhotoUtils.bitmapToBase64(PhotoUtils.compressImage(bitmap))
//            return PhotoUtils.compressImage(bitmap)
//        } else{
//            return null;
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            //选择相册图片
            SELECT_PHOTO->{
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        //4.4及以上系统使用这个方法处理图片
                        if (data != null&&null!=PhotoUtils.handleImgeOnKitKat(this,data)) {
                            displayImage(PhotoUtils.handleImgeOnKitKat(this,data)!!)
//                                iv_advice_img_choose.setImageBitmap(displayImage(PhotoUtils.handleImgeOnKitKat(this, data)))
                            iv_advice_img_choose.visibility=View.VISIBLE
                            img_choose.visibility=View.GONE
                        };
                    }else {
                        if (data != null&&null!=PhotoUtils.handleImageBeforeKitKat(this,data)) {
                            displayImage(PhotoUtils.handleImageBeforeKitKat(this,data)!!)
//                            iv_advice_img_choose.setImageBitmap(displayImage(PhotoUtils.handleImageBeforeKitKat(this, data)))
                            iv_advice_img_choose.visibility=View.VISIBLE
                            img_choose.visibility=View.GONE
                        };
                    }
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CHECK_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PhotoUtils.openAlbum(this,SELECT_PHOTO)
            } else {
                SToast("you need the permission")
            }
            else -> {
            }
        }
    }




}