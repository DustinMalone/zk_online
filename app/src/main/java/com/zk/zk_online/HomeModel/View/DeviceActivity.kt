package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zk.lpw.config.Constant
import com.zk.zk_online.Adapter.DeviceAdapter
import com.zk.zk_online.HomeModel.Model.DeviceBean
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.MD5Utils
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.circleImageView

/**
 * 机台列表页
 * Created by ZYB on 2017/11/8 0008.
 */
public class DeviceActivity : BaseActivity()
{

    var swip_layout:SwipeRefreshLayout?=null
    var grid_view:GridView?=null;
    var adapter: DeviceAdapter?=null
    var img_user: circleImageView?=null
    var tv_user_name:TextView?=null
    var tv_device_coin_price:TextView?=null

    //获取房间内设备成功标记
    var DEVICE_HANDLER_CODE=0

    var roomid:String?=null;

    var price_coin:String?=null;

    var datalist=ArrayList<DeviceBean>();

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_play_info)
        setTitle("机台")
        roomid=intent.getStringExtra("roomid");
        price_coin=intent.getStringExtra("price_coin");
        swip_layout=findViewById(R.id.swip_layout)
        grid_view=findViewById(R.id.gridview)
        img_user=findViewById(R.id.img_user)
        tv_user_name=findViewById(R.id.tv_user_name)
        tv_device_coin_price=findViewById(R.id.tv_device_coin_price)
        tv_device_coin_price!!.text=price_coin+"币/次"
        grid_view!!.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            var intent=Intent(applicationContext,PlayActivity::class.java);
            intent.putExtra("deviceid",datalist[i].machinecode)
            intent.putExtra("ip",datalist[i].ip)
            intent.putExtra("roomid",roomid);
            intent.putExtra("machineid",datalist[i].machineid)
            intent.putExtra("price",datalist[i].price_coin)
            intent.putExtra("machinename",datalist[i].machinename)
            startActivity(intent)
        })
        setData()//默认空列表 让swip在grid为空的时候能够下拉
        swip_layout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            setShowPro(false)
            getDevice()
        })

        tv_user_name!!.text=getUsername();
        Glide.with(this).load(getUserimg())
                .into(img_user)

        getDevice()
    }

    override fun onClick(v: View?) {

    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        when(bundle.getInt("what"))
        {
            DEVICE_HANDLER_CODE ->
            {
                datalist=GsonUtil.jsonToList(result,DeviceBean::class.java) as ArrayList<DeviceBean>
                setData()
                if (swip_layout!!.isRefreshing)
                {
                    swip_layout!!.isRefreshing=false;
                }
            }
        }

    }


    fun getDevice()
    {
        var sign="loginid=test"+"&roomid="+roomid!!+"&sn="+Constant.SN+"&"+Constant.KEY
        var param=HashMap<String,String>();
        param.put("sn",Constant.SN);
        param.put("loginid","test")
        param.put("roomid",roomid!!)
        param.put("sign",MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP+Constant.getroomdetail,param,DEVICE_HANDLER_CODE)

    }


    fun setData(){
        adapter= DeviceAdapter(this,datalist)
        grid_view!!.adapter=adapter
    }


    final override fun onHttpError() {
        super.onHttpError()
        if(swip_layout!!.isRefreshing)
        {
            swip_layout!!.isRefreshing=false
        }
    }
}