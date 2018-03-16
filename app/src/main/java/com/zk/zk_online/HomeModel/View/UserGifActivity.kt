package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.Adapter.SendGifAdapter
import com.zk.zk_online.HomeModel.Adapter.HasSendGifAdapter
import com.zk.zk_online.HomeModel.Model.HasSendData
import com.zk.zk_online.HomeModel.Model.SendData
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.MD5Utils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.TisDialog

/**
 * Created by ZYB on 2017/11/6 0006.
 */
class  UserGifActivity :BaseActivity(){

    var lv_send:ListView?=null
    var adapter:SendGifAdapter?=null

    var img_send:ImageView?=null

    var img_gift_back:ImageView?=null

    var has_send_adapter:HasSendGifAdapter?=null

    var tv_unsend_gift:TextView?=null

    var tv_send_gift:TextView?=null

    var iv_whitout_data:ImageView?=null


    val UNSEND_GIFT=0

    val SEND_GIFT=1

    //查询地址
    val FIND_ADDRESS=2

    //创建发货单
    val CREATEDELIVERYORDER=3

    //检测礼品是否可以加入接口
    val CHECKGIFTBYID=4

    //礼品兑换游戏币
    val GIFTEXCHANGECOINS=5


    //商品发货明细返回
    val GoodsDetail_Finish=1001

    var savedInstance:Bundle?=null

    var hasdatalist=ArrayList<HasSendData>()

    //选中列表
    var isCheckList=HashMap<Int,String>()

    //当前选中坐标
    var currySelPosition:Int=-1

    //传输数据
    var sendDataBundle=Bundle()

    //是否商品发货明细返回
    var isGoodsDetail_Finish=false



    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_user_gif)
        setTitle("我的礼品")
        savedInstance=savedInstanceState
        lv_send=findViewById(R.id.lv_send)
        img_send=findViewById(R.id.img_send)
        img_send!!.setOnClickListener(this)

        img_gift_back=findViewById(R.id.img_gift_back)
        tv_unsend_gift=findViewById(R.id.tv_unsend_gift)
        tv_send_gift=findViewById(R.id.tv_send_gift)
        iv_whitout_data=findViewById(R.id.iv_whitout_data)

        lv_send!!.onItemClickListener = object:AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0!!.adapter::class.java==HasSendGifAdapter::class.java){
                    var it=Intent(applicationContext,ProductDetailActivity::class.java)
                    var bu=Bundle()


                    bu.putString("deliveryid",hasdatalist[p2].deliveryid)
                    bu.putString("type", "old")
                    it.putExtras(bu)
                    startActivityForResult(it,GoodsDetail_Finish)
                }
            }
        }


//        getUnSendGiftList()
//        setData();
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.img_send  -> {
//                var v=LayoutInflater.from(this).inflate(R.layout.window_send_gif,null);
//                var dialog = Dialog(this);
//                dialog.setContentView(v);
//                dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog.show();
                if (isCheckList.isEmpty()){
                    SToast("请选择礼物!")
                    return
                }
                else{
                    createDeliveryOrder()

                }

            }
            R.id.img_gift_back  -> {
                finish()
            }
            R.id.tv_send_gift  -> {
                tv_unsend_gift!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))

                tv_send_gift!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_FFA54F))

                img_gift_back!!.setImageResource(R.drawable.jp_29)
                img_send!!.visibility=View.GONE
                var  dra: Drawable = ContextCompat.getDrawable(applicationContext,R.drawable.wodelipin_f);
                dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
                tv_send_gift!!.setCompoundDrawables(null,null,null,dra)

                tv_unsend_gift!!.setCompoundDrawables(null,null,null,null)
                getSendGiftList()
            }

            R.id.tv_unsend_gift  -> {
                tv_send_gift!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                tv_unsend_gift!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_FFA54F))

                isCheckList.clear()
                img_gift_back!!.setImageResource(R.drawable.gift_back)
                img_send!!.visibility=View.VISIBLE

                var  dra: Drawable = ContextCompat.getDrawable(applicationContext,R.drawable.wodelipin_f);
                dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
                tv_send_gift!!.setCompoundDrawables(null,null,null,null)

                tv_unsend_gift!!.setCompoundDrawables(null,null,null,dra)
                getUnSendGiftList()
            }

        }
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        var rjson=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
        when(bundle.getInt("what")){
            //未发货列表
            UNSEND_GIFT->{
                 var rjson=GsonUtil.jsonToList(result, SendData::class.java) as ArrayList<SendData>
                adapter= SendGifAdapter(this,rjson,isCheckList)
                adapter!!.setCheckBoxListener(object:SendGifAdapter.CheckBoxListener{
                    override fun check(buttonview: View, isCheck: Boolean, position: Int) {
                        currySelPosition=position
                        checkGiftById(adapter!!.list[position].machine_coin_id)

                    }

                })
                lv_send!!.adapter=adapter

                if (adapter!!.list.isEmpty()) {
                    iv_whitout_data!!.visibility = View.VISIBLE
                }else{
                    iv_whitout_data!!.visibility = View.GONE
                }

            }
            //已发货列表
            SEND_GIFT->{
                 hasdatalist=GsonUtil.jsonToList(result, HasSendData::class.java) as ArrayList<HasSendData>
                has_send_adapter= HasSendGifAdapter(this,hasdatalist)
                lv_send!!.adapter=has_send_adapter

                if (has_send_adapter!!.list.isEmpty()) {
                    iv_whitout_data!!.visibility = View.VISIBLE
                }else{
                    iv_whitout_data!!.visibility = View.GONE
                }
            }

        //地址列表
            FIND_ADDRESS->{
                var rjson= GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
                if (rjson!!.getAsJsonArray("data").size()==0)
                {
                    isCheckList.clear()
                    var it=Intent(applicationContext,GiftAddressAddActivity::class.java)
                    it.putExtras(sendDataBundle)
                    startActivity(it)
                }else{
                    isCheckList.clear()
                    var it=Intent(applicationContext,SendInfoActivity::class.java)
                    it.putExtras(sendDataBundle)
                    startActivity(it)
                }

            }
            //创建单据
            CREATEDELIVERYORDER->{

                var rjson=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
                sendDataBundle.putString("deliveryid",rjson!!.get("deliveryid").asString)
                sendDataBundle.putString("code", rjson!!.get("code").asString)

                //清除选中列表
                isCheckList.clear()
                var it=Intent(applicationContext,SendInfoActivity::class.java)
                it.putExtras(sendDataBundle)
                startActivity(it)

                //查询地址
//                findAddress()
            }
        //检测礼品是否可以加入
            CHECKGIFTBYID->{
                adapter!!.list[currySelPosition].isCheck=true
                adapter!!.isChecklist.put(currySelPosition,adapter!!.list[currySelPosition].machine_coin_id)
                adapter!!.notifyDataSetChanged()
                currySelPosition=-1;
            }
            //礼品兑换游戏币
            GIFTEXCHANGECOINS->{
                SToast(rjson!!.get("msg").asString)
                adapter!!.list.removeAt(currySelPosition)
                adapter!!.notifyDataSetChanged()
                currySelPosition=-1

            }

        }

    }

    fun setData(){

//        var list=ArrayList<SendData>()
//        for (i in 1..6)
//        {
//            var item= SendData("墨镜超人")
//            list.add(item)
//        }
//
//        adapter= SendGifAdapter(this,list)
//        lv_send!!.adapter=adapter
    }


    //请求获得未发送礼品列表
    fun getUnSendGiftList(){

        var param = HashMap<String, String>()
        val sign = "sn=" + Constant.SN + "&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP + Constant.unsend_gift_list, param, UNSEND_GIFT)
    }

    //请求获得已发送礼品列表
    fun getSendGiftList(){

        var param = HashMap<String, String>()
        val sign = "sn=" + Constant.SN + "&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP + Constant.getdeliveryorder, param, SEND_GIFT)
    }


    //查询收货地址管理接口
    fun findAddress(){

        var param = HashMap<String, String>()
        val sign = "sn=" + Constant.SN +"&type=view"+"&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("type", "view")
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP + Constant.address, param, FIND_ADDRESS)
    }


    //选择未发货礼品创建发货单
    fun createDeliveryOrder(){

        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        var goodsid=""
        for ( gif in isCheckList)
        {
            goodsid+=","
            goodsid+=gif.value

        }
        goodsid= goodsid.subSequence(1, goodsid.length).toString()
         goodsid="("+goodsid+")"
        param.put("data", goodsid)
        val sign ="data="+goodsid+"&sn=" + Constant.SN + "&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
//        Log.e("asd",param.toString())
        httpPost(Constant.SERVERIP + Constant.createdeliveryorder, param, CREATEDELIVERYORDER)
    }

    override fun onResume() {
        super.onResume()
        if (!isGoodsDetail_Finish){
             getUnSendGiftList()
        }
        isGoodsDetail_Finish=false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GoodsDetail_Finish){
            isGoodsDetail_Finish=true
        }
    }



    //查询收货地址管理接口
    fun checkGiftById(machine_coin_id:String){
        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("machine_coin_id",machine_coin_id)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.checkgiftbyid, param, CHECKGIFTBYID)
    }


    //礼品兑换游戏币接口
    fun giftExchangeCoins(machine_coin_id:String){
        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("machine_coin_id",machine_coin_id)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.giftexchangecoins, param, GIFTEXCHANGECOINS)
    }

    override fun onSystemError(what: Int, errormsg: String, allresult: JsonObject) {

        when(what){
            //检查礼品失败处理
            CHECKGIFTBYID->{
                if(allresult.get("errorcode").asInt==-2){
                    var tisDialog=TisDialog(this).create()
                            .setMessage("礼品无库存是否替换游戏币?\n可以替换"+allresult.get("coins").asString+"游戏币")
                            .setPositiveButton { giftExchangeCoins(adapter!!.list[currySelPosition].machine_coin_id) }
                            .setNegativeButton {
                                currySelPosition=-1
                                adapter!!.notifyDataSetChanged()
                            }
                            .show()

                }else{
                    currySelPosition=-1
                    adapter!!.notifyDataSetChanged()
                    super.onSystemError(what, errormsg, allresult)
                }
            }
            //礼品兑换失败处理
            GIFTEXCHANGECOINS->{
                super.onSystemError(what, errormsg, allresult)
                currySelPosition=-1
                adapter!!.notifyDataSetChanged()
            }
            else->{
                super.onSystemError(what, errormsg, allresult)
            }
        }
    }

    override fun onHttpError() {
        super.onHttpError()
        currySelPosition=-1
        adapter!!.notifyDataSetChanged()
    }

}