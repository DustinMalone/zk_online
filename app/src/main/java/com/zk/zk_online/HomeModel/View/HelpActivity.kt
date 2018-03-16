package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zk.zk_online.R
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_help.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class HelpActivity : BaseActivity() {
    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_help)
        setTitle("帮助")
        tv_help_content.movementMethod = ScrollingMovementMethod.getInstance()
        val sb = StringBuffer()
        var line:String?=null
        val input = resources.openRawResource(R.raw.help)
        var read: BufferedReader =BufferedReader(InputStreamReader(input))
        while (read.readLine().apply{line=this}!=null){
            sb.append(line)
            sb.append("\n")
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//            tv_help_content.text= Html.fromHtml(sb.toString(),Html.FROM_HTML_MODE_COMPACT)
//        }else{
//            tv_help_content.text= Html.fromHtml(sb.toString())
//        }

        //使用WEBVIEW
        initWebView(sb.toString())

    }

    override fun onClick(v: View?) {

    }

    override fun getMessage(bundle: Bundle) {

    }


    fun initWebView(loadUrl:String)
    {
        var webSettings = wb_help_content.settings
        //支持缩放，默认为true。
        webSettings .setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings .setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings .setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings .setDefaultTextEncodingName("utf-8");
        //设置自动加载图片
        webSettings .setLoadsImagesAutomatically(true);
        webSettings.javaScriptEnabled=true
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        wb_help_content.webViewClient= object : WebViewClient() {
            //点击网页中按钮时，在原页面打开
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                return shouldOverrideUrlLoading(view,url)
            }

            //页面加载完成后执行
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

            }
        }
        wb_help_content.loadDataWithBaseURL(null,loadUrl, "text/html", "UTF-8", null);
    }

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun readTextByLine(`is`: InputStream): String {
        val reader = InputStreamReader(`is`)
        val bufferedReader = BufferedReader(reader)
        val buffer = StringBuffer("")
        var str: String=""

        while ((bufferedReader.readLine().apply{str=this})!= null) {
            buffer.append(str)
            buffer.append("\n")
        }
        return buffer.toString()
    }
}
