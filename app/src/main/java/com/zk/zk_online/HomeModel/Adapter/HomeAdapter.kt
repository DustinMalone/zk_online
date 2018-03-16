package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zk.zk_online.HomeModel.Model.HomeBean
import com.zk.zk_online.R

/**
 * Created by mukun on 2017/11/5.
 */
class HomeAdapter(private var mContext: Context,private var list:List<HomeBean>) : BaseAdapter() {




    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mContext, R.layout.item_home_gr, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }
        /*holder.img.setImageBitmap(null);*/
        holder.name.setText(list[position].name)
        holder.msg.setText(list[position].money)
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
        val img: ImageView = view.findViewById(R.id.img_item)
        val name: TextView = view.findViewById(R.id.tv_item_name)
        val msg: TextView = view.findViewById(R.id.tv_item_price)
    }
}