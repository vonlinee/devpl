package io.devpl.backend.tools.parser.java;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 保存所有类型信息
 */
public class TypeInfoRegistry {

    /**
     * 类型名称
     */
    public static final ConcurrentHashMap<String, CopyOnWriteArrayList<TypeInfo>> types = new ConcurrentHashMap<>();

    public static List<TypeInfo> get(String simpleName) {
        return types.get(simpleName);
    }

    public static void register(TypeInfo typeInfo) {
        CopyOnWriteArrayList<TypeInfo> typeInfoList = types.get(typeInfo.getSimpleName());
        if (typeInfoList == null) {
            typeInfoList = new CopyOnWriteArrayList<>();
            types.put(typeInfo.getSimpleName(), typeInfoList);
        }
        typeInfoList.add(typeInfo);
    }
}
