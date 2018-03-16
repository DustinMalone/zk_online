package com.zk.zk_online.weight;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.zk.zk_online.HomeModel.Adapter.WindowFindAaddressAdapter;
import com.zk.zk_online.R;

import java.util.ArrayList;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class FindAddressDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private WheelView lv_find_address;
    private itemOnClickListener listener;
    private Button btn_address_yes;
    private Button btn_address_no;

    private WindowFindAaddressAdapter Adapter;

    private ArrayList<String> list;

    public FindAddressDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public FindAddressDialog create()
    {
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_find_address,null);
        lv_find_address=contentview.findViewById(R.id.wv_andress);
        btn_address_yes=contentview.findViewById(R.id.btn_address_yes);
        btn_address_no=contentview.findViewById(R.id.btn_address_no);

        btn_address_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size()!=0) {
                    listener.itemOnclick(list.get(lv_find_address.getCurrentItem()).toString());
                }
                dialog.dismiss();
            }
        });

        btn_address_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        return this;
    }

    public FindAddressDialog sendData(ArrayList<String> list)
    {
        this.list=list;
        Adapter=new WindowFindAaddressAdapter(mcontext,list);

        ArrayWheelAdapter arrayAdapter = new ArrayWheelAdapter(list,list.size());
        lv_find_address.setAdapter(arrayAdapter);
        return this;
    }

    public FindAddressDialog show()
    {
        if (dialog!=null && contentview!=null)
        {
            dialog.setContentView(contentview);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity= Gravity.BOTTOM;
            params.width= WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        }
        return  this;
    }

    public FindAddressDialog setItemlistener(itemOnClickListener listener)
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
