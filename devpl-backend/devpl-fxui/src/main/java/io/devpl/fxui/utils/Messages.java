package io.devpl.fxui.utils;

import io.devpl.sdk.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 从配置文件中加载所有的提示文本
 */
public class Messages {

    private Messages() {
    }

    private static final Properties messages = new Properties();

    static {
        File file = ResourceUtils.getResourcesAsFile("message.properties");
        if (file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                messages.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getString(String id) {
        final Object text = messages.get(id);
        if (text == null) {
            return "";
        }
        return String.valueOf(text);
    }
}
