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
import android.widget.TextView;

import com.zk.zk_online.R;
/**
 * Created by ZYB on 2017/11/15 0015.
 */

public class CatchFailedDialog  {

    private Dialog dialog;
    private CatchFailCallback catchFailCallback;
    private View content;
    private Context context;
    private Button bt_stop;
    private Button bt_again;
    private TextView tv_dialog_time;
    //创建一个倒计时线程
    private Thread thread;
    //倒计时
    private int time = 6;
    //停止倒计时线程
    private boolean stopThread=false;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what==0)
            {
                catchFailCallback.timeout();
                dismiss();
            }
            tv_dialog_time.setText(msg.what+"");
            bt_again.setText("继续战斗("+msg.what+"s)");
        }
    };


    public CatchFailedDialog(Context context)
    {
        this.context=context;
    }
    //初始化dialog布局
    public CatchFailedDialog createDialog()
    {
        dialog=new Dialog(context);
        content= LayoutInflater.from(context).inflate(R.layout.window_pick_failed,null);
        bt_stop=content.findViewById(R.id.bt_stop);
        bt_again=content.findViewById(R.id.bt_again);
        tv_dialog_time=content.findViewById(R.id.tv_dialog_time);
        tv_dialog_time.setText(time+"");
        bt_again.setText("继续战斗("+time+"s)");

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                catchFailCallback.stopclick();
                stopThread=true;
            }
        });

        bt_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                catchFailCallback.againclick();
                stopThread=true;

            }
        });

        dialog.setContentView(content);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return  this;
    }


    //设置监听器
    public CatchFailedDialog setCatchFailCallback(CatchFailCallback catchFailCallback){
        this.catchFailCallback=catchFailCallback;
        return  this;
    }
    //设置dialog能否被返回键dismiss
    public CatchFailedDialog setCanclable(boolean canclable)
    {
        dialog.setCancelable(canclable);
        return this;
    }
    //弹窗消失
    public void dismiss()
    {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    //显示弹窗
    public CatchFailedDialog show()
    {
        time=6;
        bt_again.setText("继续战斗("+time+"s)");
        stopThread=false;
        dialog.show();
        thread=new Thread(new CountdownRunnable());
        thread.start();
        return  this;
    }


    //监听类
    public interface  CatchFailCallback
    {
        void stopclick();
        void againclick();
        void timeout();
    }
    //倒计时线程执行类
    class CountdownRunnable implements  Runnable{
        @Override
        public void run() {
            while (time>=0 && !stopThread) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = Message.obtain();
                msg.what = time;
                handler.sendMessage(msg);
                time--;
            }
        }
    }


}
