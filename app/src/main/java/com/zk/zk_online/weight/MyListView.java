package com.zk.zk_online.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 *
 * 监听滑到顶部 滑出一段距离的listview
 * Created by ZYB on 2017/12/2.
 */

public class MyListView extends ListView {

    //是否到达顶部
    private boolean isArriveTop=false;
    //是否第一次记录滑到顶部时的Y值
    private boolean arriveFlag=true;
    //第一次的Y值
    private int lastY;
    //初始化的Y值
    private int curY;
    //滑动距离的临界点
    private int slopValue;


    private ScrollIndexListener listener;


    public MyListView(Context context) {
        super(context);
        init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        slopValue=getMeasuredHeight()/3;
        super.onWindowFocusChanged(hasWindowFocus);

    }

    public void init(){
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState)
                {
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        if (getFirstVisiblePosition()==0)
                        {
                            isArriveTop=true;
                            arriveFlag=true;
                        }
                        else {
                            isArriveTop=false;
                            arriveFlag=false;
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    //设置监听器
    public void setScrollIndexListener(ScrollIndexListener listener)
    {
        this.listener=listener;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastY= (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当move滑到顶部的时候 取第一次move的值作为lastY
                if (isArriveTop)
                {
                    if (arriveFlag) {
                        lastY = (int) ev.getY();
                        arriveFlag = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //通知滑动
                if (ev.getY()-lastY>slopValue && isArriveTop)
                {
                    listener.srollerToFirstIndex();
                }
                break;
        }
        return  super.onTouchEvent(ev);
    }

    //可以滑到到第一屏的监听事件
    public interface ScrollIndexListener{
        void srollerToFirstIndex();
    }
}
