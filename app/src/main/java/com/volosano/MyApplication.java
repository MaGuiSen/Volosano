package com.volosano;

import android.app.Application;

import com.volosano.modal.PointSetting;

/**
 * Created by mags on 2017/7/11.
 */

public class MyApplication extends Application {
    public static PointSetting currSetting = null;//正在执行的痛点设置

    @Override
    public void onCreate() {
        super.onCreate();
        MyCrashHandler.getInstance().init(this);
    }
}
