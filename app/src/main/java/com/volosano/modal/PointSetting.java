package com.volosano.modal;

/**
 * Created by mags on 2017/7/10.
 * 痛点设置
 */

public class PointSetting {
    private int Intensity = -1;//音量
    private String point;//部位
    private GroupSetting groupSetting1 = null;
    private GroupSetting groupSetting2 = null;

    public int getIntensity() {
        return Intensity;
    }

    public PointSetting setIntensity(int intensity) {
        Intensity = intensity;
        return this;
    }

    public String getPoint() {
        return point;
    }

    public PointSetting setPoint(String point) {
        this.point = point;
        return this;
    }

    public GroupSetting getGroupSetting1() {
        return groupSetting1;
    }

    public PointSetting setGroupSetting1(GroupSetting groupSetting1) {
        this.groupSetting1 = groupSetting1;
        return this;
    }

    public GroupSetting getGroupSetting2() {
        return groupSetting2;
    }

    public PointSetting setGroupSetting2(GroupSetting groupSetting2) {
        this.groupSetting2 = groupSetting2;
        return this;
    }
}
