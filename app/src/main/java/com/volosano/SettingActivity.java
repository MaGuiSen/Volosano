package com.volosano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.volosano.modal.GroupSetting;
import com.volosano.modal.PointSetting;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.util.CacheUtil;
import lib.util.ToastUtil;
import lib.widget.ChoiceTimeDialog;
import lib.widget.ChoiceValueDialog;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.txt_voice)
    TextView txtVoice;
    @Bind(R.id.img_voice_up)
    ImageView imgVoiceUp;
    @Bind(R.id.img_voice_down)
    ImageView imgVoiceDown;
    @Bind(R.id.txt_first_time)
    TextView txtFirstTime;
    @Bind(R.id.txt_first_minute)
    TextView txtFirstTimeLong;
    @Bind(R.id.img_first_enable)
    ImageView imgFirstEnable;
    @Bind(R.id.txt_second_time)
    TextView txtSecondTime;
    @Bind(R.id.txt_second_minute)
    TextView txtSecondTimeLong;
    @Bind(R.id.img_second_enable)
    ImageView imgSecondEnable;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.txt_point)
    TextView txt_point;
    @Bind(R.id.txt_part_tip)
    TextView txt_part_tip;
    @Bind(R.id.seek_voice)
    SeekBar seek_voice;

    ChoiceTimeDialog choiceTimeDialog1,choiceTimeDialog2;
    ChoiceValueDialog choiceValueDialog1,choiceValueDialog2;

    //最大音量
    int maxVolume = 0;
    //当前音量
    int currentVolume = 0;
    AudioManager mAudioManager = null;

    GroupSetting group1Setting = null;
    GroupSetting group2Setting = null;

    String currPoint = "";
    PointSetting pointSetting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        currPoint = getIntent().getStringExtra("currPoint");

        initAudioControl();
        initPicker();
        initSetting();
        initUI();
    }

    private void initUI() {
        txt_point.setText(currPoint);
        String partTip = "";
        if("Neck".equals(currPoint)){
            partTip = "Bl10-Sl3";
        }else if("Shoulder".equals(currPoint)){
            partTip = "GL15-Sl11";
        }else if("Lower Back".equals(currPoint)){
            partTip = "BL57-BL40";
        }
        txt_part_tip.setText(partTip);

        refreshViewEnable(imgFirstEnable, txtFirstTime, txtFirstTimeLong, group1Setting.isEnable());
        refreshViewEnable(imgSecondEnable, txtSecondTime, txtSecondTimeLong, group2Setting.isEnable());

        refreshGroup(txtFirstTime, txtFirstTimeLong, group1Setting);
        refreshGroup(txtSecondTime, txtSecondTimeLong, group2Setting);

        //设置音量
        txtVoice.setText((int)(1f*pointSetting.getIntensity()/maxVolume*10) + "");
        seek_voice.setMax(10);
        seek_voice.setProgress((int) (1f*pointSetting.getIntensity()/maxVolume*10));
        seek_voice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                pointSetting.setIntensity((int) (1f*progress/10*maxVolume));
                txtVoice.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void refreshGroup(TextView timeV, TextView timeLongV, GroupSetting groupSetting){
        int hour = groupSetting.getHour();
        int minute = groupSetting.getMinute();
        int timeLong = groupSetting.getTimeLong();
        String timeStatus = hour > 12 ? "pm" : "am";
        String minuteString = (minute < 10 ? "0":"") + minute;
        timeV.setText(hour+":"+minuteString+timeStatus);
        timeLongV.setText(timeLong+"min");
    }

    public int getCurrentVolume(){
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return currentVolume;
    }

    private void initSetting() {
        pointSetting = CacheUtil.get(currPoint);
        if(pointSetting == null){
            //说明缓存里面还没有
            pointSetting = new PointSetting();
            //设置当前音量
            pointSetting.setIntensity(getCurrentVolume());
            pointSetting.setPoint(currPoint);
        }

        group1Setting = pointSetting.getGroupSetting1();
        group2Setting = pointSetting.getGroupSetting2();

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if(group1Setting == null){
            group1Setting = new GroupSetting();
            //默认第一组使能
            group1Setting.setEnable(true);
            group1Setting.setName("group1");
            //设置时间为当前时间的小时和分钟数
            group1Setting.setHour(hour);
            group1Setting.setMinute(minute);
            group1Setting.setTimeLong(1);
            //设置状态为stop
            group1Setting.setStatus(GroupSetting.STATUS_STOP);
        }

        if(group2Setting == null){
            group2Setting = new GroupSetting();
            //默认第二组不使能
            group2Setting.setEnable(false);
            group2Setting.setName("group2");
            //设置时间为当前时间的小时和分钟数
            group2Setting.setHour(hour);
            group2Setting.setMinute(minute);
            group2Setting.setTimeLong(1);
            //设置状态为stop
            group2Setting.setStatus(GroupSetting.STATUS_STOP);
        }
    }

    public void setVoice(){
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0); //tempVolume:音量绝对值
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
                initUI();
            }
        });
        choiceTimeDialog2.setDialogClickListener(new ChoiceTimeDialog.DialogClickListener() {
            @Override
            public void choice(int hour, int minute) {
                group2Setting.setHour(hour);
                group2Setting.setMinute(minute);
                initUI();
            }
        });
        choiceValueDialog1.setDialogClickListener(new ChoiceValueDialog.DialogClickListener() {
            @Override
            public void choice(int timeLong) {
                group1Setting.setTimeLong(timeLong);
                initUI();
            }
        });
        choiceValueDialog2.setDialogClickListener(new ChoiceValueDialog.DialogClickListener() {
            @Override
            public void choice(int timeLong) {
                group2Setting.setTimeLong(timeLong);
                initUI();
            }
        });
    }

    private void initAudioControl() {
        //音量控制,初始化定义
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public void refreshViewEnable(ImageView imageView, TextView time, TextView minuteLong, boolean isEnable){
        if(isEnable){
            imageView.setImageResource(R.drawable.circle_orange_a_border_gray_a);
            time.setTextColor(0xffe6e6e6);
            minuteLong.setTextColor(0xffe6e6e6);
        }else{
            imageView.setImageResource(R.drawable.circle_white_a_border_gray_a);
            time.setTextColor(0xff6F7B88);
            minuteLong.setTextColor(0xff6F7B88);
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
                pointSetting.setIntensity(currentVolume);
                break;
            case R.id.img_voice_down:
                if(currentVolume-1 < 0){
                    ToastUtil.show("It is the min intensity");
                    return;
                }
                currentVolume -= 1;
                txtVoice.setText(currentVolume + "");
                pointSetting.setIntensity(currentVolume);
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
                refreshViewEnable(imgFirstEnable, txtFirstTime, txtFirstTimeLong, group1Setting.isEnable());
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
                refreshViewEnable(imgSecondEnable, txtSecondTime, txtSecondTimeLong, group2Setting.isEnable());
                break;
            case R.id.img_start:
                //这里将当前设置缓存到内存中
                pointSetting.setGroupSetting1(group1Setting);
                pointSetting.setGroupSetting2(group2Setting);
                MyApplication.currSetting = pointSetting;
                CacheUtil.set(currPoint, pointSetting);

                //判断两个组是否有一组以上使能了
                if(!group1Setting.isEnable() && !group2Setting.isEnable()){
                    ToastUtil.show("Please Enable One Session At Least");
                    return;
                }

                Intent intent = new Intent(this, PlayActivity.class);
                intent.putExtra("currPoint", currPoint);
                startActivity(intent);
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
                    alertDialog.dismiss();
                }
            }).setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyApplication.currSetting = pointSetting;
                    Intent intent = new Intent(SettingActivity.this, PlayActivity.class);
                    intent.putExtra("currPoint", currPoint);
                    startActivity(intent);
                }
            });
            alertDialog = builder.create();
        }
        alertDialog.show();
    }
}
