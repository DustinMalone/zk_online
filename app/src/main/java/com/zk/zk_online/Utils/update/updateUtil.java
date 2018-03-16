package com.zk.zk_online.Utils.update;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zk.lpw.config.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by Administrator on 2017/5/6 0006.
 */
//APP更新工具类
public class updateUtil {
    private Context context;
    private UpdateManager updateManager;
    public updateUtil(Context context)
    {
        this.context=context;
    }
    //获得当前APP版本
    private int getVersionCode() {
        try {
            String pkName = context.getPackageName();
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            Log.i("version",versionCode+"");
            return versionCode;
        } catch (Exception e) {
        }
        return 0;
    }
    //检查是否更新APP
    public void requestUpdate(final UpdateUtilListener listener) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("version", getVersionCode() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(Constant.Companion.getUPDATEURL());
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    Log.e("result",jsonObject.toString());
                    UpdateManager.AppUpdateVO  appUpdateVO= gson.fromJson(jsonObject.getJSONObject("data").toString(),UpdateManager.AppUpdateVO.class);
                    if (appUpdateVO.getError_code() == 0) {

                        updateManager = new UpdateManager(context, appUpdateVO);
                        if (!updateManager.checkUpdate()) {

                        }
                    } else {
                            //最新版本,即不用更新,回调
                            listener.no_update();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
