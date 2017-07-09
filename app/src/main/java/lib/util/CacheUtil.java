package lib.util;

import com.volosano.modal.GroupSettingModal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mags on 2017/7/10.
 * 模拟缓存
 */

public class CacheUtil {
    private static Map<String, GroupSettingModal> cacheMap = new HashMap<>();
    public static GroupSettingModal get(String key){
        return cacheMap.get(key);
    }

    /**
     *
     * @param key "Neck_group1", "Shoulder_group1", "Low Back_group1" "Neck_group2", "Shoulder_group2", "Low Back_group2"
     *            "Neck_intensity", "Shoulder_intensity", "Low Back_intensity"
     * @param groupSettingModal
     * @return
     */
    public static GroupSettingModal set(String key, GroupSettingModal groupSettingModal){
        return cacheMap.put(key, groupSettingModal);
    }
}
