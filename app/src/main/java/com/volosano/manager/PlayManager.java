package com.volosano.manager;

import android.util.Log;

import com.volosano.Global;
import com.volosano.modal.GroupSetting;
import com.volosano.modal.PointSetting;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mags on 2017/7/10.
 */

public class PlayManager {
    private static PlayManager playManager;
    private PlayManager(){
    }
    public synchronized static PlayManager getInstance(){
        if(playManager == null){
            playManager = new PlayManager();
        }
        return playManager;
    }

    private Timer alarmTimer = null;//用于循环判断，时间是否到达
    private TimerRun timerRun = null;

    public void start(){
        if(alarmTimer == null){
            alarmTimer = new Timer();
            alarmTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //如果当前状态是timing 计时中，则判断是否到时间
                    if(Global.Timing.equals(Global.status)){
                        //获取组1的设置信息
                        PointSetting currSetting = Global.currSetting;
                        if(currSetting != null){
                            final GroupSetting group1 = currSetting.getGroupSetting1();
                            final GroupSetting group2 = currSetting.getGroupSetting2();
                            if(group1.isEnable() && !group2.isEnable()){
                                if(!GroupSetting.STATUS_COMPLETE.equals(group1.getStatus())) {
                                    //判断时间小于
                                    Calendar cal = Calendar.getInstance();
                                    long currTime = cal.getTimeInMillis();
                                    int hour = group1.getHour();
                                    int minute = group1.getMinute();
                                    cal.set(Calendar.HOUR_OF_DAY, hour);
                                    cal.set(Calendar.MINUTE, minute);
                                    long group1Time = cal.getTimeInMillis();
                                    if (group1Time <= currTime) {
                                        //说明可以开始播放了
                                        Global.status = Global.Running;
                                        timerRun = new TimerRun(group1.getTimeLong());
                                        timerRun.start(new TimerRun.Listener() {
                                            @Override
                                            public void complete() {
                                                group1.setStatus(GroupSetting.STATUS_COMPLETE);
                                                Global.status = Global.Complete;
                                                timerRun = null;
                                            }
                                        });
                                    }
                                }else{
                                    Global.status = Global.Complete;
                                }
                            }else if(!group1.isEnable() && group2.isEnable()){
                                if(!GroupSetting.STATUS_COMPLETE.equals(group2.getStatus())){
                                    //判断时间小于
                                    Calendar cal = Calendar.getInstance();
                                    long currTime = cal.getTimeInMillis();
                                    int hour = group2.getHour();
                                    int minute = group2.getMinute();
                                    cal.set(Calendar.HOUR_OF_DAY, hour);
                                    cal.set(Calendar.MINUTE, minute);
                                    long group2Time = cal.getTimeInMillis();
                                    if(group2Time <= currTime){
                                        //说明可以开始播放了
                                        Global.status = Global.Running;
                                        timerRun = new TimerRun(group2.getTimeLong());
                                        timerRun.start(new TimerRun.Listener() {
                                            @Override
                                            public void complete() {
                                                group2.setStatus(GroupSetting.STATUS_COMPLETE);
                                                Global.status = Global.Complete;
                                                timerRun = null;
                                            }
                                        });
                                    }
                                }else{
                                    Global.status = Global.Complete;
                                }
                            }else if(group1.isEnable() && group2.isEnable()){
                                //就是两者都使能了，因为设置界面不可能给空
                                //判断两者时间大小，当然优先判断是否complete
                                if(!GroupSetting.STATUS_COMPLETE.equals(group1.getStatus()) && GroupSetting.STATUS_COMPLETE.equals(group1.getStatus())){
                                    //判断时间小于
                                    Calendar cal = Calendar.getInstance();
                                    long currTime = cal.getTimeInMillis();
                                    int hour = group1.getHour();
                                    int minute = group1.getMinute();
                                    cal.set(Calendar.HOUR_OF_DAY, hour);
                                    cal.set(Calendar.MINUTE, minute);
                                    long group1Time = cal.getTimeInMillis();
                                    if (group1Time <= currTime) {
                                        //说明可以开始播放了
                                        Global.status = Global.Running;
                                        timerRun = new TimerRun(group1.getTimeLong());
                                        timerRun.start(new TimerRun.Listener() {
                                            @Override
                                            public void complete() {
                                                group1.setStatus(GroupSetting.STATUS_COMPLETE);
                                                Global.status = Global.Complete;
                                                timerRun = null;
                                            }
                                        });
                                    }
                                }else if(GroupSetting.STATUS_COMPLETE.equals(group1.getStatus()) && !GroupSetting.STATUS_COMPLETE.equals(group1.getStatus())){
                                    //判断时间小于
                                    Calendar cal = Calendar.getInstance();
                                    long currTime = cal.getTimeInMillis();
                                    int hour = group2.getHour();
                                    int minute = group2.getMinute();
                                    cal.set(Calendar.HOUR_OF_DAY, hour);
                                    cal.set(Calendar.MINUTE, minute);
                                    long group2Time = cal.getTimeInMillis();
                                    if(group2Time <= currTime){
                                        //说明可以开始播放了
                                        Global.status = Global.Running;
                                        timerRun = new TimerRun(group2.getTimeLong());
                                        timerRun.start(new TimerRun.Listener() {
                                            @Override
                                            public void complete() {
                                                group2.setStatus(GroupSetting.STATUS_COMPLETE);
                                                Global.status = Global.Complete;
                                                timerRun = null;
                                            }
                                        });
                                    }
                                }else if(GroupSetting.STATUS_COMPLETE.equals(group1.getStatus()) && GroupSetting.STATUS_COMPLETE.equals(group1.getStatus())){

                                }
                            }else{
                                Global.status = Global.Complete;
                            }
                        }
                    }else{
                        //否则就不做动静
                    }
                }
            }, 1000, 1000);
        }
    }



    public void pause(){
        //改变状态
        //暂停播放音乐
    }

    public void resume(){
        //改变状态
        //开始播放音乐
    }

    public void stop(){
        if(alarmTimer != null){
            alarmTimer.cancel();
        }
    }
}
