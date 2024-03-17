package io.devpl.backend.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于反射，将obj转为map
 */
@Slf4j
public class ObjectToMapUtil {

    private static final String SEPARATOR = "_";

    /**
     * 嵌套对象转大map(扁平化)
     *
     * @param object 源对象
     * @return map
     */
    public static Map<String, Object> nestedObj2Map(Object object) {
        Map<String, Object> maps = JSON.parseObject(JSON.toJSONString(object), Map.class);
        Map<String, Object> result = new HashMap<>();
        maps.forEach((key, value) -> {
            common(maps, result, key, value, key);
        });
        return result;
    }

    /**
     * List嵌套对象转大list map(扁平化)
     * 处理列表对象
     *
     * @param objectList 源List对象
     * @return map
     */
    public static <T> List<Map<String, Object>> nestedObjList2ListMap(List<T> objectList) {
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        for (T t : objectList) {
            resultList.add(nestedObj2Map(t));
        }
        return resultList;
    }

    /**
     * 处理单个对象
     *
     * @param maps
     * @param prefix
     * @return
     */
    public static Map<String, Object> nestedObj2Map(Map<String, Object> maps, String prefix) {
        Map<String, Object> result = new HashMap<>();
        String keyPrefix = prefix + SEPARATOR;
        maps.forEach((key, value) -> {
            String newKey = keyPrefix + key;
            common(maps, result, key, value, newKey);
        });
        return result;
    }

    public static void common(Map<String, Object> maps, Map<String, Object> result, String key, Object value, String newKey) {
        if (maps.get(key) != null && value instanceof JSONObject) {
            Map<String, Object> subMaps = (Map) maps.get(key);
            Map<String, Object> map = nestedObj2Map(subMaps, newKey);
            if (!map.isEmpty()) {
                result.putAll(map);
            }
        } else {
            result.put(newKey, maps.get(key));
        }
    }
}
