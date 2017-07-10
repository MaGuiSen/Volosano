package com.volosano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volosano.manager.PlayManager;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        currPoint = getIntent().getStringExtra("currPoint");
        txt_part.setText(currPoint);
        if("Neck".equals(currPoint)){
            imgPart.setImageResource(BodyImages[0]);
        }else if("Shoulder".equals(currPoint)){
            imgPart.setImageResource(BodyImages[1]);
        }else if("Low Back".equals(currPoint)){
            imgPart.setImageResource(BodyImages[2]);
        }
        PlayManager.getInstance().start();
        progressFirst.setProgressColor(0xffF4BB1B);
        progressSecond.setProgressColor(0xffffffff);
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
