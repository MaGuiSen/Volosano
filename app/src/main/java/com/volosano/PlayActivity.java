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
import lib.widget.CircleProgressView;
import lib.widget.WareView;

public class PlayActivity extends AppCompatActivity {

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
    @Bind(R.id.progress_second)
    CircleProgressView progressSecond;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.img_msg, R.id.img_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_msg:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.img_play:
                break;
        }
    }
}
