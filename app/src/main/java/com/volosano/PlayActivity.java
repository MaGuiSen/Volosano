package com.volosano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volosano.modal.GroupSetting;
import com.volosano.modal.PointSetting;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.widget.CircleProgressView;
import lib.widget.WareView;

public class PlayActivity extends AppCompatActivity {
    private static final int[] BodyImages = {R.mipmap.icon_body_part_neck,R.mipmap.icon_body_part_shoulder,R.mipmap.icon_body_part_low_back};
    @Bind(R.id.img_part)
    ImageView imgPart;
    @Bind(R.id.ware_view)
    WareView wareView;
    @Bind(R.id.txt_first_time)
    TextView txtFirstTime;
    @Bind(R.id.progress_first)
    CircleProgressView progressFirst;
    @Bind(R.id.img_play)
    ImageView imgPlay;
    @Bind(R.id.txt_second_time)
    TextView txtSecondTime;
    @Bind(R.id.txt_part)
    TextView txt_part;
    @Bind(R.id.progress_second)
    CircleProgressView progressSecond;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    String currPoint = "";
    PointSetting currPointSetting = null;
    GroupSetting group1 = null;
    GroupSetting group2 = null;
    boolean isPlay = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //一秒过去啦
            if(group1.isEnable() && group2.isEnable()){
                bothUse();
            }else if(group1.isEnable() && !group2.isEnable()){
                group1Use();
            }else if(!group1.isEnable() && group2.isEnable()){
                group2Use();
            }
            handler.sendEmptyMessageDelayed(100,1000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        initPlayer();
        currPoint = getIntent().getStringExtra("currPoint");
        txt_part.setText(currPoint);
        if("Neck".equals(currPoint)){
            imgPart.setImageResource(BodyImages[0]);
        }else if("Shoulder".equals(currPoint)){
            imgPart.setImageResource(BodyImages[1]);
        }else if("Low Back".equals(currPoint)){
            imgPart.setImageResource(BodyImages[2]);
        }
        currPointSetting = MyApplication.currSetting;
        group1 = currPointSetting.getGroupSetting1();
        group2 = currPointSetting.getGroupSetting2();
        setVoice(currPointSetting.getIntensity());
        setCircleProgress();
    }

    private void setCircleProgress() {
        int hour = group1.getHour();
        int minute = group1.getMinute();
        int timeLong = group1.getTimeLong();
        boolean firstEnable = group1.isEnable();
        String timeStatus = hour > 12 ? "pm" : "am";
        String minuteString = (minute < 10 ? "0":"") + minute;
        txtFirstTime.setText(hour+":"+minuteString+timeStatus);
        progressFirst.setProgress(0, timeLong+"min");
        progressFirst.setMaxProgress(timeLong*60);
        //0xffffffff 0xffF4BB1B
        txtFirstTime.setTextColor(0xffffffff);
        progressFirst.setProgressColor(0xffffffff);


        int hour2 = group2.getHour();
        int minute2 = group2.getMinute();
        int timeLong2 = group2.getTimeLong();
        String timeStatus2 = hour > 12 ? "pm" : "am";
        String minuteString2 = (minute2 < 10 ? "0":"") + minute2;
        txtSecondTime.setText(hour2+":"+minuteString2+timeStatus2);
        txtSecondTime.setTextColor(0xffffffff);
        progressSecond.setProgress(0, timeLong2+"min");
        progressSecond.setMaxProgress(timeLong2*60);
        progressSecond.setProgressColor(0xffffffff);
    }

    private void setVoice(int voice) {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voice, 0); //tempVolume:音量绝对值
    }

    @OnClick({R.id.img_back, R.id.img_msg, R.id.img_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_msg:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.img_play:
                isPlay = !isPlay;
                runProgress();
                try{
                    if(isPlay){
//                        //开启音乐
//                        startPlay();
//                        //开启波形图
//                        wareView.start();
//                        //变化为暂停键
//                        imgPlay.setImageResource(R.mipmap.icon_stop);
                        isTimeout = true;
                    }else{
                        isTimeout = false;
                        //暂停音乐
                        pausePlay();
                        //停止波形图
                        wareView.pause();
                        //变化为开始键
                        imgPlay.setImageResource(R.mipmap.icon_start_orange);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(isPlay){
            showAlertDialog();
        }else {
            super.onBackPressed();
        }
    }

    AlertDialog alertDialog = null;
    public void showAlertDialog(){
        if(alertDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("System is running, if goBack,the system will stop.").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            }).setPositiveButton("GoBack", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //暂停音乐
                    stopPlay();
                    //停止波形图
                    wareView.pause();
                    //变化为开始键
                    imgPlay.setImageResource(R.mipmap.icon_start_orange);
                    stopProgress();
                    finish();
                }
            });
            alertDialog = builder.create();
        }
        alertDialog.show();
    }

    MediaPlayer player = null;
    public void initPlayer(){
        player = MediaPlayer.create(this, R.raw.bg);
        player.setLooping(true);
    }

    public void startPlay(){
        try {
            if(player == null){
                initPlayer();
            }
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopPlay(){
        try {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pausePlay(){
        try {
            if(player.isPlaying()) {
                player.pause();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Timer progressTimer = null;
    int progress1 = 0;
    int progress2 = 0;

    public void stopProgress(){
        if(progressTimer != null){
            progressTimer.cancel();
            progressTimer = null;
        }
        progress1 = 0;
        progress2 = 0;
    }

    boolean isTimeout = false;
    boolean group1IsFinish = false;
    boolean group2IsFinish = false;
    int waitSpace = 0;//等待时间间隔
    int timerTotal = 0;

    public void group1Use(){
        if(isTimeout){
            timerTotal++;
        }

        if(timerTotal > 0 && timerTotal <= group1.getTimeLong()*60){
            progressFirst.setProgress(timerTotal);
            group1IsFinish = false;
        }else{
            group1IsFinish = true;
            isTimeout = false;
            timerTotal = 0;
            isPlay = false;
            //重置音乐
            stopPlay();
            //停止波形图
            wareView.stop();
            //变化为开始键
            imgPlay.setImageResource(R.mipmap.icon_start_orange);
        }
        //设置group1进度条和数值颜色
        if(isPlay){
            txtFirstTime.setTextColor(0xffF4BB1B);
            progressFirst.setProgressColor(0xffF4BB1B);
        }else{
            txtFirstTime.setTextColor(0xffffffff);
            progressFirst.setProgressColor(0xffffffff);
        }
        txtSecondTime.setTextColor(0xffffffff);
        progressSecond.setProgressColor(0xffffffff);
    }

    public void group2Use(){
        if(isTimeout){
            timerTotal++;
        }
        if(timerTotal > 0 && timerTotal <= group2.getTimeLong()*60){
            progressSecond.setProgress(timerTotal);
            group2IsFinish = false;
        }else{
            group2IsFinish = true;
            isTimeout = false;
            timerTotal = 0;
            isPlay = false;
            //重置音乐
            stopPlay();
            //停止波形图
            wareView.stop();
            //变化为开始键
            imgPlay.setImageResource(R.mipmap.icon_start_orange);
        }
        //设置group1进度条和数值颜色
        if(isPlay){
            txtSecondTime.setTextColor(0xffF4BB1B);
            progressSecond.setProgressColor(0xffF4BB1B);
        }else{
            txtSecondTime.setTextColor(0xffffffff);
            progressSecond.setProgressColor(0xffffffff);
        }
        txtFirstTime.setTextColor(0xffffffff);
        progressFirst.setProgressColor(0xffffffff);
    }

    public void bothUse(){
        if(group1IsFinish && group2IsFinish){
            waitSpace = 0;
            group1IsFinish = false;
            group2IsFinish = false;
        }
        if(group1IsFinish){
            if(waitSpace > 10){
                //说明等待了十秒，可以加载第二组了

                group2Use();
            }else{
                waitSpace ++;
            }
        }else{
            group1Use();
        }
    }

    boolean timerIsInit = false;
    public void runProgress(){
        if(!timerIsInit){
            timerIsInit = true;
            handler.sendEmptyMessage(100);
        }
    }
}
