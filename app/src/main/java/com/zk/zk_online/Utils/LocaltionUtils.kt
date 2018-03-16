package com.zk.zk_online.Utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


/**
 * Created by LCB on 2018/1/15.
 */

class LocaltionUtils {
    companion object {

        private val TAG="Localtion"

        private val getIpUrl = "http://www.cz88.net/ip/viewip778.aspx"

        private val sGetAddrUrl = "http://ip-api.com/json/"

        /**
         * GPS定位
         * 通过LocationListener来获取Location信息
         */
        fun formListenerGetLocation(context:Context) {
            var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var locationListener = object : LocationListener {

                override fun onLocationChanged(location: Location) {
                    //位置信息变化时触发
                    Log.i(TAG, "纬度：" + location.getLatitude())
                    Log.i(TAG, "经度：" + location.getLongitude())
                    Log.i(TAG, "海拔：" + location.getAltitude())
                    Log.i(TAG, "时间：" + location.getTime())
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                    //GPS状态变化时触发
                }

                override fun onProviderEnabled(provider: String) {
                    //GPS禁用时触发
                }

                override fun onProviderDisabled(provider: String) {
                    //GPS开启时触发
                }
            }
            /**
             * 绑定监听
             * 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种，前者是GPS,后者是GPRS以及WIFI定位
             * 参数2，位置信息更新周期.单位是毫秒
             * 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
             * 参数4，监听
             * 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
             */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        }


        /**
         * 基站定位
         * 主动获取Location，通过以下方法获取到的是最后一次定位信息。
         * 注意：Location location=new Location(LocationManager.GPS_PROVIDER)方式获取的location的各个参数值都是为0。
         */
        fun getLocation(context:Context) {
            var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Log.i(TAG, "纬度：" + location.getLatitude())
            Log.i(TAG, "经度：" + location.getLongitude())
            Log.i(TAG, "海拔：" + location.getAltitude())
            Log.i(TAG, "时间：" + location.getTime())

        }


        /**
         * 获取IP
         */
        fun getWebIp() {
            object : Thread() {
                override fun run() {
                    var strForeignIP = ""
                    try {
                        var url = URL(getIpUrl)

                        var br = BufferedReader(InputStreamReader(url.openStream()))

                        var s = ""
                        var sb = StringBuffer("")
                        while ( br.readLine().apply { s=this } != null) {
                            sb.append(s + "\r\n")
                        }
                        br.close()

                        var webContent = ""
                        webContent = sb.toString()
                        var flagofForeignIPString = "IPMessage"
                        var startIP = (webContent.indexOf(flagofForeignIPString)
                                + flagofForeignIPString.length + 2)
                        var endIP = webContent.indexOf("</span>", startIP)
                        strForeignIP = webContent.substring(startIP, endIP)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }.start()

        }

        /**
         * 获取城市
         */
//        fun locateCityName(foreignIPString: String) {
//            object : Thread() {
//                override fun run() {
//                    try {
//                        var httpClient = DefaultHttpClient()
//                        var requestStr = sGetAddrUrl + foreignIPString
//                        var request = HttpGet(requestStr)
//                        var response = httpClient.execute(request)
//                        if (response.getStatusLine().getStatusCode() === HttpURLConnection.HTTP_OK) {
//                            var cityName = EntityUtils.toString(response.getEntity())
//
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//
//                }
//            }.start()
//
//        }


    }
}