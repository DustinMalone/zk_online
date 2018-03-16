package com.zk.zk_online.HomeModel.View

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kaopiz.kprogresshud.KProgressHUD
import com.zk.lpw.config.Constant
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.TisDialog
import kotlinx.android.synthetic.main.activity_scan_code.*
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x


class ScanCodeActivity : BaseActivity(),QRCodeView.Delegate {
    companion object {
        private val REQUEST_CODE_CAMERA = 999
    }
    val TAG=ScanCodeActivity::class.java.simpleName

    //开启线下机台
    val STARTOFFLINEMACHINE=1

    var context:Context=this


    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_scan_code)
        setTitle("扫机台二维码")


        //设置扫码结果操作
        mQRCodeView!!.setDelegate(this)
        //开启相机
        mQRCodeView!!.startCamera();
    }

    override fun onClick(v: View?) {
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        var allresult= GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        when(bundle.getInt("what")){
            //开启线下机台
            STARTOFFLINEMACHINE->{
                SToast("启动成功")
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
//        mQRCodeView!!.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView!!.startSpot();
        Log.i(TAG, "1" )
    }

    override fun onStop() {
        super.onStop()
//        mQRCodeView!!.stopCamera()
        Log.i(TAG, "2" )
    }

    override fun onDestroy() {
        mQRCodeView!!.onDestroy()
        super.onDestroy()

    }

    //震动器
     fun vibrate() {
        var vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(100)
    }


    override fun onScanQRCodeSuccess(result: String) {
        Log.i(TAG, "result:" + result)
        vibrate()//震动
        mQRCodeView!!.stopSpot()

//        SToast(result.removePrefix(Constant.startofflinemachine_tag))

        if (!TextUtils.isEmpty(result)&&result.startsWith(Constant.startofflinemachine_tag)) {

            startOfflineMachine(result.removePrefix(Constant.startofflinemachine_tag))

        } else {
           SToast("链接无效,请重新扫描")
            mQRCodeView!!.startSpot()
        }

    }

    //扫码失败
    override fun onScanQRCodeOpenCameraError() {
        Log.e("", "无相机权限,打开相机出错")
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA)
    }


    //开启线下机台
//    fun startOfflineMachine(controlnumber:String){
//
//        var param = HashMap<String, String>();
//        param.put("userid", getLoginid() + "")
//        param.put("sn", Constant.SN)
//        param.put("loginid", getUsercode())
//        param.put("controlnumber", controlnumber)
//        param.put("sign", SignParamUtil.getSignStr(param))
//        httpPost(Constant.SERVERIP + Constant.startofflinemachine, param, STARTOFFLINEMACHINE);
//    }


    //开启线下机台
    fun startOfflineMachine(controlnumber:String){
        var param = java.util.HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("loginid", getUsercode())
        param.put("controlnumber", controlnumber)
        param.put("sign", SignParamUtil.getSignStr(param))
        val gson = Gson()
        val json: String = gson.toJson(param)
        val requestparams = RequestParams(Constant.SERVERIP+Constant.startofflinemachine)
        requestparams.connectTimeout = 10000
        requestparams.isAsJsonContent = true
        requestparams.bodyContent = json
        requestparams.setHeader("Content-Type","application/json")
        requestparams.charset = "UTF-8"
        //显示加载框
        var dialog = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.dialog_hint2)).show()
        //发送请求
        x.http().post(requestparams, object : Callback.CommonCallback<String> {
            override fun onSuccess(result: String) {
                //停止摄像头
//                mQRCodeView!!.stopCamera()
//                mQRCodeView!!.onDestroy()

                var allresult=GsonUtil.jsonToObject(result,JsonObject::class.java)
//                SToast(allresult!!.get("msg").asString)
                var tisdialog=TisDialog(context).create()
                        .setMessage(allresult!!.get("msg").asString)
                        .setNegativeButtonVisibility(View.GONE)
                        .setPosiButtonVisibility(View.GONE)
                        .show()

                //倒数2秒后结束界面
                object: CountDownTimer(3000,1000){
                    override fun onFinish() {
                        tisdialog.dismiss()
                        //重新开始扫描
                        mQRCodeView!!.startSpot()
                    }
                    override fun onTick(p0: Long) {
                    }
                }.start()
            }

            override fun onError(ex: Throwable, isOnCallback: Boolean) {
                Log.i("failed", "failed")
                if (!httperror_view!!.isShowing){
                    httperror_view!!.show()

                    //倒数2秒后结束界面
                    object: CountDownTimer(2000,1000){
                        override fun onFinish() {
                            httperror_view!!.dismiss()
                            //重新开始扫描
                            mQRCodeView!!.startSpot()
                        }
                        override fun onTick(p0: Long) {
                        }
                    }.start()
                }


            }

            override fun onCancelled(cex: Callback.CancelledException) {

            }

            override fun onFinished() {
                if (dialog!=null && dialog!!.isShowing)
                {
                    dialog!!.dismiss()
                }
            }
        })


    }

    override fun onSystemError(what: Int, errormsg: String, allresult: JsonObject) {
        super.onSystemError(what, errormsg, allresult)
        when(what) {
            STARTOFFLINEMACHINE -> {
                SToast(errormsg)
                finish()
            }
        }
    }


}
