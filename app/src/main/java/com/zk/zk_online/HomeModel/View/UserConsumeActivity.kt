package com.zk.zk_online.HomeModel.View

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.zk.lpw.config.Constant
import com.zk.zk_online.Adapter.ConsumeAdapter
import com.zk.zk_online.HomeModel.Adapter.CatchGifHistoryAdapter
import com.zk.zk_online.HomeModel.Model.CatchGifHistory
import com.zk.zk_online.HomeModel.Model.Gitdata
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.MD5Utils
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class UserConsumeActivity :BaseActivity()
{

    var lv_consume:ListView?=null;
    var adapter:ConsumeAdapter?=null;
    var catch_adapter:CatchGifHistoryAdapter?=null;

    var tv_payment_history:TextView?=null

    var tv_catch_history:TextView?=null

    var iv_whitout_data:ImageView?=null

    var catchList=ArrayList<CatchGifHistory>()

    //消费记录
    val PURCHASE_HISTORY = 0;

    val CATCH_HISTORY = 1;

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_consume)
        setTitle("我的账单")
        lv_consume=findViewById(R.id.lv_consume)
        tv_payment_history=findViewById(R.id.tv_payment_history)
        tv_catch_history=findViewById(R.id.tv_catch_history)
        iv_whitout_data=findViewById(R.id.iv_whitout_data)
//        setData()
        getData()
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            //消费记录
            R.id.tv_payment_history -> {
                tv_catch_history!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))
                tv_payment_history!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_FFA54F))
                var  dra: Drawable = ContextCompat.getDrawable(applicationContext,R.drawable.wodelipin_f);
                dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
                tv_payment_history!!.setCompoundDrawables(null,null,null,dra)
                tv_catch_history!!.setCompoundDrawables(null,null,null,null)
                getData();

            }
             //获取记录
            R.id.tv_catch_history -> {
                tv_payment_history!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.grey))

                tv_catch_history!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_FFA54F))

                var  dra: Drawable = ContextCompat.getDrawable(applicationContext,R.drawable.wodelipin_f);
                dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
                tv_payment_history!!.setCompoundDrawables(null,null,null,null)
                tv_catch_history!!.setCompoundDrawables(null,null,null,dra)
                lv_consume!!.adapter=null

               CatchData()
            }

            //后退
            R.id.iv_consume_back->{
                finish()
            }
        }

    }

    override fun getMessage(bundle: Bundle) {
        var result = bundle.getString("msg")
        var rjson= GsonUtil.jsonToList(bundle.getString("msg"), Gitdata::class.java) as ArrayList<Gitdata>
        when (bundle.getInt("what")) {
        //获取消费记录
            PURCHASE_HISTORY -> {
                var rjson= GsonUtil.jsonToList(result, Gitdata::class.java) as ArrayList<Gitdata>
                adapter= ConsumeAdapter(this,rjson)
                lv_consume!!.adapter=adapter

                if (adapter!!.list.isEmpty()) {
                    iv_whitout_data!!.visibility=View.VISIBLE
                }else{
                    iv_whitout_data!!.visibility=View.GONE
                }
            }

            CATCH_HISTORY -> {
                catchList= GsonUtil.jsonToList(result, CatchGifHistory::class.java) as ArrayList<CatchGifHistory>
               catch_adapter=CatchGifHistoryAdapter(this,catchList)
                lv_consume!!.adapter=catch_adapter

                if (catch_adapter!!.list.isEmpty()) {
                    iv_whitout_data!!.visibility=View.VISIBLE
                }else{
                    iv_whitout_data!!.visibility=View.GONE
                }

            }

        }
    }
    //请求获得消费记录
    fun getData(){

        var param = HashMap<String, String>();
        val sign = "sn=" + Constant.SN + "&userid=" + getLoginid() + "&" + Constant.KEY
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign", MD5Utils.getPwd(sign).toLowerCase())
        httpPost(Constant.SERVERIP + Constant.purchase_history, param, PURCHASE_HISTORY);
    }

    //请求抓取记录
    fun CatchData(){

        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("sign",SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.catch_history, param, CATCH_HISTORY);
    }




}

