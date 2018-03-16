package com.zk.lpw.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager


/**
 * 获取版本号
 * Created by mukun on 2017/8/21.
 */
class VersionUtil {
    companion object {
        /**
         * get App versionCode
         *
         * @param context
         * @return
         */
        fun getVersionCode(context: Context): String {
            val packageManager = context.packageManager
            val packageInfo: PackageInfo
            var versionCode = ""
            try {
                packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                versionCode = packageInfo.versionCode.toString() + ""
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionCode
        }

        /**
         * get App versionName
         *
         * @param context
         * @return
         */
        fun getVersionName(context: Context): String {
            val packageManager = context.packageManager
            val packageInfo: PackageInfo
            var versionName = ""
            try {
                packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                versionName = packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionName
        }
    }

}