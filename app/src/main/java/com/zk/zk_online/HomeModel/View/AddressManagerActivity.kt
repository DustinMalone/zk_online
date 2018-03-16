package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Adapter.AndressAdapter
import com.zk.zk_online.HomeModel.Model.Andress
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.MD5Utils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_address_manager.*
import kotlinx.android.synthetic.main.layout_title.*


class AddressManagerActivity : BaseActivity() {


    var adpter: AndressAdapter?=null

    var andressDataList=ArrayList<Andress>()

    val FIND_ADDRESS=1

    val DELADDRESS=2

    //修改地址为默认地址
    val UPDATEDEFAULTADDRESS=3

    //选择地址结果
    val SELECTADDRESSRESULT=0


    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_address_manager)
        setTitle("收货地址")
        img_right!!.setImageResource(R.drawable.new_address)
        img_right.setOnClickListener(this)



    }

     override fun backfinish(view: View) {
         if (adpter!!.list.size!=0) {
             var it = Intent()
             var bundel = Bundle()
             bundel.putSerializable("selectdata", adpter!!.list[adpter!!.getCurryPos()])
             it.putExtras(bundel)
             this.setResult(SELECTADDRESSRESULT, it)
         }
        super.backfinish(view)
    }

    override fun onBackPressed() {
        if (adpter!!.list.size!=0) {
            var it = Intent()
            var bundel = Bundle()
            bundel.putSerializable("selectdata", adpter!!.list[adpter!!.getCurryPos()])
            it.putExtras(bundel)
            this.setResult(SELECTADDRESSRESULT, it)
        }
        super.onBackPressed()
    }

    override fun onClick(v: View?) {

        when(v!!.id){
        //右上角新增地址
        R.id.img_right->{
            var it= Intent(this,AddressDetailActivity::class.java)
            var bu=Bundle();
            bu.putString("type","add")
            bu.putSerializable("data", Andress())
            it.putExtras(bu)
            startActivity(it)
        }
        //中心新增地址
        R.id.iv_whitout_data->{
            var it= Intent(this,AddressDetailActivity::class.java)
            var bu=Bundle();
            bu.putString("type","add")
            bu.putSerializable("data", Andress())
            it.putExtras(bu)
            startActivity(it)
        }

        }

    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        var allresult= GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        when(bundle.getInt("what")) {

            //查看地址列表
            FIND_ADDRESS->{
                var rjson=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
                andressDataList = GsonUtil.jsonToList(rjson!!.getAsJsonArray("data").toString(), Andress::class.java) as ArrayList<Andress>
                for(ob in andressDataList){
                    if (ob.is_default=="1"){
                        ob.isCheck="1"
                    }
                }
                adpter= AndressAdapter(this,andressDataList,object:View.OnClickListener{
                    override fun onClick(p0: View?) {
                        when(p0!!.id){
                            R.id.tv_send_info_del->{
                                delAddress();
                            }
                        }
                    }
                })
                adpter!!.setDefaultAddressListener(object:AndressAdapter.DefaultAddressListener{
                    override fun onClick() {
                        updateDefaultAddress()
                    }
                })
                lv_address_manager_info!!.adapter=adpter



                if( adpter!!.list.size!=0) {
                    adpter!!.list[adpter!!.getCurryPos()].isCheck = "1"
                    iv_whitout_data.visibility=View.GONE
                }else{
                    iv_whitout_data.visibility=View.VISIBLE
                }

            }
            //删除地址
            DELADDRESS->{
                adpter!!.list.removeAt(adpter!!.getCurryPos())
                if( adpter!!.list.size!=0) {
                    adpter!!.list[0].isCheck = "1"
                    iv_whitout_data.visibility=View.GONE
                }else{
                    iv_whitout_data.visibility=View.VISIBLE
                }

                adpter!!.notifyDataSetChanged()
            }
        //修改地址为默认地址
            UPDATEDEFAULTADDRESS->{
                var rjson=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
                andressDataList = GsonUtil.jsonToList(rjson!!.getAsJsonArray("data").toString(), Andress::class.java) as ArrayList<Andress>
                for(ob in andressDataList){
                    if (ob.is_default=="1"){
                        ob.isCheck="1"
                    }
                }
                adpter!!.list=andressDataList
                adpter!!.notifyDataSetChanged()
            }
        }
    }


    //查询收货地址管理接口
    fun findAddress(){

        var param = HashMap<String, String>();
        val sign = "sn=" + Constant.SN +"&type=view"+"&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("type", "view")
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
//        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.address, param, FIND_ADDRESS);
    }


    //删除收货地址
    fun delAddress(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("id", adpter!!.list[adpter!!.getCurryPos()].id)
        param.put("sn", Constant.SN)
        param.put("type", "remove")
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.address, param, DELADDRESS);
    }


    //用户修改默认地址
    fun updateDefaultAddress(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("id", adpter!!.list[adpter!!.getCurryPos()].id)
        param.put("type", "edit")
        param.put("username", adpter!!.list[adpter!!.getCurryPos()].username)
        param.put("province", adpter!!.list[adpter!!.getCurryPos()].province)
        param.put("city", adpter!!.list[adpter!!.getCurryPos()].city)
        param.put("district",adpter!!.list[adpter!!.getCurryPos()].district)
        param.put("sex", adpter!!.list[adpter!!.getCurryPos()].sex)
        param.put("telphone",adpter!!.list[adpter!!.getCurryPos()].telphone)
        param.put("is_default","1")
        param.put("address", adpter!!.list[adpter!!.getCurryPos()].address)

        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.address, param, UPDATEDEFAULTADDRESS);
    }


    override fun onResume() {
        super.onResume()
        Handler().postDelayed(Runnable { findAddress() },200)
    }



}
