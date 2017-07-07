package com.volosano;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import lib.util.ToastUtil;

public class SplashActivity extends Activity {
    int finish = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == finish) {
                SplashActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initSome();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                handler.sendEmptyMessageDelayed(1, 300);
            }
        }, 2000);
    }

    private void initSome() {
        ToastUtil.setContext(this);
    }
}
