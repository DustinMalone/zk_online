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
import android.widget.EditText;
import android.widget.Toast;

import com.zk.zk_online.R;
import com.zk.zk_online.Utils.SomeUtil;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class MessageDialog {

    private Dialog dialog;
    private View contentview;
    private Context mcontext;

    private EditText ed_message;
    private Button  bt_send_message;

    public MessageDialog(Context context)
    {
        mcontext=context;
        dialog=new Dialog(mcontext);
    }

    public MessageDialog create()
    {
        contentview= LayoutInflater.from(mcontext).inflate(R.layout.window_message,null);
        ed_message=contentview.findViewById(R.id.ed_message);
        bt_send_message=contentview.findViewById(R.id.bt_send_message);
        return this;
    }

    public MessageDialog sendMessage(final OnsendMessageListener listener)
    {
        bt_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=ed_message.getText().toString();
                if (SomeUtil.TextIsEmpey(msg))
                {
                    Toast.makeText(mcontext,"不能为空",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listener.sendMessage(msg);
                    dismiss();
                }
            }
        });
        return this;
    }

    public MessageDialog show()
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

    public void dismiss()
    {
        if (dialog!=null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }




    public interface  OnsendMessageListener
    {
        void sendMessage(String msg);
    }
}
