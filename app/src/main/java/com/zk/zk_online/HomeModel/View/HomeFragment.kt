package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import com.zk.lpw.base.BaseFragment
import com.zk.lpw.config.Constant
import com.zk.zk_online.Adapter.DeviceAdapter
import com.zk.zk_online.Adapter.RoomAdapter
import com.zk.zk_online.HomeModel.Model.*
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.MD5Utils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.weight.*

/**
 * Created by mukun on 2017/11/5.
 */
class HomeFragment : BaseFragment() {

    var swip_layout:SwipeRefreshLayout?=null
    var gridView: HeaderGridView?=null
    var banner:Banner?=null;
    var adapter: RoomAdapter?=null
    var deviceadapter:DeviceAdapter?=null;
    var btn_fragment_home_mygift:TextView?=null
    var btn_fragment_home_xiaoxi:TextView?=null
    var btn_fragment_home_share:TextView?=null
    var btn_fragment_home_rankings:TextView?=null
    var iv_home_fragment_bottompic:ImageView?=null
    var vtv_item_banner_tis:VerticalTextview?=null

    var datalist=ArrayList<Room>();
    var devicelist=ArrayList<DeviceBean>();

    //广告图返回列表
    var bannersDataList=ArrayList<BannerBean>()
    //banner下载前的展示图
    var tempImages=java.util.ArrayList<Int>();
    //房间请求标识
    val ROOM_MES=0;
    //banner标识
    val BANNER_MES=1;
    //机台请求标识
    val DEVICES_MES=2;
    //请求得到点击房间人数标识
    val DEVICE_NUMBER=3;
    //房间状态标识
    val ROOM_STATUS=4
    //查询充值套餐
    val GETPACKAGES=5
    //创建h5支付订单
    val CREATE_ORDER = 6
    //获取用户信息(充值时调用)
    val GETUSERCOIN = 7
    //获取首页中奖名单接口
    val GETPRIZELIST=8
    //是否第一次请求
    var firstrequest=true;
    var intent:Intent?=null

    //获取用户币数
    var userCoin=""

    override fun getMessage(bundle: Bundle) {
        var result=bundle.getString("msg")
        var rjson = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        when(bundle.getInt("what"))
        {
            ROOM_MES ->
            {
                var temp=GsonUtil.jsonToList(result,Room::class.java) as ArrayList<Room>
                datalist.clear();
                datalist.addAll(temp)
                adapter!!.notifyDataSetChanged()
                firstrequest=false
                if (swip_layout!!.isRefreshing)
                {
                    swip_layout!!.isRefreshing=false
                }
            }
            BANNER_MES ->{
                bannersDataList=GsonUtil.jsonToList(result,BannerBean::class.java) as ArrayList<BannerBean>
                var images=java.util.ArrayList<String>();
                for (item in bannersDataList)
                {
                    images.add(item.pic)
                }
                if(images.isNotEmpty()&&images.size!=0){
                banner!!.update(images);
                }
            }
            DEVICES_MES ->{
                var temp=GsonUtil.jsonToList(result,DeviceBean::class.java) as ArrayList<DeviceBean>
                devicelist.clear();
                devicelist.addAll(temp)
                deviceadapter!!.notifyDataSetChanged()
                firstrequest=false
                if (swip_layout!!.isRefreshing)
                {
                    swip_layout!!.isRefreshing=false
                }
            }
            DEVICE_NUMBER ->
            {

            }
            ROOM_STATUS ->{
                startActivity(intent)
            }
        //获取充值套餐
            GETPACKAGES -> {
                var rjson = GsonUtil.jsonToList(result, ChargeMoney::class.java) as ArrayList<ChargeMoney>
                var chargeMoneyDialog = ChargeMoneyDialog(context).create()
                        .sendData(rjson,userCoin)
                        .setItemlistener { item ->
                            createH5order(item.id)
                        }
                        .show()
                //还原币数
                userCoin=""
            }
        //支付订单
            CREATE_ORDER -> {

                openWebView(rjson!!.get("payurl").asString)
            }

        //用户信息(充值时调用)
            GETUSERCOIN -> {
                var userinfo: UserInfo =GsonUtil.jsonToObject(result, UserInfo::class.java) as UserInfo
                userCoin=userinfo.coins.toString()
            }
            //获取用户中奖信息
            GETPRIZELIST->{
                var rjson=GsonUtil.jsonToObject(result, JsonArray::class.java)
                var list=ArrayList<String>()
                rjson!!.mapTo(list) { it.asJsonObject.get("username").asString+"夹到了一个"+ "<font color='#F38012'>"+it.asJsonObject.get("goodsname").asString +"<font/>"}
//                Log.e("asdasda",list.toString())
               if (!list.isEmpty()) {
                   vtv_item_banner_tis!!.setTextList(list)
                   vtv_item_banner_tis!!.setAnimTime(1000)
                   vtv_item_banner_tis!!.setTextStillTime(5000)
                   vtv_item_banner_tis!!.startAutoScroll()
               }
            }

        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun init() {
        iv_home_fragment_bottompic=baseView!!.findViewById(R.id.iv_home_fragment_bottompic)
        btn_fragment_home_mygift= baseView!!.findViewById(R.id.btn_fragment_home_mygift)
        btn_fragment_home_share= baseView!!.findViewById(R.id.btn_fragment_home_share)
        btn_fragment_home_xiaoxi=baseView!!.findViewById(R.id.btn_fragment_home_xiaoxi)
        btn_fragment_home_rankings=baseView!!.findViewById(R.id.btn_fragment_home_rankings)
        btn_fragment_home_mygift!!.text="我的\n礼品"
        btn_fragment_home_share!!.text="推广\n分享"
        btn_fragment_home_xiaoxi!!.text="礼品\n商城"
        btn_fragment_home_rankings!!.text="抓取\n排行"
        iv_home_fragment_bottompic!!.setOnClickListener(this)
        btn_fragment_home_mygift!!.setOnClickListener(this)
        btn_fragment_home_share!!.setOnClickListener(this)
        btn_fragment_home_xiaoxi!!.setOnClickListener(this)
        btn_fragment_home_rankings!!.setOnClickListener(this)
        iv_home_fragment_bottompic!!.setOnLongClickListener (object:View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                startActivity(Intent(activity,ScanCodeActivity::class.java))
                return false
            }
        })
        gridView = baseView!!.findViewById(R.id.GridView)
        swip_layout=baseView!!.findViewById(R.id.swip_layout)
        var bannerHeaderView=View.inflate(context,R.layout.item_banner,null)
        banner=bannerHeaderView!!.findViewById(R.id.item_banner)
        vtv_item_banner_tis=bannerHeaderView!!.findViewById(R.id.vtv_item_banner_tis)
        gridView!!.addHeaderView(bannerHeaderView)

        //banner默认图片
        tempImages.add(R.drawable.default_img)
        tempImages.add(R.drawable.default_img)
        tempImages.add(R.drawable.default_img)
        //banner设置
        banner!!.setDelayTime(3000);//设置轮播时间
        banner!!.setImageLoader(GlideImageLoader());
        banner!!.setImages(tempImages).start()
        banner!!.setOnBannerListener(OnBannerListener{
            position ->
            if (bannersDataList[position].is_skip=="1"){
                var it=Intent(context,BannerInfoActivity::class.java)
                it.putExtra("banner_content",bannersDataList[position].banner_content)
                startActivity(it)
                }
        })
        swip_layout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener
        {
            setDialogShow(false)
            requestDevices()
        })
        setEmptyGridView()
//        if (firstrequest)
//        {
//            requestDevices()
//
//        }
        requestBanner()

        getPrizeList()
    }

    fun setData() {

        deviceadapter= DeviceAdapter(activity,devicelist);
        //进入房间先检测房间状态是不是可以进去
        gridView!!.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, p, l ->
            Log.i("position",p.toString());
            var i=p-2
            if (i>=0){
            if (!devicelist[i].is_offline)
            {
                intent=Intent(context,PlayActivity::class.java);
                intent!!.putExtra("deviceid",devicelist[i].machinecode)
                intent!!.putExtra("ip",devicelist[i].ip)
                intent!!.putExtra("roomid",devicelist[i].roomid);
                intent!!.putExtra("machineid",devicelist[i].machineid)
                intent!!.putExtra("price",devicelist[i].price_coin)
                intent!!.putExtra("machinename",devicelist[i].machinename)
                intent!!.putExtra("goodsid",devicelist[i].goodsid)
                getRoomStatus(devicelist[i].machinecode,devicelist[i].roomid)
//                startActivity(intent)

            }
            else{
                SToast("亲，房间正在整理中...")
            }

            }

        })
        deviceadapter!!.setWaWaDetailClickListener(object:DeviceAdapter.WaWaDetailClickListener{
            override fun click(v: View,i:Int) {
                if (!devicelist[i].is_offline) {
                    intent = Intent(context, WaWaDetailActivity::class.java);
                    intent!!.putExtra("deviceid", devicelist[i].machinecode)
                    intent!!.putExtra("ip", devicelist[i].ip)
                    intent!!.putExtra("roomid", devicelist[i].roomid);
                    intent!!.putExtra("machineid", devicelist[i].machineid)
                    intent!!.putExtra("price", devicelist[i].price_coin)
                    intent!!.putExtra("machinename", devicelist[i].machinename)
                    intent!!.putExtra("goodsid", devicelist[i].goodsid)
                    getRoomStatus(devicelist[i].machinecode, devicelist[i].roomid)
                }
            }
        })
        gridView!!.adapter=deviceadapter;


    }

    //请求获得房间列表
    fun requestRoom()
    {
        var param=HashMap<String,String>();
        var sign="loginid="+getUsercode()+"&"+"sn="+Constant.SN+"&"+Constant.KEY;
        param.put("sn",Constant.SN);
        param.put("loginid",getUsercode())
        param.put("sign",MD5Utils.getPwd(sign).toLowerCase());
        httpPost(Constant.SERVERIP+Constant.getroomlist,param,ROOM_MES)
    }

    //请求获得所有机台
    fun requestDevices(){
        var param=HashMap<String,String>();
        param.put("sn",Constant.SN)
        param.put("loginid",getUsercode())
        param.put("isnew","0")
        param.put("sign",SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP+Constant.getroombyall,param,DEVICES_MES)
    }


    //请求banner广告图
    fun requestBanner(){
        var param=HashMap<String,String>();
        var sign="sn="+Constant.SN+"&userid="+getLoginid()+"&"+Constant.KEY;
        param.put("sn",Constant.SN);
        param.put("userid",getLoginid());
        param.put("sign",MD5Utils.getPwd(sign).toString())
        setDialogShow(false)
        httpPost(Constant.SERVERIP+Constant.getbannber,param,BANNER_MES)
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

    override fun onClick(v: View) {
        when(v!!.id){
           R.id.btn_fragment_home_share->{
               startActivity(Intent(activity,UserInviteCodeActivity::class.java))
           }
            R.id.btn_fragment_home_mygift->{
                startActivity(Intent(activity,UserGifActivity::class.java))
            }
            R.id.btn_fragment_home_xiaoxi->{
                //跳转到礼品商城
                startActivity(Intent(activity,GiftShopActivity::class.java))
            }
            R.id.btn_fragment_home_rankings->{
                startActivity(Intent(activity,RankingsActivity::class.java))
            }
            R.id.iv_home_fragment_bottompic->{

            }
        }
    }

    final override fun onHttpError()
    {
        super.onHttpError();
        if (swip_layout!!.isRefreshing)
        {
            swip_layout!!.isRefreshing=false
        }
    }

    final override fun onSystemError(what: Int, errormsg: String,allresult:JsonObject) {
        when(what){
            ROOM_STATUS->{
                if (allresult.get("errorcode").asString=="-2"){
                    var dialog=TisDialog(context).create().setMessage(errormsg)
                            .setNegativeButton {  }
                            .setPositiveButton { view->
                                getUserInfoCoin()
                                getPackages()
                            }.show()
                }
                else{
                    super.onSystemError(what, errormsg,allresult)
                }
            }
            else->{
                super.onSystemError(what, errormsg,allresult)
            }
        }

    }

    fun setEmptyGridView(){
        setData()
    }

    //查询充值套餐接口
    fun getPackages() {
        var param = HashMap<String, String>()
        param.put("sn", Constant.SN)
        param.put("loginid", getUsercode())
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getpackages, param, GETPACKAGES)
    }

    //请求创建h5支付订单
    fun createH5order(goodsid: String) {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("userid", getLoginid());
        param.put("goodsid", goodsid);
        param.put("apptype", "Android")
        param.put("appname", Constant.apk_name)
        param.put("appid", Constant.package_name)
        param.put("sign", SignParamUtil.getSignStr(param));
        httpPost(Constant.SERVERIP + Constant.createH5order, param, CREATE_ORDER)
    }


    //打开h5支付
    fun openWebView(callbackurl: String) {
        var webview: WebView? = WebView(context)
        webview!!.settings.javaScriptEnabled = true;
        webview!!.setWebViewClient(object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                Log.i("go_pay", "go_pay")
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
        /*
         外置浏览器打开
         Log.i("callbackurl",callbackurl);
         var  intent = Intent();
         intent.setAction("android.intent.action.VIEW");
         var content_url= Uri.parse(callbackurl);
         intent.setData(content_url);
         startActivity(intent);*/
    }

    override fun onResume() {
        super.onResume()
        requestDevices()
    }

    //请求用户信息(资产)
    fun getUserInfoCoin() {
        var param = java.util.HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("loginid", getUsercode());
        param.put("sign", SignParamUtil.getSignStr(param))
        setDialogShow(false)
        httpPost(Constant.SERVERIP + Constant.getuserinfo, param, GETUSERCOIN)
    }

    //获取首页中奖名单接口
    fun getPrizeList(){
        var param=HashMap<String,String>();
        param.put("sn",Constant.SN);
        param.put("userid",getLoginid());
        param.put("sign",SignParamUtil.getSignStr(param))
        setDialogShow(false)
        httpPost(Constant.SERVERIP+Constant.getprizelist,param,GETPRIZELIST)
    }
}