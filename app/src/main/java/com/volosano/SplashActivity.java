package com.volosano;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Bind(R.id.img_splash)
    ImageView imgSplash;
    @Bind(R.id.txt_tip)
    TextView txtTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initSome();
        txtTip.setVisibility(View.GONE);
        imgSplash.setVisibility(View.VISIBLE);
        if (checkCanContinue()) {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timer.cancel();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    handler.sendEmptyMessageDelayed(1, 300);
                }
            }, 2000);
        } else {
            txtTip.setVisibility(View.VISIBLE);
            imgSplash.setVisibility(View.GONE);
        }
    }

    private boolean checkCanContinue() {
        Calendar cal = Calendar.getInstance();
        long currLong = cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_MONTH, 14);
        cal.set(Calendar.MONTH, 6);
        long limitLong = cal.getTimeInMillis();
        Date da = cal.getTime();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("ddddddd", sp.format(da));
        Log.e("ddddddd", "" + (limitLong > currLong));
        return limitLong > currLong;
    }

    private void initSome() {
        ToastUtil.setContext(this);
    }
}
