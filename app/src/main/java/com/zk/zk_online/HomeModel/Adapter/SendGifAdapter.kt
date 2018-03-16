package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.zk.zk_online.HomeModel.Model.SendData
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class SendGifAdapter(var mcontext: Context, var list: List<SendData>) :BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_send, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

//        holder.name.setText(list[position].name)
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
//          val img: ImageView = view.findViewById(R.id.img_item)
//        val name: TextView = view.findViewById(R.id.tv_item_name)
//        val img2: TextView = view.findViewById(R.id.tv_item_price)
    }
}