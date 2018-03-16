package com.zk.zk_online.HomeModel.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.zk.zk_online.HomeModel.Adapter.PlayInfoAdapter
import com.zk.zk_online.HomeModel.Model.Gitdata
import com.zk.zk_online.R
import com.zk.zk_online.base.BaseActivity

/**
 * Created by ZYB on 2017/11/8 0008.
 */
public class PlayInfoActivity : BaseActivity()
{

    var grid_view:GridView?=null;
    var adapter:PlayInfoAdapter?=null

    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_play_info)
        grid_view=findViewById(R.id.gridview)
        grid_view!!.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->

            Log.i("item_click","item_click")
            startActivity(Intent(applicationContext,PlayActivity::class.java))
        })
        setData()
    }

    override fun onClick(v: View?) {

    }

    override fun getMessage(bundle: Bundle) {

    }
    fun setData(){

        var list=ArrayList<Gitdata>()
        for (i in 1..6)
        {
            var item= Gitdata("墨镜超人","2017-12-12 08:08:08",
                    "10","-")
            list.add(item)
        }

        adapter= PlayInfoAdapter(this,list)
        grid_view!!.adapter=adapter
    }
}