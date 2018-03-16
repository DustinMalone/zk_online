package com.zk.zk_online.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.zk.zk_online.R;


import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 *
 * 准备中弹窗
 * Created by Administrator on 2017/12/9.
 */

public class ReadingDialog {

    private Context context;
    private GifDrawable reading_gifdrawable;
    private GifImageView gif_ready;
    private View content_view;
    private Dialog dialog;
    public ReadingDialog(Context context) throws IOException {
        this.context = context;
        dialog=new Dialog(context);
        content_view= LayoutInflater.from(context).inflate(R.layout.window_ready,null);
        gif_ready=content_view.findViewById(R.id.img_ready);

    }

    public ReadingDialog show(){
        try {
            reading_gifdrawable=new GifDrawable(context.getResources(),R.drawable.ready_game);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gif_ready.setImageDrawable(reading_gifdrawable);
        dialog.setContentView(content_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        return  this;
    }

    public void dismiss(){
        if (dialog!=null && dialog.isShowing())
        {
            //回收Gif资源
            reading_gifdrawable.recycle();
            dialog.dismiss();
        }
    }
}
