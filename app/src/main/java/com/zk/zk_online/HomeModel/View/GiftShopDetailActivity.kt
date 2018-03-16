package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.HomeModel.Adapter.GiftShopDetailAdapter
import com.zk.zk_online.HomeModel.Model.GiftShopDetail
import com.zk.zk_online.HomeModel.Model.WindowGiftShopDetail
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.GIftShopDetailDialog
import com.zk.zk_online.weight.TisDialog
import kotlinx.android.synthetic.main.activity_gift_shop_detail.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class GiftShopDetailActivity : BaseActivity() {

    var goodsid=""
    var exchangenum=""
    var wawaimg=""
    var goodsprice=""
    //商品ID
    var id=""

    var billnum=""

    //选中的娃娃适配器
    var has_check_adapter: GiftShopDetailAdapter?=null

    //获取礼品详细信息
    val GETGOODDETAIL=1
    //获取可兑换礼品接口
    val GETEXCHANGELIST=2
    //确定兑换礼品接口
    val CONFIRMEXCHANGE=3
    //获取用户的娃娃数
    val GETEXCHANGENUM=4
    //app创建商城物品支付订单接口
    val CREATEEXCHANGEORDER=5
    //确认兑换娃娃是否支付接口
    val CHECKEXCHANGEISPAY=6






    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_gift_shop_detail)

        //获取页面传输的数据
        goodsid = intent.getStringExtra("goodsid")
        exchangenum= intent.getStringExtra("exchangenum")
        wawaimg= intent.getStringExtra("wawaimg")
        id= intent.getStringExtra("id")
        goodsprice=intent.getStringExtra("goodsprice")
        //初始化控件
        Glide.with(this).load(wawaimg)
                .listener(object:RequestListener<String,GlideDrawable>{
                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        iv_gift_shop_detail_wawaimg.setImageDrawable(resource)
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(iv_gift_shop_detail_wawaimg)
        Glide.with(this).load(getUserimg())
                .listener(object:RequestListener<String,GlideDrawable>{
                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        civ_gift_shop_userimg.setImageDrawable(resource)
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(civ_gift_shop_userimg)
        tv_gift_shop_detail_price.text="￥"+goodsprice
        tv_gift_shop_detail_count.text=exchangenum
        tv_gift_shop_sumcount.text="(0/"+exchangenum+")"
        has_check_adapter=GiftShopDetailAdapter(this, ArrayList<GiftShopDetail>())
        rv_gift_shop_giftlist!!.adapter=has_check_adapter
        //设置RecyclerView横向布局管理器
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_gift_shop_giftlist.setLayoutManager(linearLayoutManager)

        //设置兑换方式选中监听
        rg_gift_shop_detail_payway.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                rb_gift_shop_detail_wawa.id->{
                    btn_gift_shop_exchange.text="兑换"
                    getExchangeList()
                }
                rb_gift_shop_detail_cash.id->{
                    if (goodsprice.toDouble()<=0){
                        rg_gift_shop_detail_payway.clearCheck()
                        SToast("该娃娃暂不支持现金支付！")
                    }
                }
            }
        }
        //获取用户娃娃数
        getExchangeNum()

        //获取娃娃详情接口
        getGoodDetail();
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.rl_gift_shop_sumcount->{
                //获取用户可兑换礼品
                getExchangeList()
            }
            R.id.btn_gift_shop_exchange->{
                if (rg_gift_shop_detail_payway.checkedRadioButtonId==-1){
                    SToast("请选择一个兑换方式!")
                    return
                }
                when(rg_gift_shop_detail_payway.checkedRadioButtonId){
                    rb_gift_shop_detail_wawa.id->{
                        if (has_check_adapter!!.list.size<exchangenum.toInt()){
                            SToast("选择的娃娃数量不满足兑换条件！")
                        }else{
                        //确认用娃娃兑换礼品
                        conFirmExchange()
                        }
                    }
                    rb_gift_shop_detail_cash.id->{
                        if (goodsprice.toDouble()<=0){
                            SToast("该娃娃暂不支持现金支付！")
                        }else if(TextUtils.isEmpty(billnum)){
                            btn_gift_shop_exchange.text="完成兑换"
                            //使用现金兑换
                            createExchangeOrder()
                        }
                        else{
                            //判断是否完成支付
                            checkExchangeIsPay()
                        }
                    }
                }

            }
        }
    }

    override fun getMessage(bundle: Bundle) {
        var allresult = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        var result=bundle.getString("msg")
        when(bundle.getInt("what")){
        //获取礼品详情
            GETGOODDETAIL->{
                if (allresult!!.getAsJsonObject("Data").has("content")) {
                    var content = allresult!!.getAsJsonObject("Data").get("content").asString
                    //初始化webview
                    initWebView(content)
                }
            }
            //获取可兑换礼品接口
            GETEXCHANGELIST->{
                var rjson=GsonUtil.jsonToList(result,WindowGiftShopDetail::class.java) as ArrayList<WindowGiftShopDetail>
                var selDialog= GIftShopDetailDialog(this).create()
                        .sendData(rjson,has_check_adapter!!.list)
                        .setConfirmListener {
                            glist->
                            has_check_adapter!!.list=glist
                            has_check_adapter!!.notifyDataSetChanged()
                            tv_gift_shop_sumcount.text="("+glist.size+"/"+exchangenum+")"
                        }
                        .show()
            }

        //获取用户可兑换公仔数接口
            GETEXCHANGENUM->{
                tv_gift_shop_detail_usergift_count.text=allresult!!.get("exchangenum").asString

            }
            //兑换成功
            CONFIRMEXCHANGE->{
                var tisDialog=TisDialog(this).create()
                        .setNegativeButton { finish() }
                        .setPositiveButton {
                            startActivity(Intent(this,UserGifActivity::class.java))
                            finish()
                        }
                        .setMessage("兑换成功,是否立即发货？")
                        .show()
            }
            //app创建商城物品支付订单接口
            CREATEEXCHANGEORDER->{
                Log.e("CREATEEXCHANGEORDER",allresult!!.get("payurl").asString)
                openWebView(allresult!!.get("payurl").asString)
                billnum=allresult!!.get("billnum").asString

            }
            //确认兑换娃娃是否支付接口
            CHECKEXCHANGEISPAY->{
                var tisDialog=TisDialog(this).create()
                        .setNegativeButton { finish() }
                        .setPositiveButton {
                            startActivity(Intent(this,UserGifActivity::class.java))
                            finish()
                        }
                        .setMessage("兑换成功,是否立即发货？")
                        .show()
            }
        }
    }

    override fun backfinish(view: View) {
        super.backfinish(view)
    }

    //打开微信支付
    fun openWebView(callbackurl: String) {
        var webview: WebView? = WebView(this)
        webview!!.settings.javaScriptEnabled = true;
        webview!!.setWebViewClient(object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url!!.startsWith("weixin://wap/pay?")) {
                    var intent = Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        })
        webview!!.loadUrl(callbackurl)

    }

    //获取礼品详细信息
    fun getGoodDetail() {
        var param = HashMap<String, String>();
        param.put("goodsid", goodsid);
        param.put("sn", Constant.SN);
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getgoodsdetail, param, GETGOODDETAIL)
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


    //获取可兑换礼品接口
    fun getExchangeList() {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("userid", getLoginid());
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getexchangelist, param, GETEXCHANGELIST)
    }


    //确认兑换娃娃接口
    fun conFirmExchange() {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("userid", getLoginid());
        param.put("goodsid", goodsid);
        param.put("id", id);
        var machine_coin_id_list=""
        for (obj in has_check_adapter!!.list){
            machine_coin_id_list+=","+obj.machine_coin_id
        }
        machine_coin_id_list=machine_coin_id_list.removePrefix(",")
        machine_coin_id_list="("+machine_coin_id_list+")"
        param.put("data", machine_coin_id_list);
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.confirmexchange, param, CONFIRMEXCHANGE)
    }



    //app创建商城物品支付订单接口
    fun createExchangeOrder() {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("userid", getLoginid());
        param.put("id", id);
        param.put("goodsid", goodsid);
        param.put("apptype", "Android")
        param.put("appname", Constant.apk_name)
        param.put("appid", Constant.package_name)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.createexchangeorder, param, CREATEEXCHANGEORDER)
    }

    //确认兑换娃娃是否支付接口
    fun checkExchangeIsPay() {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("userid", getLoginid());
        param.put("billnum", billnum);
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.checkexchangeispay, param, CHECKEXCHANGEISPAY)
    }


    override fun onSystemError(what: Int, errormsg: String, allresult: JsonObject) {
        when(what){
            //支付失败操作
            CHECKEXCHANGEISPAY->{
              var tisPayDialog=TisDialog(this).create()
                      .setMessage("支付未完成,是否继续完成支付?")
                      .setNegativeButton {  }
                      .setPositiveButton {
                          btn_gift_shop_exchange.text="完成兑换"
                          createExchangeOrder() }
                      .show()
            }
            else->{
                super.onSystemError(what, errormsg, allresult)
            }
        }
    }



    //初始化webview
    private fun initWebView(content:String) {
        var webSettings=wb_gift_shop_detail.settings
        //支持缩放，默认为true。
        webSettings .setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings .setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings .setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings .setDefaultTextEncodingName("UTF-8");
        //设置自动加载图片
        webSettings .setLoadsImagesAutomatically(true);
        webSettings.javaScriptEnabled=true
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        wb_gift_shop_detail.webViewClient= object : WebViewClient() {
            //点击网页中按钮时，在原页面打开
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return shouldOverrideUrlLoading(view,url)
            }

            //页面加载完成后执行
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }
        }

        //加载网页
        wb_gift_shop_detail.loadData(Html.fromHtml(content).toString(),"text/html; charset=UTF-8","utf-8")


    }

}
