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
import android.widget.GridView;
import android.widget.TextView;

import com.zk.zk_online.HomeModel.Adapter.WindowChargeMoneyAdapter;
import com.zk.zk_online.HomeModel.Model.ChargeMoney;
import com.zk.zk_online.R;

import java.util.ArrayList;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class ChargeMoneyDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private TextView tv_window_charge_money_usercoin;
    private GridView lv_window_charge_money;
    private itemOnClickListener listener;

    private WindowChargeMoneyAdapter Adapter;

    private ArrayList<ChargeMoney> list;

    public ChargeMoneyDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public ChargeMoneyDialog create()
    {
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_charge_money,null);
        lv_window_charge_money=contentview.findViewById(R.id.lv_window_charge_money);
        lv_window_charge_money.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.itemOnclick(list.get(position));
                dismiss();
            }
        });

        tv_window_charge_money_usercoin=contentview.findViewById(R.id.tv_window_charge_money_usercoin);

        return this;
    }

    public ChargeMoneyDialog sendData(ArrayList<ChargeMoney> list,String coin)
    {
        tv_window_charge_money_usercoin.setText(coin);
        this.list=list;
        Adapter=new WindowChargeMoneyAdapter(mcontext,list);
        lv_window_charge_money.setAdapter(Adapter);
        return this;
    }

    public ChargeMoneyDialog show()
    {
        if (dialog!=null && contentview!=null)
        {
            dialog.setContentView(contentview);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity= Gravity.CENTER;
            params.width= WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        }
        return  this;
    }

    public ChargeMoneyDialog setItemlistener(itemOnClickListener listener)
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
        void itemOnclick(ChargeMoney item);
    }



}
