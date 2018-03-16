package com.zk.lpw.utils

import android.content.Context
import android.content.SharedPreferences
import com.zk.lpw.config.Constant

/**
 * Created by mukun on 2017/8/18.
 */
class SharedPreferencesUtil {

    companion object {
        private var mSharedPreferences: SharedPreferences? = null

        private fun getSharedPreferences(context: Context): SharedPreferences? {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(Constant.DATA, 0)
            }
            return mSharedPreferences

        }

        /**
         * @param context 上下文
         * @param key     键
         * @param value   默认值
         */
        fun putInt(context: Context, key: String, value: Int) {
            val editor = getSharedPreferences(context)!!.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        /**
         * @param context 上下文
         * @param key     键
         * @param value   默认值
         * @return 根据key获得的值
         */
        fun getInt(context: Context, key: String, value: Int): Int {
            return getSharedPreferences(context)!!.getInt(key, value)
        }

        /**
         * @param context 上下文
         * @param key     键
         * @param value   默认值
         */
        fun putString(context: Context, key: String, value: String) {
            val editor = getSharedPreferences(context)!!.edit()
            editor.putString(key, value)
            editor.commit()
        }

        /**
         * @param context 上下文
         * @param key     键
         * @param value   默认值
         * @return 根据key获得的值
         */
        fun getString(context: Context, key: String, value: String): String {
            return getSharedPreferences(context)!!.getString(key, value)
        }

        /**
         * @param context 上下文
         * *
         * @param key     键
         * *
         * @param value   默认值
         */
        fun putBoolean(context: Context, key: String, value: Boolean) {
            val editor = getSharedPreferences(context)!!.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        /**
         * @param context 上下文
         * *
         * @param key     键
         * *
         * @param value   默认值
         * *
         * @return 根据key获得的值
         */
        fun getBoolean(context: Context, key: String, value: Boolean): Boolean {
            return getSharedPreferences(context)!!.getBoolean(key, value)
        }

        /**
         * @param context 上下文
         * @param key     键
         * *
         * @param value   默认值
         */
        fun putLong(context: Context, key: String, value: Long?) {
            val editor = getSharedPreferences(context)!!.edit()
            editor.putLong(key, value!!)
            editor.commit()
        }

        /**
         * @param context 上下文
         * @param key     键
         * @param value   默认值
         * @return 根据key获得的值
         */
        fun getLong(context: Context, key: String, value: Long?): Long {
            return getSharedPreferences(context)!!.getLong(key, value!!)
        }

        /**
         * @param context 上下文
         * @param key     键
         */
        fun remove(context: Context, key: String) {
            val editor = getSharedPreferences(context)!!.edit()
            editor.remove(key)
            editor.commit()
        }

        fun clear(context: Context){
            val editor = getSharedPreferences(context)!!.edit()
            editor.clear()
            editor.commit()
        }
    }
}