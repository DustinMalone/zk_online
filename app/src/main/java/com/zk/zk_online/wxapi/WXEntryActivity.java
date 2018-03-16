package com.zk.zk_online.wxapi;


import android.content.Intent;
import android.util.Log;

import com.umeng.socialize.UMShareAPI;
import com.umeng.weixin.callback.WXCallbackActivity;

//import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult","onActivityResult");
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
