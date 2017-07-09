package com.volosano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volosano.modal.GroupSettingModal;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.util.CacheUtil;
import lib.util.ToastUtil;
import lib.widget.ChoiceTimeDialog;
import lib.widget.ChoiceValueDialog;

public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.txt_voice)
    TextView txtVoice;
    @Bind(R.id.img_voice_up)
    ImageView imgVoiceUp;
    @Bind(R.id.img_voice_down)
    ImageView imgVoiceDown;
    @Bind(R.id.txt_first_time)
    TextView txtFirstTime;
    @Bind(R.id.txt_first_minute)
    TextView txtFirstMinute;
    @Bind(R.id.img_first_enable)
    ImageView imgFirstEnable;
    @Bind(R.id.txt_second_time)
    TextView txtSecondTime;
    @Bind(R.id.txt_second_minute)
    TextView txtSecondMinute;
    @Bind(R.id.img_second_enable)
    ImageView imgSecondEnable;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.txt_point)
    TextView txt_point;

    ChoiceTimeDialog choiceTimeDialog1,choiceTimeDialog2;
    ChoiceValueDialog choiceValueDialog1,choiceValueDialog2;
    //最大音量
    int maxVolume = 0;
    //当前音量
    int currentVolume = 0;
    AudioManager mAudioManager = null;

    GroupSettingModal group1Setting = new GroupSettingModal();
    GroupSettingModal group2Setting = new GroupSettingModal();

    String currPoint = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initAudioControl();
        initPicker();
        initSetting();
        refreshViewEnable(imgFirstEnable, txtFirstTime, txtFirstMinute, group1Setting.isEnable());
        refreshViewEnable(imgSecondEnable, txtSecondTime, txtSecondMinute, group2Setting.isEnable());
        currPoint = getIntent().getStringExtra("currPoint");
        txt_point.setText(currPoint);
    }

    private void initSetting() {
        group1Setting = CacheUtil.get(currPoint+"_group1");
        group2Setting = CacheUtil.get(currPoint+"_group2");
        if(group1Setting == null){
            group1Setting = new GroupSettingModal();
            group1Setting.setEnable(true);
            group1Setting.setName("group1");
        }
        if(group2Setting == null){
            group2Setting = new GroupSettingModal();
            group2Setting.setEnable(false);
            group2Setting.setName("group2");
        }

    }

    private void initPicker() {
        choiceTimeDialog1 = new ChoiceTimeDialog(this);
        choiceTimeDialog2 = new ChoiceTimeDialog(this);
        choiceValueDialog1 = new ChoiceValueDialog(this);
        choiceValueDialog2 = new ChoiceValueDialog(this);
        choiceTimeDialog1.setDialogClickListener(new ChoiceTimeDialog.DialogClickListener() {
            @Override
            public void choice(int hour, int minute) {
                group1Setting.setHour(hour);
                group1Setting.setMinute(minute);
            }
        });
        choiceTimeDialog2.setDialogClickListener(new ChoiceTimeDialog.DialogClickListener() {
            @Override
            public void choice(int hour, int minute) {
                group2Setting.setHour(hour);
                group2Setting.setMinute(minute);
            }
        });
        choiceValueDialog1.setDialogClickListener(new ChoiceValueDialog.DialogClickListener() {
            @Override
            public void choice(int timeLong) {
                group1Setting.setTimeLong(timeLong);
            }
        });
        choiceValueDialog2.setDialogClickListener(new ChoiceValueDialog.DialogClickListener() {
            @Override
            public void choice(int timeLong) {
                group2Setting.setTimeLong(timeLong);
            }
        });
    }

    private void initAudioControl() {
        //音量控制,初始化定义
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        txtVoice.setText(currentVolume + "");
    }

    public void refreshViewEnable(ImageView imageView, TextView time, TextView minuteLong, boolean isEnable){
        if(isEnable){
            imageView.setImageResource(R.drawable.circle_orange_a_border_gray_a);
            time.setTextColor(0xff6F7B88);
            minuteLong.setTextColor(0xff6F7B88);
        }else{
            imageView.setImageResource(R.drawable.circle_white_a_border_gray_a);
            time.setTextColor(0xffe6e6e6);
            minuteLong.setTextColor(0xffe6e6e6);
        }
    }

    @OnClick({R.id.img_back, R.id.img_msg, R.id.img_voice_up, R.id.img_voice_down, R.id.txt_first_time, R.id.txt_first_minute, R.id.img_first_enable, R.id.txt_second_time, R.id.txt_second_minute, R.id.img_second_enable, R.id.img_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_msg:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.img_voice_up:
                if(currentVolume+1 > maxVolume){
                    ToastUtil.show("It is the max intensity");
                    return;
                }
                currentVolume += 1;
                txtVoice.setText(currentVolume + "");
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0); //tempVolume:音量绝对值
                break;
            case R.id.img_voice_down:
                if(currentVolume-1 < 0){
                    ToastUtil.show("It is the min intensity");
                    return;
                }
                currentVolume -= 1;
                txtVoice.setText(currentVolume + "");
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0); //tempVolume:音量绝对值

                break;
            case R.id.txt_first_time:
                if(!group1Setting.isEnable()){
                    return;
                }
                choiceTimeDialog1.show();
                break;
            case R.id.txt_first_minute:
                if(!group1Setting.isEnable()){
                    return;
                }
                choiceValueDialog1.show();
                break;
            case R.id.img_first_enable:
                group1Setting.setEnable(!group1Setting.isEnable());
                refreshViewEnable(imgFirstEnable, txtFirstTime, txtFirstMinute, group1Setting.isEnable());
                break;
            case R.id.txt_second_time:
                if(!group2Setting.isEnable()){
                    return;
                }
                choiceTimeDialog2.show();
                break;
            case R.id.txt_second_minute:
                if(!group2Setting.isEnable()){
                    return;
                }
                choiceValueDialog2.show();
                break;
            case R.id.img_second_enable:
                group2Setting.setEnable(!group2Setting.isEnable());
                refreshViewEnable(imgSecondEnable, txtSecondTime, txtSecondMinute, group2Setting.isEnable());
                break;
            case R.id.img_start:
                //这里将当前设置缓存到内存中
                //TODO..
                CacheUtil.set(currPoint+"_group1", group1Setting);
                CacheUtil.set(currPoint+"_group2", group2Setting);
                //判断两个组是否有一组以上使能了
                if(!group1Setting.isEnable() && group2Setting.isEnable()){
                    ToastUtil.show("Please Enable One Session At Least");
                    return;
                }
                //判断是否有全局的正在运行或者定时，有则会提示继续会将覆盖之前的设定 ：定时中  pause :暂停中 stop 停止了 running 运行
                if(Gloable.group1Setting != null
                        &&  ("timing".equals(Gloable.group1Setting.getStatus())
                            || "running".equals(Gloable.group1Setting.getStatus())
                            || "pause".equals(Gloable.group1Setting.getStatus()))){
                    //说明有设置项在起作用,如果继续操作的话将覆盖当前正在运行的
                    showAlertDialog();
                }else {
                    //判断是否有全局的正在运行或者定时，有则会提示继续会将覆盖之前的设定 ：定时中  pause :暂停中 stop 停止了 running 运行
                    if (Gloable.group2Setting != null
                            && ("timing".equals(Gloable.group2Setting.getStatus())
                            || "running".equals(Gloable.group2Setting.getStatus())
                            || "pause".equals(Gloable.group2Setting.getStatus()))) {
                        //说明有设置项在起作用,如果继续操作的话将覆盖当前正在运行的
                        showAlertDialog();
                    }else{
                        //说明没有任何设置在起作用在起作用就直接跳转播放
                        startActivity(new Intent(this, PlayActivity.class));
                    }
                }
                break;
        }
    }


    AlertDialog alertDialog = null;
    public void showAlertDialog(){
        if(alertDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("重复设置将覆盖之前的设置").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                     if(alertDialog != null){
                         alertDialog.dismiss();
                     }
                }
            }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(group1Setting!= null && group1Setting.isEnable()){
                        Gloable.group1Setting = group1Setting;
                        Gloable.group1Setting.setStatus("timing");
                    }
                    if(group2Setting!= null && group2Setting.isEnable()){
                        Gloable.group2Setting = group2Setting;
                        Gloable.group2Setting.setStatus("timing");
                    }
                    startActivity(new Intent(SettingActivity.this, PlayActivity.class));
                }
            });
            alertDialog = builder.create();
        }
        alertDialog.show();
    }
}
