package com.zk.lpw.base

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter



/**
 * Created by mukun on 2017/8/30.
 */
abstract class MyBaseAdapter<T>(internal var list: MutableList<T>?): BaseAdapter() {

    override abstract fun getView(p0: Int, p1: View?, p2: ViewGroup?): View

    override fun getItem(p0: Int): Any {
        if (list == null) return 0
        return list!!.size
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list!!.size
    }

    fun getList(): MutableList<T> {
        return list!!
    }

    /**
     * 重新设置数据，或者第一次设置数据
     * @param list
     */
    open fun setMyList(list: MutableList<T>?) {
        this.list = list!!
        notifyDataSetChanged()
    }

    /**
     * 往list后面添加数据，叠加到后面

     * @param list
     */
    open fun addList(list: MutableList<T>) {
        if (list.size == 0) {
            return
        }
        if (this.list == null) {
            this.list = ArrayList()
        }
        for (obj in list) {
            this.list!!.add(obj)
        }
        notifyDataSetChanged()
    }
}