package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zk.zk_online.HomeModel.Model.GiftShopDetail
import com.zk.zk_online.R
import java.lang.Exception

/**
 * Created by ZYB on 2017/11/5 0005.
 */
public class GiftShopDetailAdapter(var mcontext: Context, var list: ArrayList<GiftShopDetail>) : RecyclerView.Adapter<GiftShopDetailAdapter.ViewHolder>()
{
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            Glide.with(mcontext).load(list[position].pic)
                    .listener(object :RequestListener<String,GlideDrawable>{
                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                           holder!!.iv_gift_shop_detail_giftimg.setImageDrawable(resource)
                            return false
                        }
                    })
                    .into(holder!!.iv_gift_shop_detail_giftimg)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):ViewHolder {
        var holder= ViewHolder(View.inflate(mcontext, R.layout.item_gift_shop_detail_giftimg, null))

        return holder
    }


    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view){
        var iv_gift_shop_detail_giftimg:ImageView=view!!.findViewById(R.id.iv_gift_shop_detail_giftimg)
    }
}