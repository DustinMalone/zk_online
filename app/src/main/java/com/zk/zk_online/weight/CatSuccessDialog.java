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
 * Created by ZYB on 2017/11/24 0024.
 */

public class CatSuccessDialog {
    private Dialog dialog;
    private CatchSuccessCallback catchSuccessCallback;
    private View content;
    private Context context;
    private Button bt_stop;
    private Button bt_again;
    private TextView tv_dialog_time;
    private ImageView img_git;
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
                catchSuccessCallback.timeout();
                dismiss();
            }
            bt_stop.setText("分享炫耀("+msg.what+"s)");
            tv_dialog_time.setText(msg.what+"");
        }
    };


    public CatSuccessDialog(Context context)
    {
        this.context=context;

    }

    public CatSuccessDialog createDialog()
    {
        dialog=new Dialog(context);
        content= LayoutInflater.from(context).inflate(R.layout.window_pick_success,null);
        bt_stop=content.findViewById(R.id.bt_stop);
        bt_again=content.findViewById(R.id.bt_again);
        tv_dialog_time=content.findViewById(R.id.tv_dialog_time);
        img_git=content.findViewById(R.id.img_git);
        tv_dialog_time.setText(time+"");
        bt_stop.setText("分享炫耀("+time+"s)");

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                catchSuccessCallback.stopclick();
                stopThread=true;
            }
        });

        bt_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                catchSuccessCallback.againclick();
                stopThread=true;

            }
        });

        dialog.setContentView(content);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return  this;
    }

    public CatSuccessDialog setCatchSuccessCallback(CatchSuccessCallback catchSuccessCallback){
        this.catchSuccessCallback=catchSuccessCallback;
        return  this;
    }




    public CatSuccessDialog setImagePath(String url)
    {
        Glide.with(context).load(url).into(img_git);
        return  this;
    }

    public CatSuccessDialog setCanclable(boolean canclable)
    {
        dialog.setCancelable(canclable);
        return this;
    }

    public void dismiss()
    {
        if (dialog.isShowing()) {
            dialog.dismiss();
            handler.removeMessages(time);
        }
    }

    public CatSuccessDialog show()
    {
        time=6;
        bt_stop.setText("分享炫耀("+time+"s)");
        stopThread=false;
        dialog.show();
        thread=new Thread(new CountdownRunnable());
        thread.start();
        return  this;
    }

    public interface  CatchSuccessCallback
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
