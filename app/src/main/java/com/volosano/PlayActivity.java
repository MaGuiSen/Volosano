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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volosano.modal.GroupSetting;
import com.volosano.modal.PointSetting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.util.FileUtil;
import lib.widget.CircleProgressView;
import lib.widget.WareView;

public class PlayActivity extends BaseActivity {
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
                if(isPlay){
                    //变化为暂停键
                    imgPlay.setImageResource(R.mipmap.icon_stop);
                }else{
                    //变化为开始键
                    imgPlay.setImageResource(R.mipmap.icon_start_orange);
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
                    isPlay = false;
                    //暂停音乐
                    stopPlay();
                    //停止波形图
                    wareView.pause();
                    //变化为开始键
                    imgPlay.setImageResource(R.mipmap.icon_start_orange);
                    finish();
                }
            });
            alertDialog = builder.create();
        }
        alertDialog.show();
    }

    MediaPlayer player = null;
    public void initPlayer(){
        File file = new File(FileUtil.getCacheSDPath()+"wave.wav");
        if(file.exists()){
            Log.e("play","播放语音");
            player = new MediaPlayer();
            player.reset();
            try {
                player.setDataSource(new FileInputStream(file).getFD());
                player.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Log.e("play","播放系统");
            player = MediaPlayer.create(this, R.raw.wave);
        }
        player.setLooping(true);
    }

    public void startPlay(){
        try {
            if(player == null){
                initPlayer();
            }
            if(player.isPlaying()){
                return;
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
            if(player != null && player.isPlaying()) {
                player.pause();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    boolean group1IsFinish = false;
    boolean group2IsFinish = false;
    int waitSpace = 0;//等待时间间隔
    int timerTotal1 = 0;
    int timerTotal2 = 0;

    public void group1Use(){
        if(isPlay){
            timerTotal1++;
        }
        if(timerTotal1 >= 0 && timerTotal1 <= group1.getTimeLong()*60){
            progressFirst.setProgress(timerTotal1);
            group1IsFinish = false;
        }else{
            group1IsFinish = true;
            timerTotal1 = 0;
            isPlay = false;
            progressFirst.setProgress(0);
        }
        refresh(group1IsFinish);
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
        if(isPlay){
            timerTotal2++;
        }
        if(timerTotal2 >= 0 && timerTotal2 <= group2.getTimeLong()*60){
            progressSecond.setProgress(timerTotal2);
            group2IsFinish = false;
        }else{
            group2IsFinish = true;
            timerTotal2 = 0;
            isPlay = false;
            progressSecond.setProgress(0);
        }
        refresh(group2IsFinish);
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

    public void refresh(boolean groupIsFinish){
        if(isPlay){
            //开启波形图
            wareView.start();
            //播放
            startPlay();
            //变化为暂停键
            imgPlay.setImageResource(R.mipmap.icon_stop);
        }else{
            if(groupIsFinish){
                //停音乐
                stopPlay();
                //重置波形图
                wareView.stop();
            }else{
                //暂停音乐
                pausePlay();
                //停止波形图
                wareView.pause();
            }
            //变化为开始键
            imgPlay.setImageResource(R.mipmap.icon_start_orange);
        }
    }

    public void bothUse(){
        if(group1IsFinish && group2IsFinish){
            waitSpace = 0;
            group1IsFinish = false;
            group2IsFinish = false;
        }

        if(group1IsFinish){
            if(waitSpace > 5){
                //说明等待了十秒，可以加载第二组了
                if(timerTotal2 == 0){
                    isPlay = true;
                }
                group2Use();
            }else{
                waitSpace ++;
                //如果在等待过程中 用户设置为播放，那就要直接进行播放
                if(isPlay){
                    waitSpace = 6;
                    group2Use();
                }
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
