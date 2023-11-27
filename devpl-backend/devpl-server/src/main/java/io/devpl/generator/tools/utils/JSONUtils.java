package io.devpl.generator.tools.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Map;

public class JSONUtils {

    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static Map<String, Object> toMap(String json) {
        try (JSONReader reader = JSONReader.of(json)) {
            return reader.readObject();
        }
    }

    public static Map<String, Object> read(File file) {
        try {
            final JSONReader reader = JSONReader.of(new FileReader(file));
            return reader.readObject();
        } catch (FileNotFoundException e) {
            return Collections.emptyMap();
        }
    }
}
