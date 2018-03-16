package com.zk.zk_online.Utils;

import android.util.Log;

import com.google.gson.JsonObject;
import com.zk.lpw.config.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * Created by ZYB on 2017/11/11 0011.
 */

public class HttpRequest {

    public static void httpPost(String param,String url)
    {
        RequestParams requestparams = new RequestParams(Constant.Companion.getSERVERIP() + url);
        requestparams.setConnectTimeout(5000);
        requestparams.setAsJsonContent(true);
        requestparams.setBodyContent(param);
        requestparams.setCharset("UTF-8");
        x.http().post(requestparams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                    Log.i("success",result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("failed","failed");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }



    interface XutilHttpCallBack
    {
        void successCallback(String result);
        void successCallFailed(String message);
    }
}
