package com.volosano.modal;

/**
 * Created by mags on 2017/7/9.
 */

public class GroupSettingModal {
    private int hour;//小时数
    private int minute;//分钟数
    private int timeLong;//时长
    private String status = "stop";//timing ：定时中  pause :暂停中 stop 停止了 running 运行中
    private String name;//标识 group1 group2
    private boolean isEnable;//是否使能

    public int getHour() {
        return hour;
    }

    public GroupSettingModal setHour(int hour) {
        this.hour = hour;
        return this;
    }

    public int getMinute() {
        return minute;
    }

    public GroupSettingModal setMinute(int minute) {
        this.minute = minute;
        return this;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public GroupSettingModal setTimeLong(int timeLong) {
        this.timeLong = timeLong;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public GroupSettingModal setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupSettingModal setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public GroupSettingModal setEnable(boolean enable) {
        isEnable = enable;
        return this;
    }
}
