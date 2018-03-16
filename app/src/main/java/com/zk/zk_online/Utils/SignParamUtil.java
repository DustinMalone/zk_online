package com.zk.zk_online.Utils;

import android.util.Log;

import com.zk.lpw.config.Constant;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * 加密参数 类
 * Created by ZYB on 2017/11/21 0021.
 */

public class SignParamUtil {

    public static String getSignStr(HashMap<String,String> param)
    {
        String sign="";
        Map<String,String> result=sortMapByKey(param);
        for (Map.Entry<String,String> entry:result.entrySet())
        {
            if (!SomeUtil.TextIsEmpey(entry.getValue()))
            {
                sign+=entry.getKey()+"="+entry.getValue()+"&";
            }
        }
        sign+= Constant.Companion.getKEY();
        Log.i("sign",sign);
        return MD5Utils.getPwd(sign).toLowerCase();
    }
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
}

class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
