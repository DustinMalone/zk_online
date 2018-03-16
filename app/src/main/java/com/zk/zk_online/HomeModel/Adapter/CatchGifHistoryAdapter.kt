package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zk.zk_online.HomeModel.Model.CatchGifHistory
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class CatchGifHistoryAdapter(var mcontext: Context, var list: List<CatchGifHistory>) : BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_catch_gif_history, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        Glide.with(mcontext)
                .load(list[position].pic)
                .placeholder(R.drawable.default_img)
                .into(holder.iv_pic);


        holder.tv_goods_name.setText(list[position].goodsname)
        holder.tv_date.setText(list[position].createdate)

        if (list[position].isget==1) {
            holder.tv_catch_type.setText("抓取成功")
            holder.tv_catch_type.setTextColor(ContextCompat.getColor(mcontext,R.color.color_39ff43))
        }else{
            holder.tv_catch_type.setText("抓取失败")
            holder.tv_catch_type.setTextColor(ContextCompat.getColor(mcontext,R.color.color_FFA54F))

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
        var iv_pic:ImageView=view.findViewById(R.id.iv_catch_pic)
        var tv_goods_name:TextView=view.findViewById(R.id.tv_catch_history_name)
        var tv_date:TextView=view.findViewById(R.id.tv_catch_history_date)
        var tv_catch_type:TextView=view.findViewById(R.id.tv_catch_count)

    }
}