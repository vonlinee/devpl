package io.devpl.backend.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DocUtils {

    public static <V> List<String> toDocText(Map<String, V> map) {
        final LinkedList<String> list = new LinkedList<>();
        int i = 0;
        for (Map.Entry<String, V> entry : map.entrySet()) {
            i++;
            list.add(i + ": " + entry.getKey() + " => " + entry.getValue().getClass().getSimpleName());
        }
        return list;
    }
}
