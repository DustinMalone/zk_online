package com.zk.zk_online.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zk.zk_online.HomeModel.Model.ProductDetail
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/6 0006.
 */
public class ProductAdapter(var mcontext: Context, var list: List<ProductDetail>) : BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(mcontext, R.layout.item_product, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        Glide.with(mcontext)
                .load(list[position].pic)
                .placeholder(R.drawable.wawa_a)
                .into(holder.img);

        holder.name.setText(list[position].goodsname)
        holder.num.setText("x"+list[position].goodsnum)
        holder.index.setText("编号："+list[position].deliveryid)

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
       val img: ImageView = view.findViewById(R.id.iv_product_detail_pic)
      val name: TextView = view.findViewById(R.id.tv_product_detail_goodsname)
       val num: TextView = view.findViewById(R.id.tv_product_detail_goodsnum)
        val index: TextView = view.findViewById(R.id.tv_product_detail_goodsindex)
    }
}