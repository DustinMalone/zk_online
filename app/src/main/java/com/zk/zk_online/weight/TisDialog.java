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
import android.widget.TextView;

import com.zk.zk_online.R;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class TisDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private TextView tv_window_tis;

    private Button  btn_tis_yes;

    private Button  btn_tis_no;

    public TisDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public TisDialog create()
    {
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_tis,null);
        tv_window_tis=contentview.findViewById(R.id.tv_window_tis);
        btn_tis_yes=contentview.findViewById(R.id.btn_tis_yes);
        btn_tis_no=contentview.findViewById(R.id.btn_tis_no);
        return this;
    }


    public TisDialog setPositiveButton(final PositiveButtonListener listener)
    {
        btn_tis_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    listener.onClick(v);
                    dismiss();
            }
        });
        return this;
    }


    public TisDialog setNegativeButton(final NegativeButtonListener listener)
    {
        btn_tis_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }


    public TisDialog setMessage(String message)
    {
        tv_window_tis.setText(message);
        return this;
    }

    public TisDialog setPosiButtonVisibility(int visibility)
    {
        btn_tis_yes.setVisibility(visibility);
        return this;
    }

    public TisDialog setNegativeButtonVisibility(int visibility)
    {
        btn_tis_no.setVisibility(visibility);
        return this;
    }

    public TisDialog show()
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

    public interface  NegativeButtonListener
    {
        void onClick(View v);
    }


    public interface  PositiveButtonListener
    {
        void onClick(View v);
    }
}
