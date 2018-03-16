package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import com.zk.lpw.config.Constant
import com.zk.zk_online.Adapter.ProductAdapter
import com.zk.zk_online.HomeModel.Model.ProductDetail
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class ProductDetailActivity : BaseActivity()
{

    var lv_product: ListView?=null;
    var adapter: ProductAdapter?=null;
    var img_back:ImageView?=null

    var deliveryid:String?=null

//    var deliveryorder:String?=null

    var type:String?=null

    val GETDELIVERYORDERDETAIL=1

    var datalist=ArrayList<ProductDetail>()

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_product_detail)
        setTitle("礼品详情")
        lv_product=findViewById(R.id.lv_product)
        img_back=findViewById(R.id.img_back);
        img_back!!.setOnClickListener(this)

        deliveryid=intent.extras.getString("deliveryid")
//        deliveryorder=intent.extras.getString("deliveryorder")

        if(intent.extras.containsKey("type")){
            type=intent.extras.getString("type")
        }

        getDeliveryOrderDetail()

    }

    override fun onClick(v: View?) {
        finish();
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        when(bundle.getInt("what")) {
            GETDELIVERYORDERDETAIL -> {
                datalist = GsonUtil.jsonToList(result, ProductDetail::class.java) as ArrayList<ProductDetail>
                adapter= ProductAdapter(applicationContext,datalist)
                lv_product!!.adapter=adapter
            }
        }
    }


    //获取用户已发送单据通过id获取单据明细目录列表
    fun getDeliveryOrderDetail(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        if(TextUtils.isEmpty(deliveryid?.toString())){
            param.put("deliveryid", "")
        }
        else{
        param.put("deliveryid", deliveryid!!)
        }
        param.put("sign", SignParamUtil.getSignStr(param))
        if (TextUtils.isEmpty(type)){
        httpPost(Constant.SERVERIP + Constant.getdeliveryorderdetail_new, param, GETDELIVERYORDERDETAIL);
        }else{
            httpPost(Constant.SERVERIP + Constant.getdeliveryorderdetail, param, GETDELIVERYORDERDETAIL);
        }
    }
}