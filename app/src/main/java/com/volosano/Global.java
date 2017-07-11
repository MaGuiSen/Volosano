package com.volosano;

import com.volosano.modal.GroupSetting;
import com.volosano.modal.PointSetting;

/**
 * Created by mags on 2017/7/9.
 */

public class Global {
    public static String Timing = "timing";
    public static String Running = "running";
    public static String Pause = "pausePlay";
    public static String Stop = "stop";
    public static String Complete = "complete";
    public static String status = "";//timing running stop pausePlay Complete
    public static PointSetting currSetting = null;//正在执行的痛点设置

    /**
     * 是否正在执行
     * @return
     */
    public static boolean isExecuting(){
        return Timing.equals(Global.status)
                || Running.equals(Global.status)
                || Pause.equals(Global.status);
    }
}
