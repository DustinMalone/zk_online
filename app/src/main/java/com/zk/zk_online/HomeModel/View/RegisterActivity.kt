package com.zk.zk_online.HomeModel.View

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.zk.lpw.config.Constant
import com.zk.zk_online.R
import com.zk.zk_online.Utils.HtmlUtils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.apache.http.util.TextUtils



class RegisterActivity : BaseActivity() {


    //注册接口
    val REG=1

    // 手机获取验证码接口
    val GETAUTHCODE=2

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_register)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            //获取验证码按钮
            R.id.btn_register_authcode->{
                if (TextUtils.isEmpty(et_register_zz.text.toString())){
                    SToast("请填写手机号码！")
                }
                else if (!HtmlUtils.isMobileNO(et_register_zz.text.toString())){
                    SToast("请输入正确的手机号码! ")
                }else{
                    getAuthCode()
                }

            }
        //注册按钮
            R.id.btn_register_reg->{
                if (TextUtils.isEmpty(et_register_zz.text.toString())
                        ||TextUtils.isEmpty(et_register_mm.text.toString())
                        ||TextUtils.isEmpty(et_register_authcode.text.toString())){
                    SToast("请完整填写注册信息！")
                }
                else if (!HtmlUtils.isMobileNO(et_register_zz.text.toString())){
                    SToast("请输入正确的手机号码! ")
                }else {
                    reg()
                }
            }
        }

    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        when(bundle.getInt("what")){
            REG->{
                var dialog= Dialog(this)
                var img= ImageView(this)
                img.setImageResource(R.drawable.reg_sussess)
                dialog.setContentView(img)
                var params = dialog.window!!.attributes
                params.gravity = Gravity.CENTER
                params.width = WindowManager.LayoutParams.WRAP_CONTENT
                dialog.window!!.attributes = params
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                //倒数2秒后结束界面
                object: CountDownTimer(2000,1000){
                    override fun onFinish() {
                        dialog.dismiss()
                        finish()
                    }
                    override fun onTick(p0: Long) {
                    }
                }.start()
            }
            GETAUTHCODE->{
                object: CountDownTimer(60000,1000){
                    override fun onFinish() {
                        btn_register_authcode.setText("获取验证码");
                        btn_register_authcode.isEnabled=true//重新获得点击
                        btn_register_authcode.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_FFA54F))
                    }

                    override fun onTick(p0: Long) {
                        btn_register_authcode.setText((p0 / 1000).toString()+"秒后重新获取")
                        btn_register_authcode.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_ff5868))
                        btn_register_authcode.isEnabled=false
                    }
                }.start()
            }
        }
    }


    //注册接口
    fun reg(){

        var param = HashMap<String, String>()
        param.put("loginid",et_register_zz.text.toString())
        param.put("sn", Constant.SN)
        param.put("loginpwd", et_register_mm.text.toString())
        param.put("authcode", et_register_authcode.text.toString())
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.reg, param, REG)
    }

    // 手机获取验证码接口
    fun getAuthCode(){

        var param = HashMap<String, String>()
        param.put("sn", Constant.SN)
        param.put("phone", et_register_zz.text.toString())
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getauthcode, param, GETAUTHCODE)
    }






}
