package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zk.zk_online.HomeModel.Model.GiftShop
import com.zk.zk_online.R
import com.zk.zk_online.Utils.ScreenUtils
import com.zk.zk_online.weight.YuanJiaoImageView
import java.lang.Exception

/**
 * Created by ZYB on 2017/11/8 0008.
 */

public class GiftShopAdapter(var mcontext: Context, var list: List<GiftShop>) : BaseAdapter()
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_gift_shop, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        if (position%2==0){
            v.setPadding(20,0,0,0)
        }else{
            v.setPadding(0,0,20,0)
        }



        //重设属性
        var lp=holder.ll_gift_shop_linear.layoutParams
        lp.width=(ScreenUtils.getScreenWidth(mcontext) -60)/2
        lp.height= ((ScreenUtils.getScreenWidth(mcontext) -60)/2/2.7*3.7).toInt()
        holder.ll_gift_shop_linear.layoutParams=lp


        holder.tv_gift_shop_exchange_count.text="已兑换：人"
        holder.tv_gift_shop_gift_name.text=list[position].goodsname
        holder.tv_gift_shop_gift_price.text="￥"+list[position].goodsprice
        holder.tv_gift_shop_giftcount.text=list[position].exchangenum

        Glide.with(mcontext).load(list[position].pic)
                .listener(object :RequestListener<String,GlideDrawable>{
                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        holder!!.yjiv_gift_shop_giftimg.setImageDrawable(resource)
                        return false
                    }

                })
                .placeholder(R.drawable.default_img)
                .into(holder!!.yjiv_gift_shop_giftimg)

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

        var ll_gift_shop_linear: LinearLayout =view.findViewById(R.id.ll_gift_shop_linear)
        var tv_gift_shop_exchange_count: TextView =view.findViewById(R.id.tv_gift_shop_exchange_count)
        var tv_gift_shop_giftcount: TextView =view.findViewById(R.id.tv_gift_shop_giftcount);
        var tv_gift_shop_gift_name: TextView =view.findViewById(R.id.tv_gift_shop_gift_name);
        var tv_gift_shop_gift_price: TextView =view.findViewById(R.id.tv_gift_shop_gift_price);
        var yjiv_gift_shop_giftimg:YuanJiaoImageView=view.findViewById(R.id.yjiv_gift_shop_giftimg)
    }

}