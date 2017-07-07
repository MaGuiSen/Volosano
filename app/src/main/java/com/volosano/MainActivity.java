package com.volosano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.util.ToastUtil;
import lib.widget.WheelView;

public class MainActivity extends AppCompatActivity {
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth"};
    @Bind(R.id.WheelView)
    lib.widget.WheelView WheelView;

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
                Log.d("selectedIndex: ", selectedIndex + ", item: " + item);
            }
        });
    }

    @OnClick(R.id.img_msg)
    public void onClick() {
        startActivity(new Intent(this, ContactActivity.class));
    }

    @OnClick(R.id.img_ok)
    public void onCheck() {
        startActivity(new Intent(this, SettingActivity.class));
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
