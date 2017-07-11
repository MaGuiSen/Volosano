package com.volosano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volosano.modal.GroupSetting;
import com.volosano.modal.PointSetting;

import java.io.IOException;

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
        String timeStatus = hour > 12 ? "am" : "pm";
        String minuteString = (minute < 10 ? "0":"") + minute;
        txtFirstTime.setText(hour+":"+minuteString+timeStatus);
        progressFirst.setProgress(timeLong);
        //0xffffffff 0xffF4BB1B
        txtFirstTime.setTextColor(0xffffffff);
        progressFirst.setProgressColor(0xffffffff);


        int hour2 = group2.getHour();
        int minute2 = group2.getMinute();
        int timeLong2 = group2.getTimeLong();
        String timeStatus2 = hour > 12 ? "am" : "pm";
        String minuteString2 = (minute2 < 10 ? "0":"") + minute2;
        txtSecondTime.setText(hour2+":"+minuteString2+timeStatus2);
        txtSecondTime.setTextColor(0xffffffff);
        progressSecond.setProgress(timeLong2);
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
                try{
                    if(isPlay){
                        //开启音乐
                        startPlay();
                        //开启波形图
                        wareView.start();
                        //变化为暂停键
                        imgPlay.setImageResource(R.mipmap.icon_stop);
                    }else{
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
            builder.setTitle("System is running, if go back,the system will stop.").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
}
