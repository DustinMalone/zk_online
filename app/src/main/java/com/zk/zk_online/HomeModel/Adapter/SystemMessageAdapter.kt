package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zk.zk_online.HomeModel.Model.SystemMessage
import com.zk.zk_online.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class SystemMessageAdapter(var mcontext: Context, var list: ArrayList<SystemMessage>) : BaseAdapter()
{



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_system_message, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.tv_system_message_name.text=list[position].remark
        holder.tv_system_message_content.text=list[position].message
        holder.tv_system_message_time.text=getTime(list[position].createdate)
        return v
    }


    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    fun getTime(time:String):String{
        var curryTime = Calendar.getInstance().timeInMillis
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA)
        val date: Date
        var times: Long = curryTime
        try {
            date = sdr.parse(time)
            times = date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var dt=(curryTime- times)/1000
        var returnTimew =""
        if (dt/3600/24/30/365>0){
            returnTimew= (dt/3600/24/30/365).toString()+"年前"
        }
        else if (dt/3600/24/30>0){
            returnTimew= (dt/3600/24/30).toString()+"月前"
        }
        else if (dt/3600/24>0){
            returnTimew= (dt/3600/24).toString()+"天前"
        }
        else if (dt/3600>0){
            returnTimew= (dt/3600).toString()+"小时前"
        }else if(dt/60>0){
            returnTimew= (dt/60).toString()+"分钟前"
        }else {
            returnTimew= dt.toString()+"秒前"
        }

        return returnTimew

    }


    class ViewHolder(var view: View) {
        var tv_system_message_name: TextView =view.findViewById(R.id.tv_system_message_name)
        var tv_system_message_content: TextView =view.findViewById(R.id.tv_system_message_content)
        var tv_system_message_time: TextView =view.findViewById(R.id.tv_system_message_time)

    }
}