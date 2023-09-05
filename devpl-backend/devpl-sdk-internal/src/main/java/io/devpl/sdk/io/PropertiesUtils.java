package io.devpl.sdk.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * Utilities for Properties.
 */
public class PropertiesUtils {

    public static final Properties EMPTY = new Properties();

    /**
     * @see PropertiesUtils#load(InputStream, boolean)
     */
    public static Properties load(InputStream stream) throws IOException, IllegalArgumentException {
        return load(stream, false);
    }

    /**
     * @throws IOException              if an error occurred when reading from the
     *                                  input stream.
     * @throws IllegalArgumentException if the input stream contains a
     *                                  malformed Unicode escape sequence.
     */
    public static Properties load(InputStream stream, boolean optional) throws IOException, IllegalArgumentException {
        if (stream == null) {
            return optional ? EMPTY : null;
        }
        Properties properties = new Properties();
        properties.load(stream);
        return properties;
    }

    /**
     * Load properties from the given file into Properties.
     */
    public static Properties load(String pathname) {
        try (InputStream is = new FileInputStream(pathname)) {
            return load(is);
        } catch (IOException e) {
            return new Properties();
        }
    }

    /**
     * Pick the name of a JDBC url. Such as xxx.url, xxx is the name.
     */
    public static List<String> loadNameList(Properties properties, String propertyPrefix) {
        List<String> nameList = new ArrayList<String>();
        Set<String> names = new HashSet<String>();
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
