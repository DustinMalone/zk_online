package com.zk.zk_online.HomeModel.View


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ohmerhe.kolley.request.Http
import com.orhanobut.logger.Logger
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMWeb
import com.zk.lpw.config.Constant
import com.zk.lpw.utils.SharedPreferencesUtil
import com.zk.zk_online.Adapter.MyGifAdapter
import com.zk.zk_online.HomeModel.Adapter.DanMaKuAdapter
import com.zk.zk_online.HomeModel.Adapter.PlayPicAdapter
import com.zk.zk_online.HomeModel.Model.CatchRankBean
import com.zk.zk_online.HomeModel.Model.ChargeMoney
import com.zk.zk_online.HomeModel.Model.RoomUserInfo
import com.zk.zk_online.HomeModel.Model.UserInfo
import com.zk.zk_online.R
import com.zk.zk_online.Utils.*
import com.zk.zk_online.base.BaseActivity
import com.zk.zk_online.weight.*
import io.agora.AgoraAPI
import io.agora.AgoraAPIOnlySignal
import io.agora.IAgoraAPI
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import kotlinx.android.synthetic.main.activity_play.*
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.ui.widget.DanmakuView
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.nio.charset.Charset
import java.util.*


class PlayActivity : BaseActivity(), MyImageView.OnImageViewClickListener {


    //显示视频fraglayout View
    var video_content: FrameLayout? = null
    //视频和弹幕framelayout
    var frame_p: FrameLayout? = null
    //控制遥感View
    var linear_play: LinearLayout? = null
    //预约View
    var linear_order: LinearLayout? = null
    //开始View
    var linear_start: LinearLayout? = null
    //正在游戏中用户View
    var linear_current_user: LinearLayout? = null;
    //礼品详情View
    var linear_gooddetail: LinearLayout? = null

    var myVerticalScrollLayout: MyVerticalScrollLayout? = null
    var webview: WebView? = null;


    var  mediaPlayer=MediaPlayer();
//    var soundpool: SoundPool? = null;
    var streamid: Int = 0


    var lv_1: MyListView? = null
    //弹幕View
    var danmaku: DanmakuView? = null
    var dContext: DanmakuContext? = null
    var showDanmaku: Boolean? = true

    //弹幕View列表
    var lv_danmaku: ListView? = null
    var danmaListData = ArrayList<String>()
    var danmaListAdapter: DanMaKuAdapter? = null

    var img_feed_back: ImageView? = null

    var img_good: ImageView? = null
    var img_play_up: MyImageView? = null;
    var img_play_down: MyImageView? = null;
    var img_play_left: MyImageView? = null;
    var img_play_right: MyImageView? = null;
    var img_play_commit: MyImageView? = null;
    var img_change_camera: ImageView? = null
    var circle_img_user: circleImageView? = null
    var img_loading: GifImageView? = null
    var img_wait_result: ImageView? = null
    //    var iv_play_rbpic: ImageView? = null
    var rv_play_rbpic: RecyclerView? = null
    var playPicAdapter: PlayPicAdapter? = null
    var roomUserdatalist = ArrayList<RoomUserInfo>()
    var classjson: RoomUserInfo? = null

    var tv_timmer: TextView? = null
    var tv_room_persons: TextView? = null
    var tv_price_info: TextView? = null
    var tv_barrage: TextView? = null
    var tv_user_name: TextView? = null
    var tv_goodname: TextView? = null
    var tv_catch_rank: TextView? = null;
    var tv_good_detail: TextView? = null;
    var iv_play_close_music: ImageView? = null
    var img_right: ImageView? = null

    //加载框 gifdrawable
    var loading_gifdrawable: GifDrawable? = null


    //倒计时控件
    var img_countdown: CountDownView? = null


    //准备中 动画 dialog
    var reading_dialog:ReadingDialog?=null
    //抓取失败 dialog
    var pickfail_dialog:CatchFailedDialog?=null
    //抓取成功 dialog
    var picksuccess_dialog:CatSuccessDialog?=null;

    var adaper: MyGifAdapter? = null;

    var rtcEngine: RtcEngine? = null
    var agoraAPIOnlySignal: AgoraAPIOnlySignal? = null
    //频道秘钥
    var channelkey: String? = null
    //信令秘钥
    var signalkey: String? = null

    //用户编号/账号
    var account = "";
    //当前房间游戏用户id
    var gameUserid = "";
    //设备编码
    var deviceid: String? = null
    //设备id
    var machineid: String? = null
    //机台IP
    var ip: String? = null
    //房间id
    var roomid = ""
    //游戏价格
    var price = ""
    //礼品id
    var goodsid = "";
    //机台名称
    var machinename = ""
    //信令管理员
    val adminAccount = "admin"
    //礼品图像链接
    var goodspic = ""
    //礼品名称
    var goodsname = ""
    //切换摄像头指令
    val CHANGECAMEAR = "change_camera"
    //离开游戏 恢复摄像头指令
    val FINISHGAME = "finish_game";


    //倒计时
    var second = 30;
    //房间人数
    var persons = 0;
    //用户币数
    var coins = 0;


    //是否停止发送消息
    var stophandler = false;
    //是否正在抓取 如果正在抓取，则不可以多次发出 查询抓取结果的请求
    var catchable = true;
    //是否当前用户锁住房间
    var iscurLock = false;
    //channelkey是否过期
    var isexpire = false;
    //弹幕是否初始化完毕
    var isDanmuOk = false;
    //是否可以点击开始游戏
    var startable = true;
    //当前用户是否在玩
    var curuserplay = false;
    //是否可以按返回键(下爪后不可按返回键)
    var backeventadble = true;

    //向下指令
    val ACTION_DOWN = "04"
    //向上指令
    val ACTION_UP = "03"
    //向左指令
    val ACTION_LEFT = "01"
    //向右指令
    val ACTION_RIGHT = "02"
    //确认指令
    val ACTION_PICK = "05"
    //松开指令
    val ACTION_CANCLE = "00"


    //投币成功标识
    val PUT_CODE = 0;
    //进入或者离开房间标识
    val ROOM_CODE = 1;
    //解锁或者锁房间标识
    val LOCK_CODE = 10
    //获取秘钥成功标识
    val KEY_CODE = 11;
    //获取礼品信息成功
    val GIT_CODE = 12;
    //获取充值套餐接口
    val GETPACKAGES = 1001
    //获取抓取排行
    val CATCH_HISTORY = 1002
    //创建h5支付订单
    val CREATE_ORDER = 1003
    //查询房间内用户信息
    val ROOMUSERLIST = 1004
    //其他用户进入出去房间
    val INCOME = 1005
    val OUTCOME = 1006
    //查询机械故障反馈信息
    val BREAKINFOMACHINE = 1007
    //提交反馈信息
    val FEEDBACK = 1008
    //查询分享链接信息
    val GETSHARECODEURL = 1009
    //获取用户信息(主要是资产信息)
    val GETUSERCOIN = 1010
    //查询用戶是否可以分享邀請碼
    val GETSHARETIMES=1011
    //游戏下爪前中途退出房间
    val HALFWAYOUT=1012

    //背景音乐状态
    var music_status:Boolean?=null


    //倒计时标识
    val COUNT_DOWN = 2
    //抽中结果
    val CHECK_RESULT = 3;
    //弹幕初始化完毕
    var DANMU_OK = 4;


    //进入房间
    val ENTER_ROOM = 1;
    //退出房间
    val LEAVE_ROOM = 2;
    //进入或者退出 房间
    var ROOM_TYPE = 0;

    //锁定
    val LOCKing = 1
    //解锁
    val UNLOCK = 0

    var remoteVideoView: SurfaceView?=null

    var curryVidel:Int=Constant.MUID

    //网络状态管理
    lateinit var mTelephonyManager: TelephonyManager
    lateinit var mListener: PhoneStatListener

    internal var mhandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            when (msg!!.what) {

            //得到检查结果
                CHECK_RESULT -> {
                    var result = msg!!.obj as Boolean
                    //得到结果后 设为观众模式 正在抓取设为true
                    catchable = true
                    backeventadble = true
                    curuserplay = false
                    rtcEngine!!.setClientRole(Constants.CLIENT_ROLE_AUDIENCE, null)

                    if (result) {
                        // 抽中后先获得礼品信息 再显示对话框
                        pickSuccess(goodspic)
                    } else {
                        //抽不中操作
                        pickFailed()
                    }
                }
            }

        }
    }

    var parser: BaseDanmakuParser = object : BaseDanmakuParser() {
        override fun parse(): IDanmakus {
            return Danmakus();
        }
    };

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_play)
        //获取上个页面传递的参数 或者sp信息
        deviceid = intent.getStringExtra("deviceid");
        ip = intent.getStringExtra("ip");
        roomid = intent.getStringExtra("roomid");
        machineid = intent.getStringExtra("machineid");
        price = intent.getStringExtra("price")
        machinename = intent.getStringExtra("machinename")
        goodsid = intent.getStringExtra("goodsid");

        loading_gifdrawable = GifDrawable(resources, R.drawable.loading)


        music_status=SharedPreferencesUtil.getBoolean(this,Constant.MUSICSTATUS,true);
        coins = getUserCoins().toInt();
        account = getUsercode();
        //初始化信令
        agoraAPIOnlySignal = AgoraAPI.getInstance(this, Constant.APPID)
        //设置回调
        agoraAPIOnlySignal!!.callbackSet(signCallback);
        setTitle(machinename)
        initView()
        initDialog()
        initVideo();
        initDanmaku()
        initWIFI()
        getChannelKey()
        CatchData()
        getGoodDetail()
        getUserInfoCoin()

    }

    //监听WIFI信号
    private fun initWIFI() {
        mTelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager;
        //开始监听
        mListener = PhoneStatListener(this, img_right!!);
        //监听信号强度
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    //请求回调
    override fun getMessage(bundle: Bundle) {
        var result = bundle.getString("msg")
        var rjson = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        when (bundle.getInt("what")) {
        //投币成功 开始游戏
            PUT_CODE -> {
                iscurLock = true
                coins = coins - price.toInt();
//                tv_price_info!!.text = "余额：" + coins + " | " + price + "币"
                tv_price_info!!.text = "余额: " + coins
                SharedPreferencesUtil.putString(this, Constant.USERCOINS, coins.toString());
                curuserplay = true;
                startGame()
            }
        //进入或者离开房间 成功
            ROOM_CODE -> {

                if (ROOM_TYPE == ENTER_ROOM) {
                    var lock = rjson!!.get("lock").toString()
                    if (!lock.equals("0")) {
                        changeViewByLock(lock, rjson.get("username").asString, rjson.get("headurl").asString, rjson.get("userid").asString)
                    } else changeViewByLock(lock, "", "", "");
                }
            }
        //获取秘钥成功
            KEY_CODE -> {
                Log.i("channelkey", rjson!!.get("signkey").asString);
                channelkey = rjson!!.get("signkey").asString;
                if (!isexpire) {
                    signalkey = TokenUtil.getToken(Constant.APPID, Constant.AppCertificate, getLoginid() + "")
                    agoraAPIOnlySignal!!.login2(Constant.APPID, getLoginid() + "", signalkey, 0, null, 30, 3);
                    agoraAPIOnlySignal!!.channelJoin(deviceid!!);
                    playVideo()
                } else {
                    rtcEngine!!.renewChannelKey(channelkey);
                }
            }
        //获取礼品详情
            GIT_CODE -> {
                goodspic = rjson!!.getAsJsonObject("Data").get("pic").asString
                goodsname = rjson!!.getAsJsonObject("Data").get("goodsname").asString
                if (this.isFinishing) {
                    //aaa
                    Glide.with(applicationContext).load(goodspic).placeholder(R.drawable.default_img).into(img_good)
                }
                tv_goodname!!.text = goodsname
            }
        //获取充值套餐
            GETPACKAGES -> {
                var rjson = GsonUtil.jsonToList(result, ChargeMoney::class.java) as ArrayList<ChargeMoney>
                var chargeMoneyDialog = ChargeMoneyDialog(this).create()
                        .sendData(rjson,coins.toString())
                        .setItemlistener { item ->
                            createH5order(item.id)
                        }
                        .show()
            }
        //抓取排行榜
            CATCH_HISTORY -> {
                var catchList = GsonUtil.jsonToList(result, CatchRankBean::class.java) as ArrayList<CatchRankBean>
                adaper = MyGifAdapter(this, catchList)
                lv_1!!.adapter = adaper

            }
        //支付订单
            CREATE_ORDER -> {

                openWebView(rjson!!.get("payurl").asString)
            }
        //查询房间内用户信息
            ROOMUSERLIST -> {
                roomUserdatalist = GsonUtil.jsonToList(result, RoomUserInfo::class.java) as ArrayList<RoomUserInfo>
                playPicAdapter = PlayPicAdapter(applicationContext, roomUserdatalist)
                rv_play_rbpic!!.adapter = playPicAdapter
            }
        //有其他用户进入房间
            INCOME -> {
                var json = GsonUtil.jsonToList(result, RoomUserInfo::class.java) as ArrayList<RoomUserInfo>
                roomUserdatalist.addAll(json)
                playPicAdapter!!.list = roomUserdatalist
                playPicAdapter!!.notifyDataSetChanged()
            }
        //有其他用户离开房间
            OUTCOME -> {
                var json = GsonUtil.jsonToList(result, RoomUserInfo::class.java) as ArrayList<RoomUserInfo>
//                Log.e("jsons",json[0].usercode)
                if (!json.isEmpty()) {
                    var i = 0
                    for (t in roomUserdatalist) {
                        if (t.id == json[0].id) {
                            roomUserdatalist.remove(t)
                            break
                        }
                        i++;
                    }
                    playPicAdapter!!.list = roomUserdatalist
                    playPicAdapter!!.notifyDataSetChanged()
                }
            }
            //报障数据
            BREAKINFOMACHINE -> {
                var json = result.replace("\"", "").split(",")
                var dialog = WindowFeedBackDialog(this).create()
                        .sendData(json as java.util.ArrayList<String>)
                        .setItemlistener { itemOnclick ->
                            if(itemOnclick=="其他"){
                                var intent_advice=Intent(this,AdviceActivity::class.java)
                                intent_advice.putExtra("machineid",machineid)
                                intent_advice.putExtra("machinename",machinename)
                                startActivity(intent_advice)
                            }else{
                            feedBack(itemOnclick)
                            }
                        }
                        .show()
            }
            //报障成功
            FEEDBACK -> {
                var dialog = Dialog(this)
                var img = ImageView(this)
                img.setImageResource(R.drawable.feedback_sueecss)
                dialog.setContentView(img)
                var params = dialog.window!!.attributes
                params.gravity = Gravity.CENTER
                params.width = WindowManager.LayoutParams.WRAP_CONTENT
                dialog.window!!.attributes = params
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
            //分享链接
            GETSHARECODEURL -> {
                umengShareInit(rjson!!.get("Data").asString)
            }
            //查询用户是否可以分享分享码
            GETSHARETIMES->{
                var dialog = MoneyNotEnoughDialog(this).create()
                        .setISShare(rjson!!.get("isshare").asBoolean)
                       .setChongzhiListener(object:MoneyNotEnoughDialog.ChongzhiListener{
                           override fun onClick(v: View?) {
                               getPackages()
                           }
                       }).show()
            }
            //用户信息资产
            GETUSERCOIN ->
            {
                var userinfo: UserInfo =GsonUtil.jsonToObject(result, UserInfo::class.java) as UserInfo
                coins=userinfo.coins
                tv_price_info!!.text= "余额: "+ userinfo.coins
            }
            //游戏中途退出房间
            HALFWAYOUT ->
            {

            }


        }
    }

    //初始化绑定View
    fun initView() {

        //重设属性
//        var lp=fl_view.layoutParams
//        lp.width=ScreenUtils.getScreenWidth(this)
//        lp.height= (ScreenUtils.getScreenWidth(this)*1.2).toInt()
//        fl_view.layoutParams=lp

        lv_1 = findViewById(R.id.lv_1)
        img_play_up = findViewById(R.id.img_play_up)
        img_play_down = findViewById(R.id.img_play_down)
        img_play_left = findViewById(R.id.img_play_left)
        img_play_right = findViewById(R.id.img_play_right)
        img_play_commit = findViewById(R.id.img_play_commit)
        img_change_camera = findViewById(R.id.img_change_camera);
        circle_img_user = findViewById(R.id.circle_img_user)
        img_wait_result = findViewById(R.id.img_wait_result)
        img_good = findViewById(R.id.img_good)
        iv_play_close_music = findViewById(R.id.iv_play_close_music)
        iv_play_close_music!!.isSelected = false
        img_right = findViewById(R.id.img_right)

        img_loading = findViewById(R.id.img_loading)
        img_loading!!.setImageDrawable(loading_gifdrawable)

        img_play_up!!.setOnImageViewClickListener(this)
        img_play_down!!.setOnImageViewClickListener(this)
        img_play_left!!.setOnImageViewClickListener(this)
        img_play_right!!.setOnImageViewClickListener(this)
        img_play_commit!!.setOnImageViewClickListener(this)
        iv_play_close_music!!.setOnClickListener(this)

        img_play_up!!.setCode(ACTION_UP);
        img_play_down!!.setCode(ACTION_DOWN);
        img_play_left!!.setCode(ACTION_LEFT);
        img_play_right!!.setCode(ACTION_RIGHT);
        img_play_commit!!.setCode(ACTION_PICK);

        tv_timmer = findViewById(R.id.tv_timmer)
        tv_room_persons = findViewById(R.id.tv_room_persons);
        tv_price_info = findViewById(R.id.tv_price_info);
        tv_barrage = findViewById(R.id.tv_barrage);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_goodname = findViewById(R.id.tv_goodname)
        tv_catch_rank = findViewById(R.id.tv_catch_rank)
        tv_good_detail = findViewById(R.id.tv_good_detail)

        video_content = findViewById(R.id.video_content)
        linear_order = findViewById(R.id.linear_order)
        linear_play = findViewById(R.id.linear_play)
        linear_start = findViewById(R.id.linear_start)
        linear_current_user = findViewById(R.id.linear_current_user);
        linear_gooddetail = findViewById(R.id.linear_gooddetail)
        frame_p = findViewById(R.id.frame_p)
        myVerticalScrollLayout = findViewById(R.id.MyVerticalScrollLayout);
        webview = findViewById(R.id.webview);

        //头像列表
        rv_play_rbpic = findViewById(R.id.rv_play_rbpic)
        playPicAdapter = PlayPicAdapter(applicationContext, roomUserdatalist)
        //设置布局管理器
        var linearLayoutManager = LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_play_rbpic!!.setLayoutManager(linearLayoutManager);
        rv_play_rbpic!!.adapter = playPicAdapter

        //倒计时控件初始化
        img_countdown = findViewById(R.id.img_countdown)
        img_countdown!!.setSeconds(second)
        img_countdown!!.setTimeOverListener(CountDownView.TimeOverlistener {
            //游戏30时间到
            img_countdown!!.stopcountdown(true)
            img_countdown!!.visibility = View.GONE
            if (catchable) {
                sendCommon(ACTION_PICK, deviceid.toString());
                catchable = false;
                backeventadble = false
                Handler().postDelayed(Runnable {
                    sendCommon(ACTION_CANCLE, deviceid.toString());
                    InterceptControll(true)
                    Thread(CheckRunnable()).start()
                    img_countdown!!.visibility = View.GONE
                    img_wait_result!!.visibility = View.VISIBLE
                }, 20);
            }
        });
        //弹幕列表
        lv_danmaku = findViewById(R.id.lv_danmaku);
        danmaListAdapter = DanMaKuAdapter(this, danmaListData)
        lv_danmaku!!.adapter = danmaListAdapter

        //排行榜listview监听上滑 通知父View上滑
        lv_1!!.setScrollIndexListener(MyListView.ScrollIndexListener {
            myVerticalScrollLayout!!.scrollerToFirstIndex();
        })
        //报障
        img_feed_back = findViewById(R.id.img_feed_back)

    }

    fun initDialog(){
        //准备中弹窗
        reading_dialog= ReadingDialog(this);

        //抓取失败弹窗
        pickfail_dialog=CatchFailedDialog(this)
                .setCatchFailCallback(object:CatchFailedDialog.CatchFailCallback{
                    override fun stopclick() {
                        stopGame()
                    }
                    override fun againclick() {
                        playAgain()
                    }
                    override fun timeout() {
                        stopGame()
                    }
                })
                .createDialog()
                .setCanclable(false)

        //抓取成功弹窗
        picksuccess_dialog=CatSuccessDialog(this)
                .setCatchSuccessCallback(object :CatSuccessDialog.CatchSuccessCallback{
                    override fun stopclick() {
                         stopGame()
                        //分享sdk
                        getShareCodeUrl()
                    }
                    override fun againclick() {
                        reading_dialog!!.show()
                        playAgain()
                    }
                    override fun timeout() {
                       stopGame()
                    }
                })
                .createDialog()
                .setCanclable(false)
    }

    //初始化视频
    fun initVideo() {
        rtcEngine = RtcEngine.create(this, Constant.APPID, rtchandler)
        rtcEngine!!.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        rtcEngine!!.disableAudio();
        rtcEngine!!.enableVideo();
        rtcEngine!!.muteLocalVideoStream(true);
        rtcEngine!!.muteLocalAudioStream(true);
  //        rtcEngine!!.setVideoProfile(Constants.VIDEO_PROFILE_360P_9, false)
        rtcEngine!!.setVideoProfile(Constants.VIDEO_PROFILE_480P_9, false)
        rtcEngine!!.setRemoteVideoStreamType(getLoginid().toInt(), Constants.VIDEO_STREAM_LOW);
        rtcEngine!!.muteAllRemoteAudioStreams(true);
        rtcEngine!!.setClientRole(Constants.CLIENT_ROLE_AUDIENCE, null)
    }

    //播放视频
    fun playVideo() {
        try {
             remoteVideoView = RtcEngine.CreateRendererView(this)
            rtcEngine!!.setupRemoteVideo(VideoCanvas(remoteVideoView, VideoCanvas.RENDER_MODE_HIDDEN, curryVidel))
            rtcEngine!!.joinChannel(channelkey, deviceid, null, getLoginid().toInt()
            );
            video_content!!.addView(remoteVideoView, 0)
            if (music_status!!) {
                playMusic()
            }
            enterOrLeave(ENTER_ROOM)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_order -> {
                //预约排队
            }
            R.id.bt_message -> {
                //发消息
            }
            R.id.img_message -> {
                //发消息
                var msg_dialog = MessageDialog(this)
                        .create()
                        .sendMessage(object : MessageDialog.OnsendMessageListener {
                            override fun sendMessage(msg: String?) {
                                agoraAPIOnlySignal!!.messageChannelSend(deviceid, getUsername() + " :" + msg, "")
                            }
                        }).show();

            }
            R.id.bt_pay -> {
                //充值
            }
            R.id.img_start -> {
                //开始投币
                if (startable) {
                    if(coins<price.toInt()){
                        getUserInfoCoin()
                    }
                    putCoinRequest(true);
                }
            }
            R.id.img_change_camera -> {
                //切换娃娃机的摄像头 发送点对点消息
//                agoraAPIOnlySignal!!.messageInstantSend(deviceid, 0, CHANGECAMEAR, "1");
               if(curryVidel==Constant.MUID){
                   curryVidel=Constant.MUID2
               }else{
                   curryVidel=Constant.MUID
               }
                rtcEngine!!.setupRemoteVideo(VideoCanvas(remoteVideoView, VideoCanvas.RENDER_MODE_HIDDEN,curryVidel))


            }
            R.id.tv_barrage -> {
                //关闭 打开弹幕
                if (showDanmaku!!) {
                    showDanmaku = false
                    danmaku!!.hide()
                    tv_barrage!!.text = "弹幕 : 关"
                    lv_danmaku!!.visibility = View.GONE
                } else {
                    showDanmaku = true
                    danmaku!!.show()
                    tv_barrage!!.text = "弹幕 : 开"
                    lv_danmaku!!.visibility = View.VISIBLE
                }
            }
        //请求充值查询套餐接口，弹出充值套餐窗口
            R.id.img_money -> {
                getPackages()
            }
        //故障反馈
            R.id.img_feed_back -> {
                getBreakInfoMachine()
            }
            R.id.tv_good_detail -> {
                tv_catch_rank!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tv_good_detail!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resources.getDrawable(R.drawable.wodelipin_d))
                lv_1!!.visibility = View.GONE
                linear_gooddetail!!.visibility = View.VISIBLE

            }
            R.id.tv_catch_rank -> {
                tv_catch_rank!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resources.getDrawable(R.drawable.wodelipin_d));
                tv_good_detail!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                linear_gooddetail!!.visibility = View.GONE
                lv_1!!.visibility = View.VISIBLE

            }
            R.id.iv_play_close_music -> {
                if (!iv_play_close_music!!.isSelected) {
                    iv_play_close_music!!.isSelected = true
                    if(music_status!!&&mediaPlayer!=null){
                        mediaPlayer!!.pause()
                    }

                } else {
                    iv_play_close_music!!.isSelected = false
                    if (music_status!!&&mediaPlayer!=null) {
                        mediaPlayer!!.start()
                    }

                }

            }
        }
    }

    override fun onPause() {
        super.onPause();
       /* if (soundpool!=null && iv_play_close_music!!.isSelected)
        {
            soundpool!!.pause(streamid)
        }*/
    }


    override fun clickDown(code: String?) {
        sendCommon(code!!, deviceid!!)
    }

    override fun clickUp(code: String?) {

        sendCommon(ACTION_CANCLE, deviceid!!);
        if (code.equals(ACTION_PICK)) {
            //防止多次发出多次轮询请求
            img_countdown!!.stopcountdown(true);
            img_countdown!!.visibility = View.GONE
            img_wait_result!!.visibility = View.VISIBLE
            if (catchable) {
                catchable = false
                backeventadble = false
                InterceptControll(true)
                Thread(CheckRunnable()).start()
                img_countdown!!.visibility = View.GONE
                img_wait_result!!.visibility = View.VISIBLE
            }
        }


    }

    //开始游戏 切换主播 以及倒计时
    fun startGame() {
        playTransTOBroad()
        reading_dialog!!.dismiss()
        InterceptControll(false);
        linear_start!!.visibility = View.GONE
        linear_play!!.visibility = View.VISIBLE;
        img_countdown!!.reset()
        img_countdown!!.visibility = View.VISIBLE
        img_countdown!!.startCountdown()
    }

    //上下左右方向键 事件拦截
    fun InterceptControll(i: Boolean) {
        img_play_down!!.setInterceptClick(i)
        img_play_up!!.setInterceptClick(i)
        img_play_left!!.setInterceptClick(i)
        img_play_right!!.setInterceptClick(i)

    }

    //切换成声网主播角色
    fun playTransTOBroad() {
        rtcEngine!!.setClientRole(Constants.CLIENT_ROLE_BROADCASTER, null);
        rtcEngine!!.enableDualStreamMode(true);
    }


    /**
     * 发送指令到服务器上
     * code  指令代码
     * device 设备代码
     */
    fun sendCommon(code: String, device: String) {
        var sing = "device=" + device + "&ip=" + ip + "&order=" + code!! + "&sn=" + Constant.SN + "&" + Constant.KEY;
        var jsonobject = JsonObject();
        jsonobject.addProperty("sn", Constant.SN);
        jsonobject.addProperty("order", code);
        jsonobject.addProperty("device", device);
        jsonobject.addProperty("ip", ip);
        jsonobject.addProperty("sign", MD5Utils.getPwd(sing).toLowerCase());
        HttpRequest.httpPost(jsonobject.toString(), Constant.command)
    }

    //初始化弹幕
    fun initDanmaku() {
        danmaku = findViewById(R.id.danmaku)
        danmaku!!.enableDanmakuDrawingCache(true)
        // 设置最大显示行数
        val maxLinesPair = HashMap<Int, Int>()
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 8) // 滚动弹幕最大显示5行

        danmaku!!.setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                isDanmuOk = true
                danmaku!!.start()
                var message = Message.obtain();
                message.what = DANMU_OK;
                mhandler.sendMessage(message)
            }

            override fun updateTimer(timer: DanmakuTimer) {

            }

            override fun danmakuShown(danmaku: BaseDanmaku) {

            }

            override fun drawingFinished() {

            }
        })

        dContext = DanmakuContext.create()
        dContext!!.setMaximumLines(maxLinesPair)
        danmaku!!.prepare(parser, dContext)
    }

    //投币请求
    fun putCoinRequest(showproable: Boolean) {
        var param = HashMap<String, String>();
        var sign = "machinecode=" + deviceid + "&roomid=" + roomid + "&sn=" + Constant.SN + "&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("sn", Constant.SN)
        param.put("userid", getLoginid());
        param.put("roomid", roomid);
        param.put("machinecode", deviceid!!);
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        setShowPro(showproable)
        httpPost(Constant.SERVERIP + Constant.start, param, PUT_CODE);
    }

    //请求获得channelkey
    fun getChannelKey() {
        var param = HashMap<String, String>();
        val sign = "machinecode=" + deviceid + "&sn=" + Constant.SN + "&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("userid", getLoginid() + "")
        param.put("machinecode", deviceid!!)
        param.put("sn", Constant.SN)
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP + Constant.GETCHANNEL, param, KEY_CODE);
    }

    //进入或者离开房间时 发出此请求
    fun enterOrLeave(type: Int) {
        ROOM_TYPE = type;
        var sign = "machineid=" + machineid + "&roomid=" + roomid + "&sn=" + Constant.SN + "&type=" + type + "&" + Constant.KEY;
        var param = HashMap<String, String>();
        param.put("type", type!!.toString());
        param.put("sn", Constant.SN);
        param.put("roomid", roomid);
        param.put("machineid", machineid!!);
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.ioroom, param, ROOM_CODE);

    }

    //获取礼品详细信息
    fun getGoodDetail() {
        var param = HashMap<String, String>();
        param.put("goodsid", goodsid);
        param.put("sn", Constant.SN);
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getgoodsdetail, param, GIT_CODE)
    }

    //锁住房间或者解锁
    fun lockOrUnlockRoom(type: Int) {
        var sign = "machineid=" + machineid + "&roomid=" + roomid + "&sn=" + Constant.SN + "&type=" + type + "&" + Constant.KEY;
        var param = HashMap<String, String>();
        param.put("type", type.toString());
        param.put("sn", Constant.SN);
        param.put("roomid", roomid);
        param.put("machineid", machineid!!);
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP + Constant.lockroom, param, LOCK_CODE);
    }

    //查询充值套餐接口
    fun getPackages() {
        var param = HashMap<String, String>()
        param.put("sn", Constant.SN)
        param.put("loginid", getUsercode())
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getpackages, param, GETPACKAGES)
    }

    //请求抓取记录
    fun CatchData() {
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("machineid", machineid + "")
        param.put("sn", Constant.SN)
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.roomranklist, param, CATCH_HISTORY);
    }

    //请求用户信息(资产)
    fun getUserInfoCoin() {
        var param = HashMap<String, String>();
        param.put("sn", Constant.SN);
        param.put("loginid", getUsercode());
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getuserinfo, param, GETUSERCOIN)
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
    //游戏下爪前中途退出房间 调用此接口
    fun halfwayout(){
        var param=HashMap<String,String>();
        param.put("sn",Constant.SN);
        param.put("machinecode",deviceid.toString())
        param.put("machineid",machineid.toString())
        param.put("roomid",roomid)
        param.put("userid",getLoginid())
        param.put("sign",SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP+Constant.halfwayout,param,HALFWAYOUT)
    }

    //抓取失败
    fun pickFailed() {
        img_countdown!!.stopcountdown(true)
        img_countdown!!.visibility = View.GONE
        img_wait_result!!.visibility = View.GONE
        pickfail_dialog!!.show()
        second = -1
        tv_timmer!!.text = ""
        linear_play!!.visibility = View.GONE
        linear_start!!.visibility = View.VISIBLE

    }

    //抓取成功
    fun pickSuccess(path: String) {
        img_countdown!!.stopcountdown(true)
        img_countdown!!.visibility = View.GONE
        img_wait_result!!.visibility = View.GONE
         picksuccess_dialog!!
                 .setImagePath(path)
                .show()
        second = -1
        tv_timmer!!.text = ""
        linear_play!!.visibility = View.GONE
        linear_start!!.visibility = View.VISIBLE
    }


    //添加一条弹幕
    fun addDanmaku(msg: String) {
        val itemdanmu = dContext!!.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        if (itemdanmu == null) {
            return;
        }
        itemdanmu.text = msg
        itemdanmu.padding = 5
        itemdanmu.textSize = pxUtil.sp2px(15f, this).toFloat()
        itemdanmu.textColor = Color.WHITE
        itemdanmu.setTime(danmaku!!.getCurrentTime())
        danmaku!!.addDanmaku(itemdanmu)
    }

    //列表弹幕添加一条信息
    fun addDanmakuList(msg: String) {
        Log.e("dsad", "1")
        danmaListData.add(msg)
        danmaListAdapter!!.updateData(danmaListData)
        lv_danmaku!!.setSelection(danmaListAdapter!!.count)
    }


    override fun onResume() {
        super.onResume();
        if (danmaku != null && danmaku!!.isPrepared() && danmaku!!.isPaused()) {
            danmaku!!.resume();
        }
        /*if (soundpool!=null && !iv_play_close_music!!.isSelected)
        {
            soundpool!!.resume(streamid)
        }*/
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        if (mediaPlayer!=null&&!iv_play_close_music!!.isSelected){
            if (music_status!!) {
                mediaPlayer!!.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (danmaku != null && danmaku!!.isPrepared()) {
            danmaku!!.pause();
        }
        //用户不在当前页面时，停止监听
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
        if (music_status!!&&mediaPlayer!=null) {
            mediaPlayer!!.pause()
        }
    }

    override fun onDestroy() {

        Log.i("PlayActivity","destory")
        loading_gifdrawable!!.recycle()
        img_loading = null;
        pickfail_dialog=null;
        picksuccess_dialog=null;
        if (danmaku != null) {
            danmaku!!.stop()
            danmaku!!.release();
            danmaku = null;
        }
        enterOrLeave(LEAVE_ROOM)
        stophandler = true;
        agoraAPIOnlySignal!!.channelLeave(deviceid)
        rtcEngine!!.leaveChannel();
        if (mediaPlayer != null) {
            mediaPlayer!!.stop();
            mediaPlayer!!.release()
        }
        if (curuserplay) {
            //是否能够按返回键 下爪后到出结果这段时间都不能响应
            if (backeventadble) {
                Log.i("go","go")
                img_countdown!!.stopcountdown(true)
                halfwayout()
            }
        }
        super.onDestroy()

    }

    //返回键
    override fun onBackPressed() {
        //如果当前玩家正在玩 按返回键 出现弹框提醒 取消后解锁房间
        if (curuserplay) {
            //是否能够按返回键 下爪后到出结果这段时间都不能响应
            if (backeventadble) {
                var dialog = TisDialog(this).create().setMessage("当前正在游戏中，是否退出")
                        .setPositiveButton { v ->
//                            lockOrUnlockRoom(UNLOCK);
                            img_countdown!!.stopcountdown(true)
                            halfwayout()
                            super.onBackPressed()
                        }.setNegativeButton { v -> }.show()
            }
        } else super.onBackPressed();

    }

    //
    override fun backfinish(view: View) {
        if (curuserplay) {
            if (backeventadble) {
                var dialog = TisDialog(this).create().setMessage("当前正在游戏中，是否退出")
                        .setPositiveButton { v ->
//                            lockOrUnlockRoom(UNLOCK);
                            img_countdown!!.stopcountdown(true)
                            halfwayout()
                            super.onBackPressed()
                        }.setNegativeButton { v -> }.show()
            }
        } else super.backfinish(view);
    }

    //检查礼品是否抽中线程  10s内定时轮询
    inner class CheckRunnable : Runnable {
        //查询次数 10次 每次1000ms   10内查出结果
        public var select_count = 10;
        //是否抓中
        var iscatch = false

        override fun run() {
            while (select_count > 0 && !iscatch) {
                checkRequest();
                Thread.sleep(1000)
                select_count--;
            }
            if (stophandler)
                return
            //轮询结束后发送结果到handler
            var message = Message.obtain();
            message.what = CHECK_RESULT;
            message.obj = iscatch;
            mhandler.sendMessage(message)

        }

        //查询请求
        fun checkRequest() {
            var map = HashMap<String, String>();
            map.put("sn", Constant.SN)
            map.put("userid", getLoginid());
            map.put("roomid", roomid);
            map.put("machinecode", deviceid!!);
            map.put("ip", ip!!);
            map.put("sign", SignParamUtil.getSignStr(map));
            Http.post {
                url = Constant.SERVERIP + Constant.check
                headers { "Content-Type" - "application/json" }
                val gson = Gson()
                val json: String = gson.toJson(map)

                raw = json

                Logger.i(json)
                Logger.i(url)

                onSuccess {
                    Logger.i("on success ${it.toString(Charset.defaultCharset())}")
                    var result = GsonUtil.jsonToObject(it.toString(Charset.defaultCharset()), JsonObject::class.java)

                    if (result!!.get("errorcode").toString().equals("0")) {
                        if (result!!.get("isget").toString().equals("false")) {
                            iscatch = false;
                        } else {
                            iscatch = true;
                            goodsid = result.get("goodsid").asString
                        }
                    } else {
                        SToast(result.get("msg").asString)
                    }
                }
                onFail { error ->
                    Logger.i("on fail $error")
                }

                onFinish {
                }
            }

        }
    }


    //根据房间是否锁住 更改页面的View
    fun changeViewByLock(lock: String, username: String, headurl: String, userid: String) {

        if (!lock.equals("0")) {

            var username = username
            var headurl = headurl
            startable = false
            if (!this.isFinishing) {
                Glide.with(this).load(headurl).listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        circle_img_user!!.setImageDrawable(resource)
                        return false
                    }
                }).placeholder(R.drawable.default_img).into(circle_img_user)
            }
            tv_user_name!!.text = username
            linear_current_user!!.visibility = View.VISIBLE
            if (userid.equals(getLoginid()) && iscurLock) {
                linear_play!!.visibility = View.VISIBLE;
                linear_start!!.visibility = View.GONE;
            } else {
                linear_play!!.visibility = View.GONE;
                linear_start!!.visibility = View.VISIBLE;
                img_start!!.setImageResource(R.drawable.kaishi_stop)
            }
        } else {
            startable = true;
            img_start!!.setImageResource(R.drawable.kaishi)
            linear_current_user!!.visibility = View.GONE
            linear_play!!.visibility = View.GONE;
            linear_start!!.visibility = View.VISIBLE;
        }
    }

    //播放音乐
    fun playMusic() {
        //随机选择一首音乐
        var musicList:IntArray=intArrayOf(R.raw.bg_music,R.raw.bg_music_b,R.raw.bg_music_c)
        var random=Random().nextInt(3)
        mediaPlayer!!.reset()
        mediaPlayer=MediaPlayer.create(this,musicList[random]);
        mediaPlayer!!.isLooping=true
        mediaPlayer!!.setOnCompletionListener { object:MediaPlayer.OnCompletionListener{
            override fun onCompletion(mp: MediaPlayer?) {
                mediaPlayer!!.start()
            }
        } }
        mediaPlayer!!.start()

    }
    //放弃游戏
    fun stopGame(){
        lockOrUnlockRoom(UNLOCK)
        iscurLock = false
        curuserplay = false
    }
    //再玩一局
    fun playAgain(){
        reading_dialog!!.show()
        //延时3秒后再发送请求
        Handler().postDelayed(Runnable {
            if (coins < price.toInt()) {
                getUserInfoCoin()
            }
            putCoinRequest(false)
        }, 3000)
    }

    //打开h5支付
    fun openWebView(callbackurl: String) {
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


    override fun onSystemError(what: Int, errormsg: String, allresult: JsonObject) {

        reading_dialog!!.dismiss()
        when (what) {
            PUT_CODE -> {
                SToastCancel()
                if (errormsg.equals("余额不足上分失败")) {
                    getShareTimes()
                }else
                {
                    super.onSystemError(what, errormsg,allresult)
                }
            }

        }

    }

    override fun onHttpError() {
        super.onHttpError()
        reading_dialog!!.dismiss()
    }




    //声网回调handler
    internal var rtchandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        //加入房间成功回调
        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            Log.i("join_channel_success", channel + " " + uid + " " + elapsed);
        }

        //失败回调
        override fun onError(err: Int) {
            super.onError(err)
            Log.i("join_channel_failed", err.toString());
            if (err == 109) {
                isexpire = true
                getChannelKey()
            }
        }

        //收到第一帧数据后显示视频
        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            Handler(mainLooper).post {
                img_loading!!.visibility = View.GONE
                frame_p!!.visibility = View.VISIBLE
            }

        }
    }
    //声网信令回调
    private val signCallback = object : IAgoraAPI.ICallBack {
        override fun onReconnecting(i: Int) {

        }

        override fun onReconnected(i: Int) {

        }

        //信令登录成功回调
        override fun onLoginSuccess(i: Int, i1: Int) {
            Log.i("signal_login_success", i.toString() + " " + i1.toString())

        }

        //退出信令登录回调
        override fun onLogout(i: Int) {
            Log.i("signal_logout", i.toString())
        }

        //信令登陆失败回调
        override fun onLoginFailed(i: Int) {
            Log.i("signal_login_failed", i.toString())
        }

        //信令成功登陆房间回调
        override fun onChannelJoined(s: String) {
            Log.i("signal_join_channel", s)
            //获得该频道内的人数
            agoraAPIOnlySignal!!.channelQueryUserNum(deviceid);
//            agoraAPIOnlySignal!!.messageChannelSend(deviceid, getUsername() + "进来了", "");


        }

        //信令登陆房间失败回调
        override fun onChannelJoinFailed(s: String, i: Int) {
            Log.i("signal_join_failed", s + " " + i.toString())
        }

        override fun onChannelLeaved(s: String, i: Int) {
            Log.i("sign_leave_channel", s + " " + i.toString());
            agoraAPIOnlySignal!!.getUserAttrAll(s)
        }

        //有用户加入频道回调
        override fun onChannelUserJoined(s: String, i: Int) {
            Log.i("signal_channel_success", s + " " + i.toString())
            if (!s.startsWith(adminAccount)) {
                persons += 1;
                runOnUiThread {
                    tv_room_persons!!.text = "(" + persons.toString() + "人)"
                    var req = "(" + "\"" + s + "\"" + ")"
                    roomUserListF(req, INCOME)
                }
            }
        }

        //有用户离开频道回调
        override fun onChannelUserLeaved(s: String, i: Int) {
            Log.i("signal_channel_leave", s + " " + i.toString())
            if (!s.startsWith(adminAccount)) {
                persons -= 1;
                runOnUiThread {
                    tv_room_persons!!.text = "(" + persons.toString() + "人)"
                    var req = "(" + "\"" + s + "\"" + ")"
                    if (s != getLoginid()) {
                        roomUserListF(req, OUTCOME)
                    }
                }
            }
        }

        //当前频道用户列表
        override fun onChannelUserList(strings: Array<String>, ints: IntArray) {
            var dateList = ""
            if (!strings.isEmpty()) {
                for (i in strings) {
                    dateList += "," + "\"" + i.toString() + "\""
                }
                dateList = dateList.subSequence(1, dateList.length).toString()
                dateList = "(" + dateList + ")"
                runOnUiThread {
                    roomUserList(dateList)
                }
            }
            Log.i("current_channel_list", strings.size.toString() + ints.size.toString())


        }

        override fun onChannelQueryUserNumResult(s: String, i: Int, i1: Int) {
            Log.i("channel_mans", i1.toString())
            persons = i1 - 1
            Handler(mainLooper).post {
                tv_room_persons!!.text = "(" + persons.toString() + "人)"
            }
        }

        override fun onChannelQueryUserIsIn(s: String, s1: String, i: Int) {

        }

        override fun onChannelAttrUpdated(s: String, s1: String, s2: String, s3: String) {

        }

        override fun onInviteReceived(s: String, s1: String, i: Int, s2: String) {

        }

        override fun onInviteReceivedByPeer(s: String, s1: String, i: Int) {

        }

        override fun onInviteAcceptedByPeer(s: String, s1: String, i: Int, s2: String) {

        }

        override fun onInviteRefusedByPeer(s: String, s1: String, i: Int, s2: String) {

        }

        override fun onInviteFailed(s: String, s1: String, i: Int, i1: Int, s2: String) {

        }

        override fun onInviteEndByPeer(s: String, s1: String, i: Int, s2: String) {

        }

        override fun onInviteEndByMyself(s: String, s1: String, i: Int) {

        }

        override fun onInviteMsg(s: String, s1: String, i: Int, s2: String, s3: String, s4: String) {

        }

        override fun onMessageSendError(s: String, i: Int) {
            Log.i("send_error", s + " " + i)
        }

        override fun onMessageSendProgress(s: String, s1: String, s2: String, s3: String) {

        }

        override fun onMessageSendSuccess(s: String) {
            Log.i("send message", s)
        }

        override fun onMessageAppReceived(s: String) {

        }

        //接收点对点消息
        override fun onMessageInstantReceive(s: String, i: Int, s1: String) {

            Log.i("instant_msg", s + " " + s1);

            var msg_json = GsonUtil.jsonToObject(s1, JsonObject::class.java);
            var cur_coins = msg_json!!.get("coins").asInt
            runOnUiThread {
                coins += cur_coins
                Log.i("coins", coins.toString())
                tv_price_info!!.text = "余额: "+coins
                SharedPreferencesUtil.putString(applicationContext, Constant.USERCOINS, coins.toString());
                SToast("充值成功")
            }

        }

        override fun onMessageChannelReceive(s: String, s1: String, i: Int, s2: String) {
            //添加弹幕
            Log.i("getmessage", s + " " + s1 + " " + i + " " + s2)
            if (!s1.startsWith(adminAccount)) {
                runOnUiThread {
                    addDanmakuList(s2)
                }
            }
            if (isDanmuOk && !s1.startsWith(adminAccount)) {
                addDanmaku(s2)
            }
            //处理管理员发来的推送消息
            if (s1.startsWith(adminAccount)) {
                runOnUiThread {
                    var rjson = GsonUtil.jsonToObject(s2, JsonObject::class.java);
                    var lock = rjson!!.get("lock").asString
                    if (!lock.equals("0")) {
                        changeViewByLock(lock, rjson.get("username").asString, rjson.get("headurl").asString, rjson.get("userid").asString)
                    } else changeViewByLock(lock, "", "", "")
                }
            }

        }

        override fun onLog(s: String) {

        }

        override fun onInvokeRet(s: String, s1: String, s2: String) {

        }

        override fun onMsg(s: String, s1: String, s2: String) {

        }

        override fun onUserAttrResult(s: String, s1: String, s2: String) {

        }

        override fun onUserAttrAllResult(s: String, s1: String) {

        }

        override fun onError(s: String, i: Int, s1: String) {

        }

        override fun onQueryUserStatusResult(s: String, s1: String) {

        }

        override fun onDbg(s: String, bytes: ByteArray) {

        }

        override fun onBCCall_result(s: String, s1: String, s2: String) {

        }
    }

    //查询房间用户信息接口
    fun roomUserList(datelist: String) {
        var param = HashMap<String, String>()
        param.put("sn", Constant.SN)
        param.put("userlist", datelist)
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.roomuserlist, param, ROOMUSERLIST)
    }

    //查询房间用户信息接口
    fun roomUserListF(datelist: String, type: Int) {
        var param = HashMap<String, String>()
        param.put("sn", Constant.SN)
        param.put("userlist", datelist)
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.roomuserlist, param, type)
    }


    //查询机器故障的信息接口
    fun getBreakInfoMachine() {
        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sympol", "user_error")
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getsympol, param, BREAKINFOMACHINE)
    }


    //用户反馈接口
    fun feedBack(content: String) {

        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("username", getUsername())
        param.put("content", content)
        param.put("machinename", machinename)
        param.put("machineid", machineid!!)
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.feedback, param, FEEDBACK)
    }


    //查询分享链接接口
    fun getShareCodeUrl() {

        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sympol", "sharecode_url")
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getsympol, param, GETSHARECODEURL)
    }


    //友盟分享到微信
    fun umengShareInit(url: String) {
        var shareListener = object : UMShareListener {
            /**
             * @descrption 分享开始的回调
             * @param platform 平台类型
             */
            override fun onStart(p0: SHARE_MEDIA?) {
            }

            /**
             * @descrption 分享成功的回调
             * @param platform 平台类型
             */
            override fun onResult(p0: SHARE_MEDIA?) {
                SToast("成功")
            }


            /**
             * @descrption 分享失败的回调
             * @param platform 平台类型
             * @param t 错误原因
             */
            override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                SToast("失败:请检查是否安装微信")
                Log.e("失败", p1!!.message)
            }

            /**
             * @descrption 分享取消的回调
             * @param platform 平台类型
             */
            override fun onCancel(p0: SHARE_MEDIA?) {
                SToast("取消")
            }

        };
        val web = UMWeb(url+getUsercode())
        web.title = "抓萌抓娃娃"//标题
        web.description = " "//描述
        var shareAction = ShareAction(this)
                .withMedia(web)
                .withText("分享")
                .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener)
                .open();


    }



    //查询用户是否可以分享分享码
    fun getShareTimes(){
        var param = HashMap<String, String>()
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", SignParamUtil.getSignStr(param))
        setShowPro(false)
        httpPost(Constant.SERVERIP + Constant.getsharetimes, param, GETSHARETIMES)
    }


}

