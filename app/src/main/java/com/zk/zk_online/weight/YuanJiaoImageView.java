package com.zk.zk_online.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by LCB on 2017/12/5.
 */
public class YuanJiaoImageView extends android.support.v7.widget.AppCompatImageView {

    //圆角弧度
    private float[] rids = {18.0f,18.0f,18.0f,18.0f,0.0f,0.0f,0.0f,0.0f,};


    public YuanJiaoImageView(Context context) {
        super(context);
    }

    public YuanJiaoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YuanJiaoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        //绘制圆角imageview
        path.addRoundRect(new RectF(0,0,w,h),rids, Path.Direction.CW);
        canvas.clipPath(path);
//        Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        bitmapPaint.setColor(Color.TRANSPARENT); // 颜色随意，不要有透明度。
//        canvas.drawPath(path, bitmapPaint);
        super.onDraw(canvas);
    }

    //设置圆角弧度
    public void setRids(float lt,float rt,float lb,float rb) {
        rids= new float[]{lt, lt, rt,rt,lb,lb,rb,rb};
        invalidate();
    }
}