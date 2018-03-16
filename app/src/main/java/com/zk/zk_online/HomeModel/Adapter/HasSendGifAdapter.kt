package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zk.zk_online.HomeModel.Model.HasSendData
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class HasSendGifAdapter(var mcontext: Context, var list: List<HasSendData>) : BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_has_send, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }
        holder.username.setText(list[position].deliveryname)
        holder.phone.setText(list[position].deliverytel)

        if (list[position].is_delivery==1){
        holder.order.setText(list[position].deliverycode)
        }else{
            holder.order.setText("")
        }

        holder.address.setText(list[position].province+list[position].city
                +list[position].district+list[position].deliveryaddress)

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
        val username: TextView = view.findViewById(R.id.tv_has_gift_username)
        val address: TextView = view.findViewById(R.id.tv_has_gift_address)
        val phone: TextView = view.findViewById(R.id.tv_has_gift_phone)
        val order: TextView = view.findViewById(R.id.tv_has_gift_order)
    }
}