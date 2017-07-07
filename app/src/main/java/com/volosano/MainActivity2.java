package com.volosano;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import lib.widget.CircleProgressView;
import lib.widget.WheelView;

public class MainActivity2 extends AppCompatActivity {
    private CircleProgressView mCircleBar;
    Timer timer = null;
    int progress = 0;
    int maxTime = 100;//10分钟 转化成秒
    int currTime = 0;
    SeekBar mSeekBarDef;
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        initTimer();
        initWheelView();

        initVoice();
    }

    public  void initVoice(){
//        //音量控制,初始化定义
//        final AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        //最大音量
//        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        //当前音量
//        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        mSeekBarDef = (SeekBar) findViewById(R.id.seekBar);
//        mSeekBarDef.setMax(maxVolume);
//        mSeekBarDef.setProgress(currentVolume);
//        mSeekBarDef.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                int p = seekBar.getProgress();
//                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p, 0); //tempVolume:音量绝对值
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    public void initTimer(){
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

    public void initWheelView(){
        WheelView wva = (WheelView) findViewById(R.id.WheelView);

        wva.setOffset(1);
        wva.setItems(Arrays.asList(PLANETS));
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("selectedIndex: ", selectedIndex + ", item: " + item);
            }
        });
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
