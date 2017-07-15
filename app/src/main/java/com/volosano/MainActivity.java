package com.volosano;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.util.ToastUtil;
import lib.widget.WheelView;

public class MainActivity extends BaseActivity {
    private static final String[] PLANETS = new String[]{"Neck", "Shoulder", "Low Back"};
    private static final int[] BodyImages = {R.mipmap.icon_body_neck,R.mipmap.icon_body_shoulder,R.mipmap.icon_body_low_back};
    @Bind(R.id.WheelView)
    lib.widget.WheelView WheelView;
    @Bind(R.id.img_body)
    ImageView img_body;
    String currChoicePoint = PLANETS[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initWheelView();
    }

    public void initWheelView() {
        WheelView wva = (WheelView) findViewById(R.id.WheelView);
        wva.setOffset(1);
        wva.setItems(Arrays.asList(PLANETS));
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                img_body.setImageResource(BodyImages[selectedIndex - 1]);
                currChoicePoint = PLANETS[selectedIndex - 1];
            }
        });
    }

    @OnClick(R.id.img_msg)
    public void onClick() {
        startActivity(new Intent(this, ContactActivity.class));
    }

    @OnClick(R.id.img_ok)
    public void onCheck() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("currPoint", currChoicePoint);
        startActivity(intent);
    }

    // 再按一次退出
    private long firstTime;
    private long secondTime;
    private long spaceTime;

    @Override
    public void onBackPressed() {
        firstTime = System.currentTimeMillis();
        spaceTime = firstTime - secondTime;
        secondTime = firstTime;
        if (spaceTime > 2000) {
            ToastUtil.show("Click again to exit the application");
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }
}
