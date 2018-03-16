package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zk.zk_online.HomeModel.Model.UserAdvice
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class UserAdviceAdapter(var mcontext: Context, var list: ArrayList<UserAdvice>) : BaseAdapter()
{


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_user_advice, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.tv_item_user_advice_name.text="机台名称 : "+list[position].machinename
        holder.tv_item_user_advice_date.text="时间 : "+list[position].createdate
        holder.tv_item_user_advice_problem.text="反馈问题 : "+list[position].content
        holder.tv_item_user_advice_result.text="处理结果 : "+list[position].deal_result
        holder.tv_item_user_advice_handle_date.text="处理时间 : "+list[position].deal_time

        if (list[position].status==1){
            holder.iv_user_advice_status.visibility=View.VISIBLE
        }else{
            holder.iv_user_advice_status.visibility=View.INVISIBLE
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

        var tv_item_user_advice_date: TextView =view.findViewById(R.id.tv_item_user_advice_date)
        var tv_item_user_advice_name: TextView =view.findViewById(R.id.tv_item_user_advice_name)
        var tv_item_user_advice_problem: TextView =view.findViewById(R.id.tv_item_user_advice_problem)
        var tv_item_user_advice_handle_date: TextView =view.findViewById(R.id.tv_item_user_advice_handle_date)
        var tv_item_user_advice_result: TextView =view.findViewById(R.id.tv_item_user_advice_result)
        var iv_user_advice_status: ImageView =view.findViewById(R.id.iv_user_advice_status)


    }
}