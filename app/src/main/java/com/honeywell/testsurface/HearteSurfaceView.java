package com.honeywell.testsurface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.LinkedList;

/**
 * Created by zhenliu on 2018/5/19.
 */

public class HearteSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHoler;
    private boolean isDrawing;
    private Canvas mCanvas;
    private Path path;
    private Paint mPaint;
    float[] x;
    LinkedList<Float> y = new LinkedList<>();
    private int mHeight;
    private int lines;
    private int baseline;
    private int squareLengh;

    public HearteSurfaceView(Context context) {
        this(context, null);
    }

    public HearteSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSurfaceHoler = getHolder();
        mSurfaceHoler.addCallback(this);
        this.setKeepScreenOn(true);
        mPaint = new Paint();
        mPaint.setColor(0xffff0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(3f);
        mPaint.setAntiAlias(true);
        for (int i = 0; i < 80; i++) {
            y.add((float) (getHeight() / 2));
        }
        path = new Path();
    }

    private void drawBackground(Canvas canvas) {
        mHeight = getHeight();
        squareLengh = mHeight / 40;//根据高度，画40根线
        Paint paint = new Paint();
        paint.setColor(0xffffc3c8);
        paint.setStyle(Paint.Style.STROKE);
        //画网格横线
        for (int i = 0; i < 41; i++) {
            if (i % 5 == 0) {
                paint.setStrokeWidth(3f);
            } else {
                paint.setStrokeWidth(1f);
            }
            canvas.drawLine(0, i * squareLengh, getWidth(), i * squareLengh, paint);
        }
        //基准线在控件的中间位置，就是20小格的位置
        baseline = 20 * squareLengh;

        //画竖线
        lines = getWidth() / squareLengh + 1;
        x = new float[lines];
        int sum = 0;
        for (int i = 0; i < lines; i++) {
            if (i % 5 == 0) {
                paint.setStrokeWidth(3f);
            } else {
                paint.setStrokeWidth(1f);
            }
            x[i] = i * squareLengh;
            canvas.drawLine(i * squareLengh, 0, i * squareLengh, mHeight, paint);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setData(Float data) {

        y.add(data);
        if (y.size() > 40) {
            y.pop();
        }

    }

    @Override
    public void run() {
        while (isDrawing) {
            try {
                //每隔100毫秒，画一次
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            drawing();
        }
    }

    private void drawing() {
        try {
            mCanvas = mSurfaceHoler.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            drawBackground(mCanvas);
            //设置起点
            path.moveTo(x[0], y.get(0) + baseline);
            for (int i = 0; i < y.size(); i++) {
                path.lineTo(x[i], y.get(i) + baseline);
            }
            mCanvas.drawPath(path, mPaint);
            //清理path的数据，
            path.reset();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHoler.unlockCanvasAndPost(mCanvas);
            }
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }
}
