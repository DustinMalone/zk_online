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
import android.widget.ListView;
import android.widget.TextView;

import com.zk.zk_online.HomeModel.Adapter.WindowGiftShopDetailAdapter;
import com.zk.zk_online.HomeModel.Model.GiftShopDetail;
import com.zk.zk_online.HomeModel.Model.WindowGiftShopDetail;
import com.zk.zk_online.HomeModel.View.GiftShopDetailActivity;
import com.zk.zk_online.R;

import java.util.ArrayList;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class GIftShopDetailDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private ConfirmListener listener;

    private ListView lv_window_gift_shop_detail;

    private Button btn_window_gift_shop_detail;

    private TextView tv_window_gift_shop_detail_selectcount;

    private WindowGiftShopDetailAdapter Adapter;

    private ArrayList<WindowGiftShopDetail> list;

    public GIftShopDetailDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public GIftShopDetailDialog create()
    {
        //初始化VIEW
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_gift_shop_detail,null);
        lv_window_gift_shop_detail=contentview.findViewById(R.id.lv_window_gift_shop_detail);
        btn_window_gift_shop_detail=contentview.findViewById(R.id.btn_window_gift_shop_detail);
        tv_window_gift_shop_detail_selectcount=contentview.findViewById(R.id.tv_window_gift_shop_detail_selectcount);

        tv_window_gift_shop_detail_selectcount.setText("我的娃娃(0/"+((GiftShopDetailActivity)mcontext).getExchangenum()+")");
        //设置点击事件
        btn_window_gift_shop_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onClick(Adapter.isCheckList());
                }
                dismiss();
            }
        });
        return this;
    }

    public GIftShopDetailDialog sendData(ArrayList<WindowGiftShopDetail> list,ArrayList<GiftShopDetail> isCheckList)
    {
        this.list=list;
        tv_window_gift_shop_detail_selectcount.setText("我的娃娃("+isCheckList.size()+"/"+((GiftShopDetailActivity)mcontext).getExchangenum()+")");

        for(GiftShopDetail gsd : isCheckList){
            for (int i=0;i<list.size();i++){
                if (gsd.getMachine_coin_id().equals(list.get(i).getMachine_coin_id())){
                    list.get(i).setIscheck(true);
                    break;
                }
            }
        }
        Adapter=new WindowGiftShopDetailAdapter(mcontext,list,isCheckList);
        lv_window_gift_shop_detail.setAdapter(Adapter);
        return this;
    }

    public GIftShopDetailDialog show()
    {
        if (dialog!=null && contentview!=null)
        {
            dialog.setContentView(contentview);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity= Gravity.CENTER;
            params.width= WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();


        }
        return  this;
    }


    public void dismiss()
    {
        if (dialog!=null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    public interface ConfirmListener{
        void onClick(ArrayList<GiftShopDetail> glist);
    }

    public GIftShopDetailDialog setConfirmListener(ConfirmListener l){
            this.listener=l;
            return  this;
    }


}
