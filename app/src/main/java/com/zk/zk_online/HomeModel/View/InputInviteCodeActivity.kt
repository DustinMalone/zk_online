package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_input_invite_code.*

class InputInviteCodeActivity : BaseActivity() {

    val INPUTINVICECODE=1

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_input_invite_code)
        setTitle("输入邀请码")
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_input_code_gain->{
                if(!TextUtils.isEmpty(et_input_invite_code.text.toString())) {
                    inputInviceCode()
                }else{
                    SToast("请输入邀请码！")
                }
            }
        }
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        when(bundle.getInt("what")){
            INPUTINVICECODE->{
                var rjson=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
                SToast("成功获得"+rjson!!.get("presentcoin").asString+"币")
            }
        }
    }


    //输入邀请码赠币接口
    fun inputInviceCode(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sharecode", et_input_invite_code.text.toString())
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getsharecode, param, INPUTINVICECODE);
    }
}
