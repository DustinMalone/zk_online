package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/5 0005.
 */
public class DanMaKuAdapter(var mcontext: Context, var list: List<String>) : BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_danmu_text, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        if(list[position].indexOf(":") != -1)
        {
//            holder.tv_item_danmu_username.text=list[position].substring(0,list[position].indexOf(":")+1)
//            holder.tv_item_danmu.text=list[position].substring(list[position].indexOf(":")+1,list[position].length)

            var content=list[position].substring(0,list[position].indexOf(":")+1)+
                    "<font color=\"#FFC926\">"+list[position].substring(list[position].indexOf(":")+1,list[position].length)+"</font>"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tv_item_danmu.text= Html.fromHtml(content,Html.FROM_HTML_MODE_COMPACT)
            }else{
                holder.tv_item_danmu.text= Html.fromHtml(content)
            }

        }else{
            holder.tv_item_danmu.text=list[position]
        }

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

    fun updateData(newList:ArrayList<String>){
        list=newList
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) {

        val tv_item_danmu: TextView = view.findViewById(R.id.tv_item_danmu)
        val tv_item_danmu_username: TextView = view.findViewById(R.id.tv_item_danmu_username)

    }
}