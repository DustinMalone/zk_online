package com.zk.zk_online.Utils



/**
 * Created by ZYB on 2017/11/14 0014.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZYB on 2017/6/11 0011.
 */
public class GsonUtil
{

    companion object
    {    val gson = Gson()
        /**
         * json数据转object
         * @param jsonstr json数据
         * *
         * @param Class 泛型class
         * *
         * @param <T> 泛型
         * *
         * @return
        </T> */
        fun <T> jsonToObject(jsonstr: String, Class: Class<T>): T? {
            if (TextUtil.TextIsEmpey(jsonstr)) return null
            val t = gson.fromJson(jsonstr, Class)
            return t
        }

        /**
         * JSON数据转List<T>
         * @param jsonstr  json数据
         * *
         * @param cls       class type类型
         * *
         * @param <T>       T泛型
         * *
         * @return
        </T></T> */
        fun <T> jsonToList(jsonstr: String, cls: Class<T>): kotlin.collections.List<T>? {
            if (TextUtil.TextIsEmpey(jsonstr)) return null
            val list = ArrayList<T>()
            val array = gson.fromJson(jsonstr, JsonArray::class.java)
            for (elem in array) {
                val t = gson.fromJson(elem.toString(), cls)
                list.add(t)
            }
            return list
        }

        /**
         * 对象转json
         * @param object 要转的对象
         * *
         * @param <T>    对象类型
         * *
         * @return
        </T> */
        fun <T> objectToJson(`object`: T?): String {
            var jsonStr = ""
            if (`object` != null) {
                jsonStr = gson.toJson(`object`)
            }
            return jsonStr

        }
    }

}
