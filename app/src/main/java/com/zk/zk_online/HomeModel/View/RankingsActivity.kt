package com.zk.zk_online.HomeModel.View

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonObject
import com.zk.lpw.config.Constant
import com.zk.zk_online.Adapter.RankingsAdapter
import com.zk.zk_online.HomeModel.Model.Rankings
import com.zk.zk_online.R
import com.zk.zk_online.Utils.GsonUtil
import com.zk.zk_online.Utils.SignParamUtil
import com.zk.zk_online.base.BaseActivity
import kotlinx.android.synthetic.main.activity_rankings.*
import java.text.SimpleDateFormat
import java.util.*

class RankingsActivity : BaseActivity() {


    var adapter_rankings:RankingsAdapter?=null

    //获取排行榜
    val GETMAINRANKINGLIST=1

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_rankings)

        //初始化设置
        btn_rankings_pick_god.isEnabled=false
        getMainRankingList("1",null,null)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            //抓神榜
            R.id.btn_rankings_pick_god->{
                setBtnEnable(btn_rankings_pick_god)
                getMainRankingList("1",null,null)
            }
            //财富榜
            R.id.btn_rankings_pick_wealth->{
                setBtnEnable(btn_rankings_pick_wealth)
                getMainRankingList("2",null,null)
            }
            //周抓取榜
            R.id.btn_rankings_pick_week->{
                setBtnEnable(btn_rankings_pick_week)
                getMainRankingList("1",getWeekDate(true)+" 00:00:00",getWeekDate(false)+" 23:59:59")
            }
            //月抓取榜去
            R.id.btn_rankings_pick_month->{
                setBtnEnable(btn_rankings_pick_month)
                getMainRankingList("1",getMonthDate(true)+" 00:00:00",getMonthDate(false)+" 23:59:59")
            }
        }
    }

    //设置按钮是否可用
    fun setBtnEnable(v:View){
        btn_rankings_pick_god.isEnabled=true
        btn_rankings_pick_wealth.isEnabled=true
        btn_rankings_pick_week.isEnabled=true
        btn_rankings_pick_month.isEnabled=true
        v.isEnabled=false

    }

    override fun getMessage(bundle: Bundle) {
        var rjson = GsonUtil.jsonToObject(bundle.getString("allresult"), JsonObject::class.java)
        var result=bundle.getString("msg")
        when (bundle.getInt("what")) {
            GETMAINRANKINGLIST->{
                var dataList=GsonUtil.jsonToList(result,Rankings::class.java) as ArrayList<Rankings>

                //判断数据是否大于0
                if(dataList.size>0){
                //放置第一名
                Glide.with(this).load(dataList[0].headimgurl)
                        .listener(object : RequestListener<String, GlideDrawable> {
                            override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                civ_item_rankings_pic_f.setImageDrawable(resource)
                                return false
                            }
                        })
                        .into(civ_item_rankings_pic_f)
                civ_item_rankings_f_name.text=dataList[0].username
                if(!TextUtils.isEmpty(dataList[0].getnumber)){
                    civ_item_rankings_f_times.text=dataList[0].getnumber+"次｜神抓"
                }else{
                    civ_item_rankings_f_times.text=dataList[0].coins+"币"
                }
                dataList.removeAt(0)
                }

                //判断数据是否大于0
                if(dataList.size>0) {
                    //放置第二名
                    Glide.with(this).load(dataList[0].headimgurl)
                            .listener(object : RequestListener<String, GlideDrawable> {
                                override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                    civ_item_rankings_pic_s.setImageDrawable(resource)
                                    return false
                                }
                            })
                            .into(civ_item_rankings_pic_s)
                    civ_item_rankings_s_name.text = dataList[0].username
                    if (!TextUtils.isEmpty(dataList[0].getnumber)) {
                        civ_item_rankings_s_times.text = dataList[0].getnumber + "次｜神抓"
                    } else {
                        civ_item_rankings_s_times.text = dataList[0].coins + "币"
                    }
                    dataList.removeAt(0)
                }

                //判断数据是否大于0
                if(dataList.size>0) {
                    //放置第三名
                    Glide.with(this).load(dataList[0].headimgurl)
                            .listener(object : RequestListener<String, GlideDrawable> {
                                override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }

                                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                    civ_item_rankings_pic_t.setImageDrawable(resource)
                                    return false
                                }
                            })
                            .into(civ_item_rankings_pic_t)
                    civ_item_rankings_t_name.text = dataList[0].username
                    if (!TextUtils.isEmpty(dataList[0].getnumber)) {
                        civ_item_rankings_t_times.text = dataList[0].getnumber + "次｜神抓"
                    } else {
                        civ_item_rankings_t_times.text = dataList[0].coins + "币"
                    }

                    dataList.removeAt(0)
                }
                //判断数据是否大于0
                if(dataList.size>0) {
                    adapter_rankings = RankingsAdapter(this, dataList)
                    lv_rankings_info.adapter = adapter_rankings
                }
            }
        }
    }

    override fun backfinish(view: View) {
        super.backfinish(view)
    }

    //获取每周的开始结束日期
    fun getWeekDate(isStartdate:Boolean):String{
        val calendar = Calendar.getInstance()
        calendar.setFirstDayOfWeek(Calendar.MONDAY)
        //获取每周开始时间
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val sTime = calendar.time
        //获取每周结束时间
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val eTime = calendar.time
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        if (isStartdate){
            return sdf.format(sTime)
        }else{
            return sdf.format(eTime)
        }
    }

    //获取每月的开始结束日期
    fun getMonthDate(isStartdate:Boolean):String{
        val cal = Calendar.getInstance()
        //获取每月开始时间
        cal.set(Calendar.DAY_OF_MONTH, 1)
        var sTime=cal.time
        //获取每月结束时间
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        var eTime=cal.time

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        if (isStartdate){
            return sdf.format(sTime)
        }else{
            return sdf.format(eTime)
        }
    }


    //获取排行榜
    fun getMainRankingList(type:String,startdate:String?,enddate:String?){
        var param = HashMap<String, String>();
        param.put("userid", getLoginid() + "")
        param.put("sn", Constant.SN)
        param.put("type", type)
        if (!TextUtils.isEmpty(startdate)&&!TextUtils.isEmpty(enddate)){
            param.put("startdate", startdate!!)
            param.put("enddate", enddate!!)
        }
        param.put("sign", SignParamUtil.getSignStr(param))
        httpPost(Constant.SERVERIP + Constant.getmainrankinglist, param, GETMAINRANKINGLIST);
    }

}
