package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Adapter.GiftShopAdapter
import com.zk.zk_online.HomeModel.Model.BannerBean
import com.zk.zk_online.HomeModel.Model.GiftShop
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.GlideImageLoader
import kotlinx.android.synthetic.main.activity_gift_shop.*

class GiftShopActivity : BaseActivity() {

    //banner下载前的展示图
    var tempImages=java.util.ArrayList<Int>();

    var banner:Banner?=null;

    var adapter_gift_shop:GiftShopAdapter?=null

    //banner标识
    val BANNER_MES=1;
    //获取兑换商城礼品列表
    val GETGOODSLIST=2
    //获取用户可兑换公仔数接口
    val GETEXCHANGENUM=3


    //广告图返回列表
    var bannersDataList=ArrayList<BannerBean>()

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_gift_shop)

        //用户头像
        Glide.with(this)
                .load(getUserimg())
                .into(civ_gift_shop_userimg)

        var bannerHeaderView=View.inflate(this,R.layout.item_gift_shop_banner,null)
        banner=bannerHeaderView!!.findViewById(R.id.item_gift_shop_banner_content)
        hgv_gift_shop!!.addHeaderView(bannerHeaderView)

        //banner默认图片
        tempImages.add(R.drawable.default_img)
        //banner设置
        banner!!.setDelayTime(3000);//设置轮播时间
        banner!!.setImageLoader(GlideImageLoader());
        banner!!.setImages(tempImages).start()
        banner!!.setOnBannerListener(OnBannerListener{
            position ->
            if (bannersDataList[position].is_skip=="1"){
                var it= Intent(this,BannerInfoActivity::class.java)
                it.putExtra("banner_content",bannersDataList[position].banner_content)
                startActivity(it)
            }
        })

        hgv_gift_shop!!.setOnItemClickListener { parent, view, position, id ->
            //减去头布局占的位置
            var i=position-2;
            if (i>=0){
            var it= Intent(this,GiftShopDetailActivity::class.java)
                it.putExtra("wawaimg",adapter_gift_shop!!.list[i].pic)
                it.putExtra("id",adapter_gift_shop!!.list[i].id)
                it.putExtra("goodsprice",adapter_gift_shop!!.list[i].goodsprice)
                it.putExtra("goodsid",adapter_gift_shop!!.list[i].goodsid)
                it.putExtra("exchangenum",adapter_gift_shop!!.list[i].exchangenum)
                startActivity(it)
            }
        }


    }

    override fun onClick(v: View?) {
    }

    override fun getMessage(bundle: Bundle) {
        var allresult = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        var result=bundle.getString("msg")
        when(bundle.getInt("what")){
            //banner图
            BANNER_MES ->{
                bannersDataList=GsonUtil.jsonToList(result, BannerBean::class.java) as ArrayList<BannerBean>
                var images=java.util.ArrayList<String>();
                for (item in bannersDataList)
                {
                    images.add(item.pic)
                }
                if(images.isNotEmpty()&&images.size!=0){
                    banner!!.update(images);
                }
            }
            //获取商城接口
            GETGOODSLIST->{
                var giftList=GsonUtil.jsonToList(result, GiftShop::class.java) as ArrayList<GiftShop>
                adapter_gift_shop= GiftShopAdapter(this,giftList)
                hgv_gift_shop!!.adapter=adapter_gift_shop
            }
            //获取用户可兑换公仔数接口
            GETEXCHANGENUM->{
                tv_gift_shop_usergiftnum.text=allresult!!.get("exchangenum").asString

            }
        }
    }

    override fun backfinish(view: View) {
        super.backfinish(view)
    }

    //请求banner广告图
    fun requestBanner(){
        var param=HashMap<String,String>();
        param.put("sn", Constant.SN);
        param.put("userid",getLoginid());
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP+ Constant.getbannber,param,BANNER_MES)
    }


    //获取商城兑换商品列表接口
    fun getGoodsList(){
        var param=HashMap<String,String>();
        param.put("sn", Constant.SN);
        param.put("userid",getLoginid());
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP+ Constant.getgoodslist,param,GETGOODSLIST)
    }

    // 获取用户可兑换公仔数接口
    fun getExchangeNum(){
        var param=HashMap<String,String>();
        param.put("sn", Constant.SN);
        param.put("userid",getLoginid());
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP+ Constant.getexchangenum,param,GETEXCHANGENUM)
    }

    override fun onResume() {
        super.onResume()
        //请求banner接口
        requestBanner()
        //获取商城列表接口
        getGoodsList()
        //获取用户可兑换公仔数接口
        getExchangeNum()
    }

}
