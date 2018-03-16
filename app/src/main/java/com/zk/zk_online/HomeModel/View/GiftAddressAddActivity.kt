package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Model.Area
import com.zk.zk_online.HomeModel.Model.GiftAddressAdd
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.HtmlUtils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.FindAddressDialog
import kotlinx.android.synthetic.main.activity_gift_address_add.*

class GiftAddressAddActivity : BaseActivity() {

    var tv_gift_address_add_identifier:TextView?=null
    var tv_gift_address_add_goodsnum:TextView?=null
    var tv_gift_address_add_goodsname:TextView?=null
    var iv_gift_address_add_pic:ImageView?=null
    var sp_province:TextView?=null
    var sp_city:TextView?=null
    var sp_district:TextView?=null



    var cb_gift_address_man:CheckBox?=null

    var cb_gift_address_lady:CheckBox?=null

    //查询地区通用接口
    val A_GETAREABYID=999
    val B_GETAREABYID=998
    val C_GETAREABYID=997

    //获取用户已发送单据通过id获取单据明细目录列表
    val GETDELIVERYORDERDETAIL=1

    //用户确认发货接口
    val CONFIRMDELIVERYORDER=2

    //用户保存地址
    val SAVEADDRESS=3

    var datalist=ArrayList<GiftAddressAdd>()

    var provincelist=ArrayList<Area>()

    var citylist=ArrayList<Area>()

    var districtlist=ArrayList<Area>()

    var deliveryid:String?=null

    var deliveryorder:String?=null

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_gift_address_add)
        setTitle("添加地址")
        tv_gift_address_add_identifier=findViewById(R.id.tv_gift_address_add_identifier)
        tv_gift_address_add_goodsnum=findViewById(R.id.tv_gift_address_add_goodsnum)
        tv_gift_address_add_goodsname=findViewById(R.id.tv_gift_address_add_goodsname)
        iv_gift_address_add_pic=findViewById(R.id.iv_gift_address_add_pic)
        cb_gift_address_lady=findViewById(R.id.cb_gift_address_lady)
        cb_gift_address_man=findViewById(R.id.cb_gift_address_man)
        sp_province=findViewById(R.id.sp_province)
        sp_city=findViewById(R.id.sp_city)
        sp_district=findViewById(R.id.sp_district)

        sp_province!!.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
               if( p0.isNullOrEmpty()){
                   sp_province!!.setHint("选择省份")
                }
            }
        })

        sp_city!!.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if( p0.isNullOrEmpty()){
                    sp_city!!.setHint("选择城市")
                }
            }
        })

        sp_district!!.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if( p0.isNullOrEmpty()){
                    sp_district!!.setHint("选择区/县")
                }
            }
        })

        deliveryid=intent.extras.getString("deliveryid")
        deliveryorder=intent.extras.getString("deliveryorder")


        cb_gift_address_lady!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(){button, ischeck ->
            if (ischeck){
                cb_gift_address_man!!.isChecked=false
            }else
            {
                cb_gift_address_man!!.isChecked=true
            }
        })

        cb_gift_address_man!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(){button, ischeck ->
            if (ischeck){
                cb_gift_address_lady!!.isChecked=false
            }else
            {
                cb_gift_address_lady!!.isChecked=true
            }
        })





        getDeliveryOrderDetail();



    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tv_gift_address_add_detail->{
                var bd=Bundle();
                bd.putString("deliveryid",deliveryid)
                bd.putString("deliveryorder",deliveryorder)
                var it= Intent(applicationContext,ProductDetailActivity::class.java)
                it.putExtras(bd)
                startActivity(it)
            }
            R.id.iv_gift_address_add_send->{
                if (TextUtils.isEmpty(et_gift_address_add_useraddress.text.toString())
                        ||TextUtils.isEmpty(et_gift_address_add_username.text.toString())
                        ||TextUtils.isEmpty(et_gift_address_add_usertel.text.toString())
                        ||TextUtils.isEmpty(sp_province!!.text.toString())
                        ||TextUtils.isEmpty(sp_city!!.text.toString())
                        ){
                    SToast("请完整填写信息！")
                }else if (!HtmlUtils.isMobileNO(et_gift_address_add_usertel.text.toString())){
                    SToast("请输入正确的手机号码! ")
                }else{
                    saveAddress();
                }
            }
            R.id.sp_province->{
              getAreaById("0",A_GETAREABYID)

            }
            R.id.sp_city->{
                if (TextUtils.isEmpty(sp_province!!.text.toString())){
                    SToast("请先选择省份!")
                }else{
                    var aid=""
                    for (pro in provincelist){
                       if(pro.name==sp_province!!.text.toString()){
                           aid=pro.id
                           break
                       }
                    }
                getAreaById(aid,B_GETAREABYID)
                }

            }
            R.id.sp_district->{
                if (TextUtils.isEmpty(sp_city!!.text.toString())){
                    SToast("请先选择城市!")
                }else{
                    var aid=""
                    for (pro in citylist){
                        if(pro.name==sp_city!!.text.toString()){
                            aid=pro.id
                            break
                        }
                    }
                    getAreaById(aid,C_GETAREABYID)
                }

            }
        }
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        when(bundle.getInt("what")){
            GETDELIVERYORDERDETAIL->{

                datalist=GsonUtil.jsonToList(result,GiftAddressAdd::class.java) as ArrayList<GiftAddressAdd>

                Glide.with(applicationContext)
                        .load(datalist.get(0).pic)
                        .placeholder(R.drawable.wawa_a)
                        .into(iv_gift_address_add_pic);

                tv_gift_address_add_goodsname!!.setText(datalist.get(0).goodsname)
                tv_gift_address_add_goodsnum!!.setText("x"+datalist.get(0).goodsnum)
                tv_gift_address_add_identifier!!.setText("编号："+datalist.get(0).deliveryid)

            }
            //省
            A_GETAREABYID->{
                provincelist=GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
                var list=ArrayList<String>()
                for (pro in provincelist){
                    list.add(pro.name)
                }
                var address_dialog= FindAddressDialog(this).create()
                        .sendData(list)
                        .setItemlistener{item ->
                            sp_city!!.text=""
                            sp_district!!.text=""
                            sp_province!!.text=item.toString()
                    }.show()

            }

        //市
            B_GETAREABYID->{
                citylist=GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
                var list=ArrayList<String>()
                for (pro in citylist){
                    list.add(pro.name)
                }

                var address_dialog= FindAddressDialog(this).create()
                        .sendData(list)
                        .setItemlistener {item ->
                            sp_district!!.text=""
                    sp_city!!.text=item.toString()
                }.show()
            }

        //区
            C_GETAREABYID->{
                setShowPro(false)
                districtlist=GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
                var list=ArrayList<String>()
                for (pro in districtlist){
                    list.add(pro.name)
                }

                var address_dialog= FindAddressDialog(this).create()
                        .sendData(list)
                        .setItemlistener {item ->
                    sp_district!!.text=item.toString()
                }.show()
            }
        //用户确认发货接口
            CONFIRMDELIVERYORDER->{
                finish()
            }

        //用户保存地址
            SAVEADDRESS->{
                ConfirmdeLiveryOrder()
            }

        }
    }

    //查询收货地址管理接口
    fun getAreaById(areaid:String,what:Int){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("areaid", areaid)
        param.put("sign", SignParamUtil.getSignStr(param))
//        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getareabyid, param, what);
    }


    //获取用户已发送单据通过id获取单据明细目录列表
    fun getDeliveryOrderDetail(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("deliveryid", deliveryid!!)


        param.put("sign", SignParamUtil.getSignStr(param))

        Log.e("ddd",param.toString())
        httpPost(Constant.SERVERIP + Constant.getdeliveryorderdetail_new, param, GETDELIVERYORDERDETAIL);
    }


    //用户确认发货接口
    fun ConfirmdeLiveryOrder(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("deliveryid", deliveryid!!)
        param.put("deliveryname", et_gift_address_add_username.text.toString())
        param.put("deliverytel", et_gift_address_add_usertel.text.toString())
        param.put("province", sp_province!!.text.toString())
        param.put("city", sp_city!!.text.toString())

        if (TextUtils.isEmpty(sp_district!!.text?.toString())){
            param.put("district", "")
        }else{
            param.put("district",sp_district!!.text.toString())
        }
        param.put("deliveryaddress", et_gift_address_add_useraddress.text.toString())

        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.confirmdeliveryorder, param, CONFIRMDELIVERYORDER);
    }



    //用户保存发货地址
    fun saveAddress(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("type", "add")
        param.put("username", et_gift_address_add_username.text.toString())
        param.put("province", sp_province!!.text.toString())
        param.put("city", sp_city!!.text.toString())
        Log.e("sp_district",sp_district!!.text?.toString()+"yes")
        if (TextUtils.isEmpty(sp_district!!.text?.toString())){
            param.put("district", "")
        }else{
            param.put("district",sp_district!!.text.toString())
        }
        if(cb_gift_address_man!!.isChecked){
            param.put("sex", "男")
        }
        else{
            param.put("sex", "女")
        }
        param.put("telphone", et_gift_address_add_usertel.text.toString())
        param.put("is_default", "1")
        param.put("address", et_gift_address_add_useraddress.text.toString())


        param.put("sign", SignParamUtil.getSignStr(param))
        Log.e("oa",param.toString())
        httpPost(Constant.SERVERIP + Constant.address, param, SAVEADDRESS);
    }


}
