package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zk.zk_online.HomeModel.Model.ChargeMoney
import com.zk.zk_online.R
import java.util.*

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class WindowChargeMoneyAdapter(var mcontext: Context, var list: ArrayList<ChargeMoney>) : BaseAdapter()
{



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_charge_money, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }
        holder.tv_item_charge_coin.text=list[position].goodcoins+"币"
        holder.tv_item_charge_money.text=list[position].goodsprice+" 元"

//        holder.tv_item_charge_coin.text=list[position].goodsname
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
        var tv_item_charge_coin: TextView =view.findViewById(R.id.tv_item_charge_coin)
        var tv_item_charge_money: TextView =view.findViewById(R.id.tv_item_charge_money)


    }
}