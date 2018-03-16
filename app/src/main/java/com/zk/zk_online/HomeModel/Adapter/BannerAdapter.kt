package com.zk.zk_online.HomeModel.Adapter


import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


/**
 * Created by Administrator on 2017/11/22.
 */
class BannerAdapter (var mcontext: Context, var imagelist:ArrayList<ImageView>) : PagerAdapter()
{

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`;
    }

    override fun getCount(): Int {
        return imagelist.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(imagelist[position])
        return imagelist.get(position)
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        return container.removeView(`object` as View)
    }



}