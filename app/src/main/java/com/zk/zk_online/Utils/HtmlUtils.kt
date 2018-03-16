package com.zk.zk_online.Utils

import android.content.Context
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


/**
 * Created by LCB on 2017/12/4.
 */
class HtmlUtils {
    companion object {
    /**
     * @function escapeHTML 转义html脚本 < > & " '
     * @param a -
     *            字符串
     */
    fun escapeHTML(a:String):String{
        return a.replace(" ", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("\'", "&apos;")
    }

    /**
     * @function unescapeHTML 还原html脚本 < > & " '
     * @param a -
     *            字符串
     */
    fun unescapeHTML(a:String):String{
        return a.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&quot;", "\"").replace("&apos;", "'");
    }



        /**
         * 验证手机格式
         */
        fun isMobileNO(mobiles: String): Boolean {
            /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        17[678],14[57]
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
            val telRegex = "[1][34578]\\d{9}"//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
            return if (TextUtils.isEmpty(mobiles))
                false
            else
                mobiles.matches(telRegex.toRegex())
        }


        /**
         * 返回手机运营商名称，在调用支付前调用作判断
         * @param telephonyManager
         * @return
         */
        fun getProvidersName(context: Context): String? {
            var ProvidersName: String? = null
            var telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val IMSI = telephonyManager.subscriberId ?: return "unknow"

            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动"
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通"
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信"
            }

            try {
                ProvidersName = URLEncoder.encode("" + ProvidersName!!, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

//            Log.e("TAG_IMSI", "==== 当前卡为：" + ProvidersName!!)
            return ProvidersName
        }
    }
}