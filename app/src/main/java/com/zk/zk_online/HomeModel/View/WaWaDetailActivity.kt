package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_wa_wa_detail.*
import java.util.*

class WaWaDetailActivity : BaseActivity() {

    //获取礼品详细信息
    val GETGOODDETAIL=1
    //请求房间状态
    val ROOM_STATUS=2

    var deviceid=""
    var ip=""
    var roomid=""
    var machineid=""
    var price=""
    var machinename=""
    var goodsid=""

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_wa_wa_detail)
        setTitle("宝贝详情")

        //获取上个页面传递的参数 或者sp信息
        deviceid = intent.getStringExtra("deviceid");
        ip = intent.getStringExtra("ip");
        roomid = intent.getStringExtra("roomid");
        machineid = intent.getStringExtra("machineid");
        price = intent.getStringExtra("price")
        machinename = intent.getStringExtra("machinename")
        goodsid = intent.getStringExtra("goodsid");


        getGoodDetail()


    }

    //初始化webview
    private fun initWebView(content:String) {
        var webSettings=wb_wa_wa_detail_content.settings
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

        wb_wa_wa_detail_content.webViewClient= object : WebViewClient() {
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
        wb_wa_wa_detail_content.loadData(Html.fromHtml(content).toString(),"text/html; charset=UTF-8","utf-8")


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            //跳转到游戏界面
            R.id.btn_wa_wa_detail_play->{
                getRoomStatus(deviceid,roomid)

            }
        }
    }

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        var rjson = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        when(bundle.getInt("what")){
            ROOM_STATUS ->{
                var intent = Intent(this, PlayActivity::class.java);
                intent!!.putExtra("deviceid", deviceid)
                intent!!.putExtra("ip", ip)
                intent!!.putExtra("roomid", roomid);
                intent!!.putExtra("machineid", machineid)
                intent!!.putExtra("price", price)
                intent!!.putExtra("machinename", machinename)
                intent!!.putExtra("goodsid", goodsid)
                startActivity(intent)
                finish()
            }
            //获取礼品详情
            GETGOODDETAIL->{
                if (rjson!!.getAsJsonObject("Data").has("content")) {
                    var content = rjson!!.getAsJsonObject("Data").get("content").asString
                    //初始化webview
                    Log.e("contet",content)
                    initWebView(content)
                }
            }
        }

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

    //请求获得房间状态
    fun getRoomStatus(machinecode:String,roomid:String){
        var param=HashMap<String,String>();
        param.put("sn",Constant.SN);
        param.put("machinecode",machinecode)
        param.put("roomid",roomid)
        param.put("userid",getLoginid())
        param.put("sign",SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP+Constant.checkroomstatus,param,ROOM_STATUS)

    }

}
