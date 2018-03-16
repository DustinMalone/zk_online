package com.zk.zk_online.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZYB on 2017/6/11 0011.
 */

public class TextUtil {
    //判断字符串是否为空
    public static  boolean  TextIsEmpey(String str)
    {
        if (null==str||0==str.length()) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获得现在的时间字符串
     * @return
     */
    public static String getDateNow(String templet)
    {
        String dateString="";
        if (TextUtil.TextIsEmpey(templet))
            templet="yyyy-MM-dd : HH:mm:ss";
        Map<String,Object> map = new HashMap<String,Object>();
        SimpleDateFormat smp=new SimpleDateFormat(templet);
        dateString=smp.format(new Date());

        return dateString;



    }
}
