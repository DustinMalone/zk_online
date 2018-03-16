package com.zk.zk_online.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zk.zk_online.HomeModel.Model.SendData
import com.zk.zk_online.R





/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class SendGifAdapter(var mcontext: Context, var list: ArrayList<SendData>, var isChecklist: HashMap<Int,String>) :BaseAdapter()
{
    private var checkBoxListener:CheckBoxListener?=null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_send, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.name.setText(list[position].goodsname)
        holder.date.setText(list[position].createdate)

        Glide.with(mcontext)
                .load(list[position].pic)
                .placeholder(R.drawable.default_img)
                .into(holder.img);


//        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
////                list[position].isCheck=true
////                holder.checkbox.isChecked=true
////                isChecklist.put(position,list[position].machine_coin_id)
//                if (checkBoxListener!=null){
//                    checkBoxListener!!.check(buttonView,isChecked,position)
//                }
//            } else {
//                list[position].isCheck=false
//                holder.checkbox.isChecked=false
//                isChecklist.remove(position)
//            }
//
//
//        }


        holder.checkbox.setOnClickListener { view ->
            if (list[position].isCheck) {
                list[position].isCheck=false
                holder.checkbox.isChecked=false
                isChecklist.remove(position)

            } else {
                if (checkBoxListener!=null){
                    checkBoxListener!!.check(view,true,position)
                }
            }


        }


        if (list[position].isCheck){
            holder.checkbox.isChecked=true
        }else{
            holder.checkbox.isChecked=false
        }

//        holder.checkbox.isChecked=list[position].isCheck


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
          val img: ImageView = view.findViewById(R.id.iv_send_gift_pic)
        val name: TextView = view.findViewById(R.id.tv_send_gift_name)
        val date: TextView = view.findViewById(R.id.tv_send_gift_date)
        var checkbox:CheckBox=view.findViewById(R.id.cb_send_gift_selection)
    }

    interface CheckBoxListener{
        fun check(buttonview:View,isCheck:Boolean,position:Int)
    }

    fun setCheckBoxListener(checkBoxListener:CheckBoxListener){
        this.checkBoxListener=checkBoxListener
    }
}