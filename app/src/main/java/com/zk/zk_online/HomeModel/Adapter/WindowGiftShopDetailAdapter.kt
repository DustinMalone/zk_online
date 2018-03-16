package com.zk.zk_online.HomeModel.Adapter

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
import com.zk.zk_online.HomeModel.Model.GiftShopDetail
import com.zk.zk_online.HomeModel.Model.WindowGiftShopDetail
import com.zk.zk_online.HomeModel.View.GiftShopDetailActivity
import com.zk.zk_online.R
import java.lang.Exception
import java.util.*

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class WindowGiftShopDetailAdapter(var mcontext: Context, var list: ArrayList<WindowGiftShopDetail>, var isCheckList: ArrayList<GiftShopDetail>) : BaseAdapter()
{


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_window_gift_shop_detail, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder!!.tv_window_gift_shop_detail_giftname.text=list[position].goodsname



        Glide.with(mcontext).load(list[position].pic)
                .listener(object :RequestListener<String,GlideDrawable>{
                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        holder!!.iv_window_gift_shop_detail_giftimg.setImageDrawable(resource)
                        return false
                    }
                }).placeholder(R.drawable.default_img)
                .into(holder!!.iv_window_gift_shop_detail_giftimg)

        holder!!.iv_window_gift_shop_detail_sel.setOnClickListener {

            if (list[position].ischeck){
                list[position].ischeck=false
                //设置娃娃为没选中状态
                var i=0;
               for (obj in isCheckList){
                   if (obj.machine_coin_id.equals(list[position].machine_coin_id)){
                       isCheckList.removeAt(i)
                       break
                   }
                   i=i+1;
               }
            }else{
                if (isCheckList==null||isCheckList!!.size==(mcontext as GiftShopDetailActivity).exchangenum.toInt()){
                    (mcontext as GiftShopDetailActivity).SToast("已选择足够的娃娃数量!")
                    return@setOnClickListener
                }

                list[position].ischeck=true
                //设置娃娃为选中状态
                isCheckList.add(GiftShopDetail(list[position].goodsname,list[position].createdate
                        ,list[position].userid,list[position].pic,list[position].machine_coin_id))
            }
            notifyDataSetChanged()
        }


        if (list[position].ischeck){
            holder!!.iv_window_gift_shop_detail_sel.setImageResource(R.drawable.tike)
        }else{
            holder!!.iv_window_gift_shop_detail_sel.setImageResource(R.drawable.yuan)
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
        var tv_window_gift_shop_detail_giftname:TextView=view.findViewById(R.id.tv_window_gift_shop_detail_giftname)
        var iv_window_gift_shop_detail_giftimg:ImageView=view.findViewById(R.id.iv_window_gift_shop_detail_giftimg)
        var iv_window_gift_shop_detail_sel:ImageView=view.findViewById(R.id.iv_window_gift_shop_detail_sel)
    }
}