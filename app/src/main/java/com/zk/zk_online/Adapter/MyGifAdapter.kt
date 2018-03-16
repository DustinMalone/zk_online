package com.zk.zk_online.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zk.zk_online.HomeModel.Model.CatchRankBean
import com.zk.zk_online.R
import java.lang.Exception

/**
 * Created by ZYB on 2017/11/5 0005.
 */
public class MyGifAdapter(var mcontext:Context, var list: List<CatchRankBean>) :BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_my_gif, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.name.setText(list[position].username)
        holder.count.setText("抓中"+list[position].count+"次")
        Glide.with(mcontext)
                .load(list[position].headimgurl)
                .placeholder(R.drawable.default_img)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        holder!!.img.setImageDrawable(resource)
                        return false
                    }
                })
                .into(holder.img)

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
        var img: ImageView = view.findViewById(R.id.iv_play_goodspic)
        var name: TextView = view.findViewById(R.id.tv_item_name)
        var count:TextView=view.findViewById(R.id.tv_item_count);


    }
}