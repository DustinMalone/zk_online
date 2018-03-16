package com.zk.zk_online.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by ZYB on 2017/11/6 0006.
 */

public class MyImageView extends ImageView {


    //确认指令
    private String  ACTION_PICK="05";
    private OnImageViewClickListener listener;
    //指令
    private String code;
    //当前毫秒数
    private long currmill=0;

    //是否拦截事件
    private boolean interceptClick=false;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setOnImageViewClickListener(OnImageViewClickListener listener){
        this.listener=listener;
    }

    public void setCode(String code)
    {
        this.code=code;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (interceptClick)
        {
            return super.onTouchEvent(event);
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Log.i("code_down",code);
                if (listener!=null) {
                    currmill=System.currentTimeMillis();
                    listener.clickDown(code);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i("code_up",code);
                if (listener!=null) {
                    try {
                        //上下左右控制方向 延时10s才发送 实现甩尾?
                        long delay=System.currentTimeMillis()-currmill;
                        if (delay<10)
                            Thread.sleep(10-delay);
                        /*if (!code.equals(ACTION_PICK)) {
                            Thread.sleep(10-delay);
                        }*/
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    listener.clickUp(code);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setInterceptClick(boolean interceptClick) {
        this.interceptClick = interceptClick;
    }

    public interface OnImageViewClickListener
    {
        void clickDown(String code);
        void clickUp(String code);
    }
}
