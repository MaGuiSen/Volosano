package lib.util;

import com.volosano.modal.GroupSetting;
import com.volosano.modal.PointSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mags on 2017/7/10.
 * 模拟缓存
 */

public class CacheUtil {
    private static Map<String, PointSetting> cacheMap = new HashMap<>();
    public static PointSetting get(String key){
        return cacheMap.get(key);
    }

    /**
     * 根据key：痛点获取设置的缓存
     * @param key "Neck", "Shoulder", "Low Back"
     * @param groupSetting
     * @return
     */
    public static Map<String, PointSetting> set(String key, PointSetting groupSetting){
        cacheMap.put(key, groupSetting);
        return cacheMap;
    }
}
