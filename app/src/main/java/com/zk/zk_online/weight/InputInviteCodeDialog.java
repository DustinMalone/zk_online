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

public class InputInviteCodeDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private TextView et_window_invite_code;

    private TextView tv_window_inputcode_content;

    private Button  btn_inputcode_dialog_yes;

    private Button  btn_inputcode_dialog_no;

    public InputInviteCodeDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public InputInviteCodeDialog create()
    {
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_inputcode_dialog,null);
        tv_window_inputcode_content=contentview.findViewById(R.id.tv_window_inputcode_content);
        et_window_invite_code=contentview.findViewById(R.id.et_window_invite_code);
        btn_inputcode_dialog_yes=contentview.findViewById(R.id.btn_inputcode_dialog_yes);
        btn_inputcode_dialog_no=contentview.findViewById(R.id.btn_inputcode_dialog_no);
        return this;
    }


    public InputInviteCodeDialog setPositiveButton(final PositiveButtonListener listener)
    {
        btn_inputcode_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    listener.onClick(v,et_window_invite_code.getText().toString());
//                    dismiss();
            }
        });
        return this;
    }


    public InputInviteCodeDialog setNegativeButton(final NegativeButtonListener listener)
    {
        btn_inputcode_dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }


    public InputInviteCodeDialog setMessage(String message)
    {
        tv_window_inputcode_content.setText(message);
        return this;
    }

    public InputInviteCodeDialog show()
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
        void onClick(View v,String s);
    }
}
