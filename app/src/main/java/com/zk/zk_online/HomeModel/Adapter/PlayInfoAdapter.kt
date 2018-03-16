package com.zk.zk_online.HomeModel.Adapter

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.zk.zk_online.HomeModel.Model.Gitdata
import com.zk.zk_online.R

/**
 * Created by ZYB on 2017/11/8 0008.
 */

public class PlayInfoAdapter(var mcontext: Context, var list: List<Gitdata>) : BaseAdapter()
{
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

        /*holder.img.setImageBitmap(null);*/
//        holder.name.setText(list[position].name)





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
        /*  val img: ImageView = view.findViewById(R.id.img_item)*/

        var linear_p:LinearLayout=view.findViewById(R.id.linear_p)
        var img_gif:ImageView=view.findViewById(R.id.img_gif)
    }
}