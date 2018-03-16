package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.zk.zk_online.HomeModel.Model.Andress
import com.zk.zk_online.HomeModel.View.AddressDetailActivity
import com.zk.zk_online.R
import com.zk.zk_online.weight.TisDialog


/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class AndressAdapter(var mcontext: Context, var list: ArrayList<Andress>,var onClickListener: View.OnClickListener) : BaseAdapter()
{
    companion object {
        //当前选中的坐标
        var curryPos=0
    }
    init {
        curryPos=0
    }

    var listener:DefaultAddressListener?=null
    //是否显示编辑，删除
    var isSendGift:Boolean=false


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_send_info, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

//        if (position==list.size){
//            holder.iv_add.visibility=View.VISIBLE
//            holder.ll_item_top.visibility=View.GONE
//
//            holder.iv_add.setOnClickListener(View.OnClickListener{
//                view->
//                var it= Intent(mcontext,AddressDetailActivity::class.java)
//                var bu=Bundle();
//                bu.putString("type","add")
//                bu.putSerializable("data", Andress())
//                it.putExtras(bu)
//                it.flags= Intent.FLAG_ACTIVITY_NEW_TASK
//                mcontext.startActivity(it)
//            })
//            return v
//        }

        holder.iv_add.visibility=View.GONE
        holder.ll_item_top.visibility=View.VISIBLE
        holder.tv_username.setText(list[position].username)
        holder.tv_usertel.setText(list[position].telphone)
        holder.tv_useraddress.setText("收货地址:"+list[position].province
                +list[position].city+list[position].district
                +list[position].address)


        //判断是否默认地址
        if (list[position].is_default=="1"){
            holder.tv_default.setTextColor(ContextCompat.getColor(mcontext, R.color.color_f05c28))
            holder.iv_btn.isSelected=true
        }else{
            holder.tv_default.setTextColor(ContextCompat.getColor(mcontext, R.color.black))
        }

        //判断是否选中
        if (list[position].isCheck=="1"){
            holder.iv_btn.isSelected=true
            setCurryPos(position)
        }else{
            holder.iv_btn.isSelected=false
        }

        //判断是否显示编辑删除
        if(isSendGift){
            holder.tv_edit.visibility=View.INVISIBLE
            holder.tv_del.visibility=View.INVISIBLE
        }else{
            holder.tv_edit.visibility=View.VISIBLE
            holder.tv_del.visibility=View.VISIBLE
        }

        holder.iv_btn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                for (i in list){
                   i.isCheck="0"
                }
                list[position].isCheck="1"
                setCurryPos(position)
                Log.e("position",curryPos.toString())
                notifyDataSetChanged()
            }
        })

        holder.tv_del.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                var dialog=TisDialog(mcontext).create()
                        .setMessage("是否删除？")
                        .setPositiveButton { v->  setCurryPos(position)
                            onClickListener.onClick(p0)}
                        .setNegativeButton { v-> }
                        .show()

            }
        })

        holder.tv_edit.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                var it= Intent(mcontext,AddressDetailActivity::class.java)
                var bu=Bundle();
                bu.putSerializable("data",list[position])
                bu.putString("type","edit")
                it.putExtras(bu)
                it.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                mcontext.startActivity(it)
            }
        })

        holder.iv_detail.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                var it= Intent(mcontext,AddressDetailActivity::class.java)
                var bu=Bundle();
                bu.putString("type","view")
                bu.putSerializable("data",list[position])
                it.putExtras(bu)
                it.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                mcontext.startActivity(it)

            }
        })

        holder.tv_default.setOnClickListener{
            view->
            if (listener!=null){
                setCurryPos(position)
                listener!!.onClick()
            }
        }





        return v
    }



    override fun getItem(p0: Int): Any {
//        if(p0==list.size){
//            return ""
//        }
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    interface DefaultAddressListener{
        fun onClick()
    }

    fun setDefaultAddressListener(l:DefaultAddressListener){
        listener=l
    }

    fun setIssSendGift(it:Boolean){
        isSendGift=it
    }

    fun setCurryPos(p0: Int) {
        curryPos=p0
    }

    fun getCurryPos(): Int {
        return curryPos
    }

    class ViewHolder(var view: View) {
        var iv_detail: ImageView =view.findViewById(R.id.iv_send_info_detail)
        var iv_btn: ImageView =view.findViewById(R.id.iv_send_info_xinzeng)
        var tv_username: TextView =view.findViewById(R.id.tv_send_info_username)
        var tv_usertel: TextView =view.findViewById(R.id.tv_send_info_usertel)
        var tv_useraddress: TextView =view.findViewById(R.id.tv_send_info_useraddress)
        var tv_default: TextView =view.findViewById(R.id.tv_send_info_defaultaddress)
        var tv_edit: TextView =view.findViewById(R.id.tv_send_info_edit)
        var tv_del: TextView =view.findViewById(R.id.tv_send_info_del)
        var iv_add: ImageView =view.findViewById(R.id.iv_item_sendinfo_bottom)
        var ll_item_top: LinearLayout =view.findViewById(R.id.ll_item_sendinfo_top)

    }
}