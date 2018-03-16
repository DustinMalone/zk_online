package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zk.zk_online.R
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_banner_info.*



class BannerInfoActivity : BaseActivity() {

    var load_url=""


    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_banner_info)

        if (!TextUtils.isEmpty(intent.getStringExtra("banner_content"))){
            load_url=intent.getStringExtra("banner_content")
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tv_info.text= Html.fromHtml(Html.fromHtml(load_url).toString(),Html.FROM_HTML_MODE_COMPACT)
//        }else{
//            tv_info.text= Html.fromHtml(Html.fromHtml(load_url).toString())
//        }

        initWebView(Html.fromHtml(load_url))
    }

    override fun onClick(v: View?) {
    }

    override fun getMessage(bundle: Bundle) {
    }

    fun initWebView(loadUrl: Spanned){
        var webSettings = wb_info.settings
        //支持缩放，默认为true。
        webSettings .setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings .setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings .setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings .setDefaultTextEncodingName("UTF-8");
        //设置自动加载图片
        webSettings .setLoadsImagesAutomatically(true);
        webSettings.javaScriptEnabled=true
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        if (dm.densityDpi > 240) {
            webSettings.setDefaultFontSize(50) //可以取1-72之间的任意值，默认16
        }

        wb_info.webViewClient= object : WebViewClient() {
            //点击网页中按钮时，在原页面打开
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                return shouldOverrideUrlLoading(view,url)
            }

            //页面加载完成后执行
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

            }
        }

        wb_info.loadData(loadUrl.toString(),"text/html; charset=UTF-8","utf-8")
//        wb_info.loadDataWithBaseURL(null,loadUrl, "text/html; charset=UTF-8", "utf-8", null);
    }


}
