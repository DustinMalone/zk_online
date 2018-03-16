package com.zk.lpw.config

/**
 * Created by mukun on 2017/8/17.
 */
class Constant {
    companion object {
        val DATA = "data_LPW"
        val USER_NAME = "name"
        val USER_PASSWORD = "password"

        var URL=""

        //更新url
        var UPDATEURL="http://www.91qc.net.cn:82/livewawa/app/APPInfo.do";

        //更新内容
       var Update_Info="*优化界面\n*新增地址管理功能"


        //声网APPID
        var APPID="9aa74fadb6594733a673f40fab8d933d"
        var AppCertificate="18db294f9bb347c1bbeabd30ef13793d"
        //主播id
        var MUID=1234567890;
       //侧主播id
       var MUID2=2;


     //sp 用户是否记住密码
        var USERISREMEBERPWD="userisremeberpwd"
        //sp 用户手机
        var USERPHONE="userphone"
        //sp 用户手机密码
        var USERPHONEPWD="userphonepwd"
        //sp 用户id
        var LOGINID="loginid"
        //sp 用户编号
        var USERCODE="usercode"
        //sp 用户名
        var USERNAME="username"
        //sp 用户头像
        var USERIMG="userimg"
        //sp 用户性别
        var USERSEX="usersex"
        //sp背景音乐状态
       val MUSICSTATUS="music_status"
        //用户币数
        var USERCOINS="usercoins"
        //用户分享码
        var SHARE_CODE="sharecode"
        //登录时的时间戳
        val LOGIN_TIME="logintime"

        //微信APPID
        var  WXAPPID="wx0695d5f4b0263cf0";
        //微信AppSecret
        var AppSecret="60f07c6dec92ef1254ec785b26b1c174"

        //服务器IP
        var SERVERIP="http://wapi.zk2013.com/olzw/"
        //sn码
        var SN="1"
        //sn KEY
        var KEY="kedieonlinetest2017*"
        //包名
        var package_name="com.zk.zk_online"
        //APP名
        var apk_name="环球抓娃娃"

        //微信登录接口
        var wxlogin="api/user/online/wxlogin.do";
        //发送操作指令接口
        var command="api/extend/online/command.do";
        //获得房间的机台
        var getroomdetail="api/room/online/getroomdetail.do"
        //获取所有机台
        var getroombyall="api/room/online/getroombyall.do";
        //获得房间列表
        var getroomlist="api/room/online/getroomlist.do";
        //设备启动
        var start="api/extend/online/start.do";
        //检测是否有抓到娃娃
        var check="api/extend/online/check.do";
        //获取充值套餐接口
        var getpackages="api/pay/online/getpackages.do";
        //创建充值订单
        var createorder="api/pay/online/createorder.do";
        //查询用户充值记录列表接口
        var payorderlist="api/pay/online/payorderlist.do";
        //进入房间接口
        var ioroom="api/room/online/ioroom.do";
        //房间锁定接口
        var lockroom="api/room/online/lockroom.do";
        //获取channelkey
        var GETCHANNEL = "api/extend/online/getchannalkey.do"
        //获取用户信息
        var getuserinfo="api/user/online/getuserinfo.do";
        //获取消费记录
        var purchase_history = "api/user/online/getbilllogs.do"

        //获取抓取记录
        var catch_history = "api/user/online/getgrablogs.do"

        //获取礼品没到货列表
        var unsend_gift_list="api/user/online/getunsendgift.do"
        //获取banner接口
        var getbannber="api/ad/online/getbannber.do";

        //获取首页bannber
        var shouye_bannber="api/ad/online/getbannber.do"

        //用户id获取用户已发货单信息列表接口
        var getdeliveryorder="api/user/online/getdeliveryorder.do"

        //选择未发货礼品创建发货单
        var createdeliveryorder="api/user/online/createdeliveryorder_new.do"

        //收货地址管理接口
        var address="api/user/online/address.do"

        //查询地区通用接口
        var getareabyid="api/user/online/getareabyid.do"

        //获取用户已发送单据通过id获取单据明细目录列表
        var getdeliveryorderdetail="api/user/online/getdeliveryorderdetail.do"

        //获取用户已发送单据通过id获取单据明细目录列表(新)
        var getdeliveryorderdetail_new="api/user/online/getdeliveryorderdetail_new.do"

        //获取礼品信息
        var getgoodsdetail="api/user/online/getgoodsdetail.do";

        //用户确认发货接口
        var confirmdeliveryorder="api/user/online/confirmdeliveryorder_new.do"

        //输入邀请码赠币接口
        var getsharecode="api/user/online/getsharecode.do"

        //用户反馈接口
        var feedback="api/user/online/feedback.do"

        //系统消息接口
        var getsysmessage="api/user/online/getsysmessage.do"

        //抓取排行接口
        var roomranklist="api/room/online/roomranklist.do";

        //查询系统参数值接口
        var getsympol="api/extend/online/getsympol.do";

        //创建H5支付订单接口
        var createH5order="api/pay/online/createH5order.do";

        //注册接口
        var reg="api/user/online/reg.do";

        // 手机获取验证码接口
        var getauthcode="api/user/online/getauthcode.do";

        //登录接口
        var loginbyphone="api/user/online/loginbyphone.do";


        //查询房间用户信息接口
        var roomuserlist="api/room/online/roomuserlist.do";
        //获取每日签到赠币接口
        var getsigncoin="api/user/online/getsigncoin.do"
        //检测房间状态
        var checkroomstatus="api/room/online/checkroomstatus.do"

        //查询用户是否可以分享分享码
        var getsharetimes="api/user/online/getsharetimes.do"

        //查询用户反馈信息列表
        var getfeedbackbyuser="api/user/online/getfeedbackbyuser.do"

        //游戏还没下爪中途还退出房间
        var halfwayout="api/room/online/halfwayout.do"

        //获取首页中奖名单接口
        var getprizelist="api/room/online/getprizelist.do"

        //获取已返回申诉条数接口
        var getfeedbacknumber="api/user/online/getfeedbacknumber.do"

        //获取平台排行榜接口
        var getmainrankinglist="api/user/online/getmainrankinglist.do"

        //检测礼品是否可以加入接口
        var checkgiftbyid="api/user/online/checkgiftbyid.do"

       // 礼品兑换游戏币接口
        var giftexchangecoins="api/user/online/giftexchangecoins.do"

        // 创建支付邮费订单
        var createpostageorder="api/pay/online/createpostageorder.do"

        // 判断发货单据是否完成接口
        var checkdeliveryorderispay="api/user/online/checkdeliveryorderispay.do"

       // 判断用户是否领取分享码接口
       var isgetsharecode="api/user/online/isgetsharecode.do"

        // 开启线下机器
        var startofflinemachine="api/o2o/online/startofflinemachine.do"

        //线下机器识别链接
        //测试
//        var startofflinemachine_tag="http://testvip.zk2013.com/?C="
        //正式
        var startofflinemachine_tag="http://pay.zk2013.com/?C="


        //获取商城兑换商品列表接口
        var getgoodslist="api/shop/online/getgoodslist.do"

        // 获取用户可兑换公仔数接口
        var getexchangenum="api/shop/online/getexchangenum.do"

       // 获取可兑换礼品接口
        var getexchangelist="api/shop/online/getexchangelist.do"

        //  确认兑换娃娃接口
        var confirmexchange="api/shop/online/confirmexchange.do"

       // app创建商城物品支付订单接口
       var createexchangeorder="api/pay/online/ createexchangeorder.do"

      // 确认兑换娃娃是否支付接口
      var checkexchangeispay="api/shop/online/checkexchangeispay.do"


    }
}