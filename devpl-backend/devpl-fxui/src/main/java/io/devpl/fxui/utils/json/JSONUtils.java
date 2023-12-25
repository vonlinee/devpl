package io.devpl.fxui.utils.json;

/**
 * JSON工具类
 */
public class JSONUtils {

    static final JSONConverter gson = new GsonConverter();

    public static <T> T toObject(String json, Class<T> type) {
        return gson.toObject(json, type);
    }

    public static String toJSONString(Object obj) {
        return gson.toJSONString(obj, false);
    }

    public static String toJSONString(Object obj, boolean prettyPrinting) {
        return gson.toJSONString(obj, prettyPrinting);
    }
}
