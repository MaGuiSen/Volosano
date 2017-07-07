package com.volosano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
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
                break;
            case R.id.img_voice_down:
                break;
            case R.id.txt_first_time:
                break;
            case R.id.txt_first_minute:
                break;
            case R.id.img_first_enable:
                break;
            case R.id.txt_second_time:
                break;
            case R.id.txt_second_minute:
                break;
            case R.id.img_second_enable:
                break;
            case R.id.img_start:
                startActivity(new Intent(this, PlayActivity.class));
                break;
        }
    }
}
