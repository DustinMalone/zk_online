package com.zk.zk_online.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.zk.zk_online.R;
import com.zk.zk_online.Utils.SomeUtil;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class MessagePopwindow {
    private PopupWindow pop_window;
    private Context mcontext;
    private View view;
    private View content;
    private EditText ed_message;
    private Button bt_send_message;

    public MessagePopwindow(Context context, View v) {
        this.view = v;
        this.mcontext = context;
    }

    @SuppressLint("ResourceAsColor")
    public MessagePopwindow create() {
        content= LayoutInflater.from(mcontext).inflate(R.layout.window_message,null);
        ed_message=content.findViewById(R.id.ed_message);
        bt_send_message=content.findViewById(R.id.bt_send_message);

        pop_window=new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        pop_window.setBackgroundDrawable(new ColorDrawable(R.color.white));
        return  this;
    }

    public MessagePopwindow setButtonClick(final OnButtonClicklistener onButtonClicklistener)
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
                    onButtonClicklistener.sendMessage(msg);
                    pop_window.dismiss();

                }
            }
        });
        return this;
    }

    public void show()
    {
        pop_window.showAtLocation(view, Gravity.BOTTOM,0,0);

    }

    public interface  OnButtonClicklistener
    {
        void sendMessage(String msg);
    }
}
