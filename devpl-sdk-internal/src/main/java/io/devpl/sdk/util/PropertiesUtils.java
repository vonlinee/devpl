package io.devpl.sdk.util;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * 针对java.util.Properties的工具类.
 */
public class PropertiesUtils {

    private static void loadProperties(Properties properties, File file) {
        try (InputStream stream = new FileInputStream(file)) {
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load properties from the given file into Properties.
     */
    public static Properties loadProperties(String file) {
        Properties properties = new Properties();
        if (file == null) {
            return properties;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            try {
                is = PropertiesUtils.class.getResourceAsStream(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (is != null) {
            try {
                properties.load(is);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    /**
     * Pick the name of a JDBC url. Such as xxx.url, xxx is the name.
     */
    public static List<String> loadNameList(Properties properties, String propertyPrefix) {
        List<String> nameList = new ArrayList<>();
        Set<String> names = new HashSet<>();
        for (String n : properties.stringPropertyNames()) {
            if (propertyPrefix != null && !propertyPrefix.isEmpty() && !n.startsWith(propertyPrefix)) {
                continue;
            }
            if (n.endsWith(".url")) {
                names.add(n.split("\\.url")[0]);
            }
        }
        if (!names.isEmpty()) {
            nameList.addAll(names);
        }
        return nameList;
    }

    public static Properties filterPrefix(Properties properties, String prefix) {
        if (properties == null || prefix == null || prefix.isEmpty()) {
            return properties;
        }
        Properties result = new Properties();
        for (String n : properties.stringPropertyNames()) {
            if (n.startsWith(prefix)) {
                result.setProperty(n, properties.getProperty(n));
            }
        }
        return result;
    }

    public static Map<String, String> asMap(Properties properties) {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Properties props, String name, T defaultValue) {
        Object value = props.get(name);
        return value == null ? defaultValue : (T) value;
    }
}
