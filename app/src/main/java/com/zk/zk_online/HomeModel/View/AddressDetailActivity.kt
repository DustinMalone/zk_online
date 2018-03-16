package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Model.Andress
import com.zk.zk_online.HomeModel.Model.Area
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.HtmlUtils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.FindAddressDialog
import kotlinx.android.synthetic.main.activity_address_detail.*
import kotlinx.android.synthetic.main.activity_address_detail_edit.*

class AddressDetailActivity : BaseActivity() {

    var type:String?=null

    var dataClass:Andress?=null

    //查询地区通用接口
    val A_GETAREABYID=999
    val B_GETAREABYID=998
    val C_GETAREABYID=997
    val D_GETAREABYID=996
    val E_GETAREABYID=995

    //编辑地址保存
    val SAVEADDRESS=1
    //新增地址保存
    val ADDADDRESS=2



    var provincelist=ArrayList<Area>()

    var citylist=ArrayList<Area>()

    var districtlist=ArrayList<Area>()

    override fun init(savedInstanceState: Bundle?) {
        //获取类型（view--查看，edit--编辑,add--添加）
        type=intent.extras.getString("type")
        //获取数据
        dataClass= intent.extras.getSerializable("data") as Andress
        if (type=="view")
        {
            //查看页面--加载这个页面
        setContentView(R.layout.activity_address_detail)
            setTitle("地址管理")
        }else{
            //编辑，添加页面--加载这个页面
            setContentView(R.layout.activity_address_detail_edit)
            setTitle("地址管理")
        }
        //查看页面--初始化控件
        if (type=="view"){
            if (dataClass!!.sex=="男"){
                cb_address_detail_man.isChecked=true
            }else{
                cb_address_detail_lady.isChecked=true
            }
        tv_address_detail_username.setText(dataClass!!.username)
            tv_address_detail_tel.setText(dataClass!!.telphone)
            tv_address_detail_pro.setText(dataClass!!.province)
            tv_address_detail_city.setText(dataClass!!.city)
            tv_address_detail_dri.setText(dataClass!!.district)
            tv_address_detail_address.setText(dataClass!!.address)

        }else if(type=="edit"){//编辑时初始化控件
            if (dataClass!!.sex=="男"){
                cb_address_edit_man.isChecked=true
            }else{
                cb_address_edit_lady.isChecked=true
            }
            cb_address_edit_lady!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(){ button, ischeck ->
                if (ischeck){
                    cb_address_edit_man!!.isChecked=false
                }else
                {
                    cb_address_edit_man!!.isChecked=true
                }
            })

            cb_address_edit_man!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(){ button, ischeck ->
                if (ischeck){
                    cb_address_edit_lady!!.isChecked=false
                }else
                {
                    cb_address_edit_lady!!.isChecked=true
                }
            })
            et_address_edit_username.setText(dataClass!!.username)
            et_address_edit_usertel.setText(dataClass!!.telphone)
            et_address_edit_useraddress.setText(dataClass!!.address)
            sp_address_edit_province.text=dataClass!!.province
            sp_address_edit_city.text=dataClass!!.city
            sp_address_edit_district.text=dataClass!!.district


            getAreaById("0",A_GETAREABYID)
        }
        else{//添加时初始化控件
            cb_address_edit_lady!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(){ button, ischeck ->
                if (ischeck){
                    cb_address_edit_man!!.isChecked=false
                }else
                {
                    cb_address_edit_man!!.isChecked=true
                }
            })

            cb_address_edit_man!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(){ button, ischeck ->
                if (ischeck){
                    cb_address_edit_lady!!.isChecked=false
                }else
                {
                    cb_address_edit_lady!!.isChecked=true
                }
            })


        }



    }

    override fun onClick(v: View?) {
        if (type=="view"){
            when(v!!.id){
                R.id.iv_address_view_back->{
                    finish()
                }
            }
        }else{
            when(v!!.id){
                R.id.iv_address_edit_save->{
                    if (TextUtils.isEmpty(et_address_edit_useraddress.text.toString())
                            ||TextUtils.isEmpty(et_address_edit_username.text.toString())
                            ||TextUtils.isEmpty(et_address_edit_usertel.text.toString())
                            ||TextUtils.isEmpty(sp_address_edit_province!!.text.toString())
                            ||TextUtils.isEmpty(sp_address_edit_city!!.text.toString())
                            ){
                        SToast("请完整填写信息！")
                    }
                    else if (!HtmlUtils.isMobileNO(et_address_edit_usertel.text.toString())){
                        SToast("请输入正确的手机号码! ")
                    }else{
                        if (type=="edit"){
                            saveAddress();
                        }else{
                            addAddress();
                        }

                    }
                }
                R.id.sp_address_edit_province->{

                    getAreaById("0",E_GETAREABYID)
                }
                R.id.sp_address_edit_city->{
                    if (TextUtils.isEmpty(sp_address_edit_province.text.toString())){
                        SToast("请先选择省份!")
                    }else{
                        var areid=""
                        for (pro in provincelist){
                            if (pro.name==sp_address_edit_province.text.toString()){
                                areid=pro.id
                                break
                            }
                        }
                        getAreaById(areid,D_GETAREABYID)
                    }
                }
                R.id.sp_address_edit_district->{
                    if (TextUtils.isEmpty(sp_address_edit_city.text.toString())){
                        SToast("请先选择城市!")
                    }else{
                        var areid=""
                        for (pro in citylist){
                            if (pro.name==sp_address_edit_city.text.toString()){
                                areid=pro.id
                                break
                            }
                        }
                        getAreaById(areid,C_GETAREABYID)
                    }
                }
            }
        }
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        when(bundle.getInt("what")) {
        //省(编辑页面初始化时调用)
            A_GETAREABYID->{
                provincelist= GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
                var areid=""
                for (pro in provincelist){
                   if (pro.name==sp_address_edit_province.text.toString()){
                       areid=pro.id
                       break
                   }

                }

                getAreaById(areid,B_GETAREABYID)

            }

        //市(编辑页面初始化时调用)
            B_GETAREABYID->{
                citylist= GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
            }

        //区(点击调用)
            C_GETAREABYID->{
                districtlist= GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
                var list=ArrayList<String>()
                for (pro in districtlist){
                    list.add(pro.name)

                }
                var addressDialog=FindAddressDialog(this).create()
                        .sendData(list)
                        .setItemlistener{item->
                            sp_address_edit_district.text=item.toString()
                        }.show()


            }

        //D市(点击调用)
            D_GETAREABYID->{
                citylist= GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
                var list=ArrayList<String>()

                for (pro in citylist){
                    list.add(pro.name)
                }

                var addressDialog=FindAddressDialog(this).create()
                        .sendData(list)
                        .setItemlistener{item->
                            sp_address_edit_district.text=""
                            sp_address_edit_city.text=item.toString()
                        }.show()

            }

        //E省(点击调用)
           E_GETAREABYID->{
                provincelist= GsonUtil.jsonToList(result,Area::class.java) as ArrayList<Area>
               var list=ArrayList<String>()

               for (pro in provincelist){
                   list.add(pro.name)
               }
               var addressDialog=FindAddressDialog(this).create()
                       .sendData(list)
                       .setItemlistener{item->
                           sp_address_edit_city.text=""
                           sp_address_edit_district.text=""
                           sp_address_edit_province.text=item.toString()
                       }.show()
            }
            SAVEADDRESS->{
                finish()
            }
            ADDADDRESS->{
                finish()
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

        httpPost(Constant.SERVERIP + Constant.getareabyid, param, what);
    }


    //用户保存发货地址
    fun saveAddress(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("id", dataClass!!.id)
        param.put("type", "edit")
        param.put("username", et_address_edit_username.text.toString())
        param.put("province", sp_address_edit_province!!.text.toString())
        param.put("city", sp_address_edit_city!!.text.toString())

        if (TextUtils.isEmpty(sp_address_edit_district!!.text?.toString())){
            param.put("district", "")
        }else{
            param.put("district",sp_address_edit_district!!.text.toString())
        }
        if(cb_address_edit_man!!.isChecked){
            param.put("sex", "男")
        }
        else{
            param.put("sex", "女")
        }
        param.put("telphone", et_address_edit_usertel.text.toString())
        param.put("is_default", dataClass!!.is_default)
        param.put("address", et_address_edit_useraddress.text.toString())


        param.put("sign", SignParamUtil.getSignStr(param))
//        Log.e("oa",param.toString())
        httpPost(Constant.SERVERIP + Constant.address, param, SAVEADDRESS);
    }



    //用户保存发货地址
    fun addAddress(){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("id", dataClass!!.id)
        param.put("type", "add")
        param.put("username", et_address_edit_username.text.toString())
        param.put("province", sp_address_edit_province!!.text.toString())
        param.put("city", sp_address_edit_city!!.text.toString())

        if (TextUtils.isEmpty(sp_address_edit_district!!.text?.toString())){
            param.put("district", "")
        }else{
            param.put("district",sp_address_edit_district!!.text.toString())
        }
        if(cb_address_edit_man!!.isChecked){
            param.put("sex", "男")
        }
        else{
            param.put("sex", "女")
        }
        param.put("telphone", et_address_edit_usertel.text.toString())
        param.put("is_default", dataClass!!.is_default)
        param.put("address", et_address_edit_useraddress.text.toString())


        param.put("sign", SignParamUtil.getSignStr(param))
        Log.e("oa",param.toString())
        httpPost(Constant.SERVERIP + Constant.address, param, ADDADDRESS);
    }

}
