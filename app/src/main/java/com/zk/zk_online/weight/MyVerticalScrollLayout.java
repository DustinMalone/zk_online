package com.zk.zk_online.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 自定义上下滑屏
 * Created by ZYB on 2017/11/7 0007.
 */

public class MyVerticalScrollLayout extends ViewGroup {


    //滑动对象
    private Scroller scroller;

    //父View高度
    private int layoutHeight = 0;


    //滑动的差值
    private int delay_height = 0;

    //上一次Y坐标的值
    private int lastY = 0;

    //第一次点击时Y的坐标
    private int firstY = 0;


    public MyVerticalScrollLayout(Context context) {
        super(context);
        scroller = new Scroller(context);
    }

    public MyVerticalScrollLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    public MyVerticalScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    public MyVerticalScrollLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        scroller = new Scroller(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        layoutHeight = this.getMeasuredHeight();
        Log.i("layoutHeight",layoutHeight+"");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录第一次按下的Y坐标
                firstY = (int) event.getY();
                lastY = firstY;
                break;
            case MotionEvent.ACTION_MOVE:
                delay_height = (int) event.getY() - lastY;
                startScroll(delay_height,0);
                lastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int scroY =scroller.getCurrY();
                delay_height = (int) event.getY() - firstY;
                if (Math.abs(delay_height) > layoutHeight/4) {
                    //当滑动差值大于半屏时
                    if (delay_height <= 0) {
                        //第一屏滑向第二屏
                        if (scroY>=0 && scroY<layoutHeight)
                        {
                            startScroll(scroY-layoutHeight,1000);
                        }
                    } else {
                        //第二屏滑向第一屏
                        if (scroY>0&& scroY<layoutHeight )
                        {
                            startScroll(scroY,1000);
                        }

                    }
                }
                //当滑动差值小于半屏时
                else {

                    if (delay_height < 0) {
                        //第一屏滑向上边界
                        if (scroY>=0 && scroY/2<=layoutHeight/2)
                        {
                            startScroll(scroY,1000);
                        }
                    } else {
                        //第二屏滑向下边界
                        if (scroY>layoutHeight/2 && scroY<=layoutHeight)
                        {
                            startScroll(scroY-layoutHeight,1000);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }


    public void startScroll(int y,int time) {
        int sX = scroller.getCurrX();
        int sY = scroller.getCurrY();
        Log.i("XYinfo", "sX:" + sX + " sY:" + sY);
        //当向下滑动时，控制不能滑出上边界
        if (y > 0) {
            //当sy小于0时 说明已经超出了上边界 ，要将view重新滑到上边界
            if (sY <= 0) {
                scroller.startScroll(sX, sY, 0, -sY, 300);
                invalidate();
                return;
            }
        }
        //当向上滑动时 不能超出下边界
        if (y < 0) {
            if (sY >= layoutHeight) {
                scroller.startScroll(sX, sY, 0, -(sY - layoutHeight), 300);
                invalidate();
                return;
            }
        }
        scroller.startScroll(sX, sY, 0, -y, time);
        invalidate();

    }


    //为子View定位
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            childView.layout(0, i * childHeight, childWidth, (i + 1) * childHeight);
        }
    }


    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 测量每一个子控件的大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //滑向第一屏
    public void scrollerToFirstIndex(){
        int sX = scroller.getCurrX();
        int sY = scroller.getCurrY();
        scroller.startScroll(sX, sY, 0, -layoutHeight, 1000);
        invalidate();

    }
}
