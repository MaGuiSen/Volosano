package com.volosano;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import lib.widget.CircleProgressView;

public class MainActivity extends AppCompatActivity {
    private CircleProgressView mCircleBar;
    Timer timer = null;
    int progress = 0;
    int maxTime = 100;//10分钟 转化成秒
    int currTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleBar = (CircleProgressView) findViewById(R.id.circleProgressbar);
        mCircleBar.setProgress(0);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currTime += 1;
                Log.e("currTime",currTime+",,"+maxTime);
                progress = currTime;
                if(progress > 100){
                    timer.cancel();
                    timer = null;
                    currTime = 0;
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCircleBar.setProgress(progress);
                        }
                    });
                }
//                if(currTime >= maxTime){
//                    timer.cancel();
//                    timer = null;
//                    currTime = 0;
//                }else{
//                    progress = (int) (100 * (currTime / maxTime));
//                }

            }
        },1000, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
