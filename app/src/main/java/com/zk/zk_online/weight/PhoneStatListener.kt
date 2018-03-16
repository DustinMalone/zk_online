package com.zk.zk_online.weight

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.widget.ImageView
import com.zk.zk_online.R


/**
 * Created by LCB on 2017/12/6.
 */
class PhoneStatListener(var mContext:Context,var imageview: ImageView):PhoneStateListener()  {

    val NETWORKTYPE_ERROR = -1
     val NETWORKTYPE_WIFI = 0
     val NETWORKTYPE_4G = 1
    val NETWORKTYPE_3G=2
     val NETWORKTYPE_2G = 3
     val NETWORKTYPE_NONE = 4



    //获取信号强度

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
        super.onSignalStrengthsChanged(signalStrength)

        //获取网络信号强度
        //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
        //            int level = signalStrength.getLevel();
        var gsmSignalStrength = signalStrength.gsmSignalStrength
        //信号强度换算公式
        var dBm_Signal = -113 + 2 * signalStrength.gsmSignalStrength

        val signalInfo = signalStrength.toString()
        val params = signalInfo.split(" ")
//        val params = signalInfo.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        //获得信号强度值

        //获取网络类型
        var netWorkType = getNetWorkType(mContext)

//        Log.e("netWorkType",netWorkType.toString())

        when (netWorkType) {
            NETWORKTYPE_WIFI -> {
//                Log.e("dBm_Signal",dBm_Signal.toString())
               setLevel(signalStrength.level)
            }
            NETWORKTYPE_3G->{
                setLevel(signalStrength.level)
//                var yys = HtmlUtils.getProvidersName(mContext)//获取当前运营商
//                if (yys === "中国移动") {
//                    imageview.setBackgroundResource(R.color.transparent) //中国移动3G不可获取，故在此返回0
//                } else if (yys === "中国联通") {
//                    var cdmaDbm = signalStrength.cdmaDbm
//                    setState(cdmaDbm)
//                } else if (yys === "中国电信") {
//                    var evdoDbm = signalStrength.evdoDbm
//                    setState(evdoDbm)
//                }
            }
            NETWORKTYPE_2G ->{
//                setState(dBm_Signal)
                setLevel(signalStrength.level)
            }
            NETWORKTYPE_4G ->{
                //4G网络 最佳范围   >-90dBm 越大越好
//                var Itedbm = Integer.parseInt(params[9])
//                setState(Itedbm)
                setLevel(signalStrength.level)
            }
            NETWORKTYPE_NONE -> {
                imageview.setBackgroundResource(R.color.transparent)
            }
            NETWORKTYPE_ERROR -> {
                imageview.setBackgroundResource(R.color.transparent)
            }

        }
    }


    fun getNetWorkType(context: Context): Int {
        var mNetWorkType = -1
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val type = networkInfo.typeName
            if (type.equals("WIFI", ignoreCase = true)) {
                mNetWorkType = NETWORKTYPE_WIFI
            } else if (type.equals("MOBILE", ignoreCase = true)) {
                return if (isFastMobileNetwork(context)) NETWORKTYPE_4G
                else if (isThreeGMobileNetwork(context))NETWORKTYPE_3G else NETWORKTYPE_2G
            }
        } else {
            mNetWorkType = NETWORKTYPE_NONE//没有网络
        }
        return mNetWorkType
    }

    /**判断网络类型 */
     fun isFastMobileNetwork(context: Context): Boolean {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (telephonyManager.networkType == TelephonyManager.NETWORK_TYPE_LTE) {
            //这里只简单区分两种类型网络，认为4G网络为快速，但最终还需要参考信号值
            true
        } else false
    }


    /**判断网络类型 */
    fun isThreeGMobileNetwork(context: Context): Boolean {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
            //这里只简单区分两种类型网络，认为4G网络为快速，但最终还需要参考信号值
            true
        } else false
    }

    //4G网络 最佳范围   >-90dBm 越大越好
    fun setState(v:Int){
        if (v>=-90&&v<0){
            imageview.setBackgroundResource(R.drawable.wifig)
        }else if (v<-90){
            imageview.setBackgroundResource(R.drawable.wifir)
        }else{
            imageview.setBackgroundResource(R.color.transparent)
        }
    }


    //4G网络 最佳范围   >-90dBm 越大越好
    fun setLevel(v:Int){
        if (v>=3){
            imageview.setBackgroundResource(R.drawable.wifig)
        }else if (v in 1..2){
            imageview.setBackgroundResource(R.drawable.wifir)
        }else{
            imageview.setBackgroundResource(R.color.transparent)
        }
    }
}