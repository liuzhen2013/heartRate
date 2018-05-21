package com.honeywell.testsurface;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.time.Clock;

public class MainActivity extends AppCompatActivity {

    float[] heartData = {-10f,0f,5f,0f,0f,-30f,15f,0f,-3f,0f,0f,0f,0f,0f};//模拟数据

    private HearteSurfaceView surfaceView;
    private int i;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            surfaceView.setData(heartData[i++%heartData.length]);
            sendEmptyMessageDelayed(0,60);
        }
    };

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int j = 0; j < heartData.length; j++) {
            heartData[j] = dp2px(heartData[j]);
        }
        surfaceView = (HearteSurfaceView) findViewById(R.id.surface);
        mHandler.sendEmptyMessage(0);


    }


}
