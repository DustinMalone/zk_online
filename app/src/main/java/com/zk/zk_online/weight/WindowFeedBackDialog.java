package com.zk.zk_online.weight;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zk.zk_online.HomeModel.Adapter.WindowFeedbackAdapter;
import com.zk.zk_online.R;

import java.util.ArrayList;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class WindowFeedBackDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private ListView lv_feed_back;
    private ImageView iv_feed_back_finish;
    private itemOnClickListener listener;

    private WindowFeedbackAdapter Adapter;

    private ArrayList<String> list;

    public WindowFeedBackDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public WindowFeedBackDialog create()
    {
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_feed_back,null);
        lv_feed_back=contentview.findViewById(R.id.lv_feed_back);
        lv_feed_back.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.itemOnclick(list.get(position));
                dismiss();
            }
        });
        iv_feed_back_finish=contentview.findViewById(R.id.iv_feed_back_finish);
        iv_feed_back_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return this;
    }

    public WindowFeedBackDialog sendData(ArrayList<String> list)
    {
        this.list=list;
        this.list.add("其他");
        Adapter=new WindowFeedbackAdapter(mcontext,this.list);
        lv_feed_back.setAdapter(Adapter);
        return this;
    }

    public WindowFeedBackDialog show()
    {
        if (dialog!=null && contentview!=null)
        {
            dialog.setContentView(contentview);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity= Gravity.CENTER;
            params.width= WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        }
        return  this;
    }

    public WindowFeedBackDialog setItemlistener(itemOnClickListener listener)
    {
        this.listener=listener;
        return this;
    }

    public void dismiss()
    {
        if (dialog!=null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    public interface itemOnClickListener{
        void itemOnclick(String item);
    }



}
