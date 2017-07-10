package com.volosano.manager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mags on 2017/7/10.
 */

public class TimerRun {
    long timeLong = 0;//转换成秒
    boolean isPause = false;
    Timer timer = null;
    public TimerRun(long timeLong){
        this.timeLong = timeLong;
    }

    public void start(final Listener listener){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isPause){
                    //如果没有停止
                    timeLong --;//依次递减
                }
                if(timeLong <= 0){
                    timer.cancel();
                    timer = null;
                    isPause = true;
                    if(listener != null){
                        //如果结束了就告知外部
                        listener.complete();
                    }
                }
                //如果停止了就不递减了
            }
        },1000,1000);
    }

    public void pause(){
        this.isPause = true;
    }


    public void resume(){
        this.isPause = false;
    }


    public void stop(){
        this.isPause = false;
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    interface Listener{
        void complete();
    }

}
