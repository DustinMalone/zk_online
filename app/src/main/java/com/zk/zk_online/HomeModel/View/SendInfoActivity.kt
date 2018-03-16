package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ListView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Adapter.AndressAdapter
import com.zk.zk_online.HomeModel.Model.Andress
import com.zk.zk_online.HomeModel.Model.GiftAddressAdd
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.MD5Utils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.TisDialog
import kotlinx.android.synthetic.main.activity_send_info.*
import kotlinx.android.synthetic.main.layout_title.*
import java.util.*

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class  SendInfoActivity:BaseActivity()
{

    var lv_send_info_gift:ListView?=null

    var deliveryid:String?=null

    var adpter:AndressAdapter?=null

    var andressDataList=ArrayList<Andress>()

//    var deliveryorder:String?=null

    var code:String?=null

   val  GETDELIVERYORDERDETAIL=1

   val FIND_ADDRESS=2

    val CONFIRMDELIVERYORDER=3

    val DELADDRESS=4

    //修改地址为默认地址
    val UPDATEDEFAULTADDRESS=5

    //选择地址返回的数据的标识
    val SELECTADDRESS=6

    //选择地址返回的数据的标识
    val CREATEPOSTAGEORDER=7

    //判断发货单据是否完成接口
    val CHECKDELIVERYORDERISPAY=8

    //添加地址返回的数据的标识
    val ADDADDRESS=9

    //判断是否选择地址操作
    var isSelectAddress=false


    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_send_info)
        setTitle("收货地址")

        img_right!!.setImageResource(R.drawable.new_address)
        img_right.setOnClickListener(this)

        lv_send_info_gift=findViewById(R.id.lv_send_info_gift)
        ll_send_info_giftdetail!!.visibility=View.VISIBLE
        rl_send_info_sendbtn!!.visibility=View.VISIBLE

        //获取传递的数据
        deliveryid = intent.extras.getString("deliveryid")
//        deliveryorder = intent.extras.getString("deliveryorder")
        code= intent.extras.getString("code")

        //设置列表item点击监听器
        lv_send_info_gift!!.setOnItemClickListener { parent, view, position, id ->
            var it =Intent(this,AddressManagerActivity::class.java)
            startActivityForResult(it,SELECTADDRESS)
        }

            getDeliveryOrderDetail();

        Handler().postDelayed(Runnable { findAddress() },200)


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tv_send_info_goodsdetail->{
                var bd=Bundle();
                bd.putString("deliveryid",deliveryid)
//                bd.putString("deliveryorder",deliveryorder)
                var it= Intent(applicationContext,ProductDetailActivity::class.java)
                it.putExtras(bd)
                startActivity(it)
            }

            R.id.iv_send_info_giftsend->{
                ConfirmdeLiveryOrder();
            }
            //底部新增地址
            R.id.iv_send_info_add_address->{
                var it= Intent(this,AddressDetailActivity::class.java)
                var bu=Bundle();
                bu.putString("type","add")
                bu.putSerializable("data", Andress())
                it.putExtras(bu)
                startActivity(it)
            }
            //右上角新增地址
            R.id.img_right->{
                var it= Intent(this,AddressDetailActivity::class.java)
                var bu=Bundle();
                bu.putString("type","add")
                bu.putSerializable("data", Andress())
                it.putExtras(bu)
                startActivityForResult(it,ADDADDRESS)
            }
            //列表中新增地址
            R.id.tv_whitout_data->{
                var it= Intent(this,AddressDetailActivity::class.java)
                var bu=Bundle();
                bu.putString("type","add")
                bu.putSerializable("data", Andress())
                it.putExtras(bu)
                startActivity(it)
            }
            //检查付款是否完成
            R.id.iv_send_info_check_ispay->{
                checkDeliveryOrderIspay()
            }



        }


    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        var allresult=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
        when(bundle.getInt("what")) {
            GETDELIVERYORDERDETAIL -> {
                var datalist = GsonUtil.jsonToList(result, GiftAddressAdd::class.java) as ArrayList<GiftAddressAdd>

                tv_send_info_goodsdetail.text="共"+allresult!!.get("count").asString+"件"
                if(datalist.size>1){
                    Glide.with(applicationContext)
                            .load(datalist.get(1).pic)
                            .placeholder(R.drawable.wawa_a)
                            .into(iv_send_info_pic_a);

                    iv_send_info_pic_a.visibility=View.VISIBLE
                }

                if(datalist.size>2){
                    Glide.with(applicationContext)
                            .load(datalist.get(2).pic)
                            .placeholder(R.drawable.wawa_a)
                            .into(iv_send_info_pic_b);

                    iv_send_info_pic_b.visibility=View.VISIBLE
                }

                Glide.with(applicationContext)
                        .load(datalist.get(0).pic)
                        .placeholder(R.drawable.wawa_a)
                        .into(iv_send_info_pic);


            }

            FIND_ADDRESS->{
                var rjson=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
                var list = GsonUtil.jsonToList(rjson!!.getAsJsonArray("data").toString(), Andress::class.java) as ArrayList<Andress>
                andressDataList.clear()
                if (list.size!=0){
                andressDataList.add(list[0])
                }
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
                adpter!!.setIssSendGift(true)
                lv_send_info_gift!!.adapter=adpter

                if( adpter!!.list.size!=0) {
                    adpter!!.list[adpter!!.getCurryPos()].isCheck = "1"
                    tv_whitout_data.visibility=View.GONE
                }else{
                    tv_whitout_data.visibility=View.VISIBLE
                }

            }

            CONFIRMDELIVERYORDER->{
                SToast("发货成功")
                finish()
            }

            DELADDRESS->{
                adpter!!.list.removeAt(adpter!!.getCurryPos())
                if( adpter!!.list.size!=0) {
                    adpter!!.list[0].isCheck = "1"
                    tv_whitout_data.visibility=View.GONE
                }else{
                    tv_whitout_data.visibility=View.VISIBLE
                }

                adpter!!.notifyDataSetChanged()
            }

            UPDATEDEFAULTADDRESS->{
                var rjson=GsonUtil.jsonToObject(bundle.getString("allresult"),JsonObject::class.java)
               var  list = GsonUtil.jsonToList(rjson!!.getAsJsonArray("data").toString(), Andress::class.java) as ArrayList<Andress>
                andressDataList.clear()
                if (list.size!=0){
                    andressDataList.add(list[0])
                }
                for(ob in andressDataList){
                    if (ob.is_default=="1"){
                        ob.isCheck="1"
                    }
                }
                adpter!!.list=andressDataList
                adpter!!.notifyDataSetChanged()
            }
        //创建支付邮费订单
            CREATEPOSTAGEORDER->{
                openWebView(allresult!!.get("payurl").asString)
            }
        //判断发货单据是否完成
            CHECKDELIVERYORDERISPAY->{
                finish()
            }
        }

    }


    //获取用户已发送单据通过id获取单据明细目录列表
    fun getDeliveryOrderDetail(){

        var param = HashMap<String, String>();
        val sign ="deliveryid="+deliveryid!!+"&sn=" + Constant.SN +"&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("deliveryid", deliveryid!!)
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP + Constant.getdeliveryorderdetail_new, param, GETDELIVERYORDERDETAIL);
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


    //用户确认发货接口
    fun ConfirmdeLiveryOrder(){
        if (adpter!!.list.size==0){
            SToast("请选择一个地址！")
            return
        }
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("deliveryid", deliveryid!!)
        param.put("deliveryname", adpter!!.list[adpter!!.getCurryPos()].username)
        param.put("deliverytel", adpter!!.list[adpter!!.getCurryPos()].telphone)
        param.put("province", adpter!!.list[adpter!!.getCurryPos()].province)
        param.put("city", adpter!!.list[adpter!!.getCurryPos()].city)
        param.put("district", adpter!!.list[adpter!!.getCurryPos()].district)
        param.put("deliveryaddress",adpter!!.list[adpter!!.getCurryPos()].address)
        param.put("code",code!!)

        param.put("sign", SignParamUtil.getSignStr(param))
//            Log.e("pasod",param.toString())
        httpPost(Constant.SERVERIP + Constant.confirmdeliveryorder, param, CONFIRMDELIVERYORDER);
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==SELECTADDRESS){
            isSelectAddress=true
            andressDataList.clear()
            if (data!=null){
                andressDataList.add(data!!.extras.getSerializable("selectdata") as Andress)
            }
            adpter!!.notifyDataSetChanged()
            if( adpter!!.list.size!=0) {
                tv_whitout_data.visibility=View.GONE
            }else{
                tv_whitout_data.visibility=View.VISIBLE
            }

        }
        //添加地址返回处理
        if(requestCode==ADDADDRESS){
            Handler().postDelayed(Runnable { findAddress() },200)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        if (!isSelectAddress){

        }else{
            isSelectAddress=false
        }
    }

    override fun onSystemError(what: Int, errormsg: String, allresult: JsonObject) {
        when(what){
            //发货失败处理
            CONFIRMDELIVERYORDER->{
                if(allresult.get("errorcode").asInt==-2){
                    var tisDialog=TisDialog(this).create()
                            .setMessage(allresult.get("msg").asString)
                            .setPositiveButton { createPostageOrder() }
                            .setNegativeButton {  }
                            .show()

                }else{
                    super.onSystemError(what, errormsg, allresult)
                }

            }
            //判断为未付款完成状态处理
            CHECKDELIVERYORDERISPAY->{
                if(allresult.get("errorcode").asInt==-2){
                    var tisDialog=TisDialog(this).create()
                            .setMessage("付款未完成,是否完成付款?")
                            .setPositiveButton {
                                if (adpter!!.list.size==0){
                                    SToast("请选择一个地址！")
                                }else{
                                createPostageOrder()
                                }
                            }
                            .setNegativeButton {  }
                            .show()

                }else{
                    finish()
                }
            }
            else->{
                super.onSystemError(what, errormsg, allresult)
            }
        }

    }

    //创建支付邮费订单
    fun createPostageOrder(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("deliveryid", deliveryid!!)
        param.put("sn", Constant.SN)
        param.put("apptype", "Android")
        param.put("appname", Constant.apk_name)
        param.put("appid", Constant.package_name)
        param.put("code", code!!)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.createpostageorder, param, CREATEPOSTAGEORDER);
    }


    //判断发货单据是否完成接口
    fun checkDeliveryOrderIspay(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("deliveryid", deliveryid!!)
        param.put("sn", Constant.SN)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.checkdeliveryorderispay, param, CHECKDELIVERYORDERISPAY);
    }



    //打开h5支付
    fun openWebView(callbackurl: String) {
        var webview: WebView? = WebView(this)
        webview!!.settings.javaScriptEnabled = true;
        webview!!.setWebViewClient(object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                Log.i("go_pay", "go_pay")
                if (url!!.startsWith("weixin://wap/pay?")) {
                    var intent = Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    iv_send_info_giftsend.visibility=View.GONE
                    iv_send_info_check_ispay.visibility=View.VISIBLE
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        })

        webview!!.loadUrl(callbackurl)

    }


}