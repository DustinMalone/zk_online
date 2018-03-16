package com.zk.zk_online.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.zk.zk_online.HomeModel.View.UserInviteCodeActivity;
import com.zk.zk_online.R;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class MoneyNotEnoughDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private Button  btn_tis_money_share;

    private Button  btn_tis_money_chongzhi;

    private Button  btn_tis_money_share_grey;

    private ImageView iv_item_tis_money_finish;

    private ChongzhiListener chongzhiListener;


    public MoneyNotEnoughDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public MoneyNotEnoughDialog create()
    {
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_tis_money,null);
        btn_tis_money_share=contentview.findViewById(R.id.btn_tis_money_share);
        btn_tis_money_chongzhi=contentview.findViewById(R.id.btn_tis_money_chongzhi);
        iv_item_tis_money_finish=contentview.findViewById(R.id.iv_item_tis_money_finish);
        btn_tis_money_share_grey=contentview.findViewById(R.id.btn_tis_money_share_grey);

        btn_tis_money_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mcontext.startActivity(new Intent(mcontext, UserInviteCodeActivity.class));
                dismiss();
            }
        });

        btn_tis_money_chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chongzhiListener!=null){
                chongzhiListener.onClick(v);
                }
                dismiss();
            }
        });

        iv_item_tis_money_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return this;
    }



    public MoneyNotEnoughDialog show()
    {
        if (dialog!=null && contentview!=null)
        {
            dialog.setContentView(contentview);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity= Gravity.CENTER;
            params.width= WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.setCancelable(false);
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    public interface ChongzhiListener{
        void onClick(View v);
    }

    public MoneyNotEnoughDialog setChongzhiListener(ChongzhiListener listener){
        this.chongzhiListener=listener;
        return  this;
    }


    public MoneyNotEnoughDialog setISShare(Boolean isShare){
        if(!isShare){
            btn_tis_money_share_grey.setVisibility(View.VISIBLE);
            btn_tis_money_share.setVisibility(View.GONE);
        }
        return  this;
    }

}
