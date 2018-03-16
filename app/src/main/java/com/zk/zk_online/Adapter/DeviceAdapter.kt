package com.zk.zk_online.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zk.zk_online.HomeModel.Model.DeviceBean
import com.zk.zk_online.R
import com.zk.zk_online.Utils.ScreenUtils


/**
 * Created by ZYB on 2017/11/8 0008.
 */

public class DeviceAdapter(var mcontext: Context, var list: List<DeviceBean>) : BaseAdapter()
{
    var wawaListener:WaWaDetailClickListener?=null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_play_info, null)
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


        if (list[position].is_offline)
        {
            holder.iv_device_status.setImageResource(R.drawable.hui)
        }
        else{
            if (list[position].lock.equals("0"))
            {
                holder.iv_device_status.setImageResource(R.drawable.lv)
            }
            else{

                holder.iv_device_status.setImageResource(R.drawable.hong)            }
        }

        //重设属性
        var lp=holder.linear_p.layoutParams
        lp.width=(ScreenUtils.getScreenWidth(mcontext)-60)/2
        lp.height= ((ScreenUtils.getScreenWidth(mcontext)-60)/2/2.7*3.7).toInt()
        holder.linear_p.layoutParams=lp

        if(list[position].is_test){
            holder.tv_devicecode.text=list[position].machinename+"(Test)"
        }else{
            holder.tv_devicecode.text=list[position].machinename
        }
        holder.tv_device_price.text=list[position].price_coin+"币/次"
        Glide.with(mcontext)
                .load(list[position].pic)
                .placeholder(R.drawable.default_img)
                .into(holder.img_gif);

        holder.tv_item_play_info_mygift.setOnClickListener({view->
            if (wawaListener!=null) {
                wawaListener!!.click(view,position)
            }
        })

        holder.tv_device_price.setOnClickListener({view->})

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

        var linear_p: LinearLayout =view.findViewById(R.id.linear_p)
        var img_gif: ImageView =view.findViewById(R.id.img_wawa)
        var tv_devicecode:TextView=view.findViewById(R.id.tv_devicecode);
        var iv_device_status:ImageView=view.findViewById(R.id.tv_device_status);
        var tv_device_price:TextView=view.findViewById(R.id.tv_device_price);
        var tv_item_play_info_mygift:TextView=view.findViewById(R.id.tv_item_play_info_mygift);
    }

    interface WaWaDetailClickListener{
        fun click(v:View,i: Int)
    }

    fun setWaWaDetailClickListener(listener:WaWaDetailClickListener){
        wawaListener=listener
    }
}