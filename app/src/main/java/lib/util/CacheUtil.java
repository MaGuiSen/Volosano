package lib.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.volosano.modal.PointSetting;

/**
 * Created by mags on 2017/7/10.
 * 模拟缓存
 */

public class CacheUtil {
    public static PointSetting get(String key){
        String setting = PreferenceUtil.getInstance().getStringParam(key, "");
        if(TextUtils.isEmpty(setting)){
            return null;
        }
        return JSON.parseObject(setting, PointSetting.class, Feature.IgnoreNotMatch);
    }

    /**
     * 根据key：痛点获取设置的缓存
     * @param key "Neck", "Shoulder", "Low Back"
     * @param pointSetting
     * @return
     */
    public static void set(String key, PointSetting pointSetting){
        PreferenceUtil.getInstance().saveParam(key, JSON.toJSONString(pointSetting));
    }
}
