package com.zk.zk_online.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zk.zk_online.HomeModel.Model.Gitdata
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class ConsumeAdapter(var mcontext: Context, var list: List<Gitdata>) : BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_consume, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }
        /*holder.img.setImageBitmap(null);*/
//        holder.name.setText(list[position].name)
        holder.date.setText(list[position].createdate)
        holder.name.setText(list[position].goodname)
        holder.count.setText(list[position].type+list[position].coin+"游戏币")
        if (list[position].type=="+"){
            holder.count.setTextColor(ContextCompat.getColor(mcontext,R.color.color_39ff43))
        }else{
            holder.count.setTextColor(ContextCompat.getColor(mcontext,R.color.color_FFA54F))
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

    class ViewHolder(var view: View) {
         val date: TextView = view.findViewById(R.id.item_purchase_history_date)
       val name: TextView = view.findViewById(R.id.item_purchase_history_name)
       val count: TextView = view.findViewById(R.id.count)
    }
}