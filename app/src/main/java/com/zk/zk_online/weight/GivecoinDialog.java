package com.zk.zk_online.weight;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zk.zk_online.R;

/**
 * Created by Administrator on 2017/12/4.
 */

public class GivecoinDialog {

    private Dialog dialog;
    private View content;
    private Context context;
    private Button bt_commit;
    private TextView tv_coin;





    public GivecoinDialog(Context context)
    {
        this.context=context;
    }

    public GivecoinDialog createDialog()
    {
        dialog=new Dialog(context);
        content= LayoutInflater.from(context).inflate(R.layout.window_give_coin,null);
        bt_commit=content.findViewById(R.id.bt_commit);
        tv_coin=content.findViewById(R.id.tv_coin);
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return  this;
    }


    public GivecoinDialog setCoinText(String coins){
        tv_coin.setText("X"+coins+"游戏币");
        return  this;
    }

    public GivecoinDialog setCanclable(boolean canclable)
    {
        dialog.setCancelable(canclable);
        return this;
    }

    public void dismiss()
    {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public GivecoinDialog show()
    {
        dialog.setContentView(content);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return  this;
    }



}
