package com.zk.zk_online.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zk.zk_online.R;
import com.zk.zk_online.Utils.pxUtil;


/**
 *
 * 倒计时进度条类
 * Created by ZYB on 2017/11/30 0030.
 */

public class CountDownView extends View {

    private Paint mpaint;
    private int width;
    private int height;

    //读秒进度条位置
    private int black_index=0;
    //倒计时
    private int seconds=0;
    //记录最开始的倒计时
    private int init_seconds=0;
    //是否停止倒计时
    private boolean isStopThread=false;
    //圆形读秒刷新时间 实现顺畅刷新
    private int refreshtime;
    //读秒到期监听
    private TimeOverlistener timeOverlistener;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //刷新
            seconds=msg.what;
            postInvalidate();


            if (seconds==0)
            {
                timeOverlistener.timeover();
            }
        }
    };

    public CountDownView(Context context) {
        super(context);
        initPaint();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }



    //设置时间
    public void setSeconds(int seconds)
    {
        this.seconds=seconds;
        init_seconds=seconds;
        refreshtime=(seconds*1000)/360;



    }

    //设置到期时间监听
    public void setTimeOverListener(TimeOverlistener listener){
        timeOverlistener=listener;
    }

    //初始化画笔
    public void initPaint(){
        mpaint=new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setStyle(Paint.Style.FILL);
        mpaint.setColor(getResources().getColor(R.color.color_90000000));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        width=getMeasuredWidth();
        height=getMeasuredHeight();


        super.onDraw(canvas);

        RectF oval=new RectF();                     //RectF对象
        oval.left=5;                              //左边
        oval.top=5;                                   //上边
        oval.right=width-5;                             //右边
        oval.bottom=height-5;                       //下边

        //再画黄色圆圈
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setStrokeWidth(8f);
        mpaint.setColor(getResources().getColor(R.color.color_FFA54F));
        canvas.drawArc(oval, 0, 360, false, mpaint);

        //再画黑色的圆圈
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setColor(Color.BLACK);
        mpaint.setStrokeWidth(5f);
        canvas.drawArc(oval, -90, black_index, false, mpaint);

        //画原型透明背景
        mpaint.setStyle(Paint.Style.FILL);
        mpaint.setColor(getResources().getColor(R.color.color_90000000));
        canvas.drawCircle((float) width/2,(float) height/2,height/2-10,mpaint);

        //画倒计时文字 居中
        Rect targetRect = new Rect(0, 0, width, height);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.TRANSPARENT);
        mpaint.setColor(Color.WHITE);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setTextSize(pxUtil.sp2px(20,getContext()));
        mpaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawRect(targetRect, paint);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(seconds+"s", targetRect.centerX(), baseline+10, mpaint);

    }

    //停止倒计时
    public void stopcountdown(boolean isStopThread){
        this.isStopThread=isStopThread;
    }
    //开始倒计时
    public void startCountdown(){
        //刷新读秒文字
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (seconds>=0 && !isStopThread)
                {

                    Message message=Message.obtain();
                    message.what=seconds;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    seconds--;
                }

            }
        }).start();
        //刷新读秒圈
        new Thread(new Runnable() {
            @Override
            public void run() {
              /*  for (int i=0;i<=360;i++)
                {
                    black_index=i;
                    postInvalidate();
                    try {
                        Thread.sleep(refreshtime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
              int i=0;
              while(i<=360 && !isStopThread)
              {
                  black_index=i;
                  postInvalidate();
                  try {
                      Thread.sleep(refreshtime);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  i++;
              }
            }
        }).start();
    }
    //回到初始化
    public void reset()
    {
        seconds=init_seconds;
        black_index=0;
        isStopThread=false;
        initPaint();
        postInvalidate();
    }

    //时间到期监听器类
    public interface TimeOverlistener{
        void timeover();
    }
}
