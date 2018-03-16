package com.zk.zk_online.Adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zk.zk_online.HomeModel.Model.Rankings
import com.zk.zk_online.R



/**
 * Created by mukun on 2017/11/5.
 */
class RankingsAdapter( var mContext: Context,  var list:ArrayList<Rankings>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mContext, R.layout.item_rankings, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.no.text=(position+4).toString()
        holder.name.text=list[position].username

        if(!TextUtils.isEmpty(list[position].getnumber)){
        holder.times.text=list[position].getnumber+"次神抓"
        }else{
            holder.times.text=list[position].coins+"币"
        }

        Glide.with(mContext)
                .load(list[position].headimgurl)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        holder.img.setImageDrawable(resource)
                        return false
                    }
                })
                .placeholder(R.drawable.default_img)
                .into(holder.img);
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
        val img: ImageView = view.findViewById(R.id.civ_item_rankings_pic)
        val name: TextView = view.findViewById(R.id.tv_item_rankings_name)
        val times: TextView = view.findViewById(R.id.tv_item_rankings_times)
        val no: TextView = view.findViewById(R.id.tv_item_rankings_no)
    }
}