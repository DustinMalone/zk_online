package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zk.zk_online.HomeModel.Model.RoomUserInfo
import com.zk.zk_online.R
import com.zk.zk_online.weight.circleImageView
import java.lang.Exception

/**
 * Created by ZYB on 2017/11/5 0005.
 */
public class PlayPicAdapter(var mcontext: Context, var list: List<RoomUserInfo>) : RecyclerView.Adapter<PlayPicAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var holder=ViewHolder(View.inflate(mcontext, R.layout.item_playpic_text, null))

        return holder

    }

    override fun getItemCount(): Int {
        if (list.size>3){
            return 3
        }

        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
       Glide.with(mcontext)
               .load(list[position].headimgurl)
               .placeholder(R.drawable.default_img)
               .listener(object :RequestListener<String,GlideDrawable>{
                   override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                       return false
                   }

                   override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    holder!!.cv_play_rbpic.setImageDrawable(resource)
                       return false
                   }
               })
               .into(holder!!.cv_play_rbpic)


    }


    class ViewHolder( itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val cv_play_rbpic: circleImageView = itemView!!.findViewById(R.id.cv_play_rbpic)

    }


}

