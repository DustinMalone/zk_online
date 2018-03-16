package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.view.View
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Adapter.UserAdviceAdapter
import com.zk.zk_online.HomeModel.Model.UserAdvice
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user_advice.*


class UserAdviceActivity : BaseActivity() {

    //查询用户反馈信息
    val GETFEEDBACKBYUSER=1

    var  adapter:UserAdviceAdapter?=null

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_user_advice)
        setTitle("反馈结果")

        //查询用户反馈信息
        getFeedBackByUser()
    }

    override fun onClick(v: View?) {
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")

        when(bundle.getInt("what")){
            GETFEEDBACKBYUSER->{
                var rjson=GsonUtil.jsonToList(result,UserAdvice::class.java) as ArrayList<UserAdvice>
                adapter=UserAdviceAdapter(this,rjson)
                lv_useradvice_info!!.adapter=adapter
            }
        }
    }


    //查询用户反馈信息列表
    fun getFeedBackByUser(){

        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getfeedbackbyuser, param, GETFEEDBACKBYUSER)
    }

}
