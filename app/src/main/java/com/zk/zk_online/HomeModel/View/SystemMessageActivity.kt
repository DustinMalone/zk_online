package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.view.View
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Adapter.SystemMessageAdapter
import com.zk.zk_online.HomeModel.Model.SystemMessage
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_system_message.*

class SystemMessageActivity : BaseActivity() {

    var systemMsgAdapter:SystemMessageAdapter?=null

    var systemMsgList=ArrayList<SystemMessage>()

    val GETSYSMESSAGE=1

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_system_message)
        setTitle("系统消息")
        getSystemMsgData();
    }

    override fun onClick(v: View?) {

    }

    override fun getMessage(bundle: Bundle) {
        var result =bundle.getString("msg")

        when(bundle.getInt("what")){
            GETSYSMESSAGE->{
                systemMsgList=GsonUtil.jsonToList(result,SystemMessage::class.java) as ArrayList<SystemMessage>
                systemMsgAdapter=SystemMessageAdapter(applicationContext,systemMsgList)
                lv_system_message_info.adapter=systemMsgAdapter
            }
        }

    }

    //请求获得系统消息
    fun getSystemMsgData(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign",SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getsysmessage, param, GETSYSMESSAGE);
    }

}
