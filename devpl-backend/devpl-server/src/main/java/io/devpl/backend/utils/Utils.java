package io.devpl.backend.utils;

import io.devpl.sdk.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * 打开指定输出文件目录
     *
     * @param outDir 输出文件目录
     * @throws IOException IO错误
     */
    public static void openDirectory(String outDir) throws IOException {
        String osName = System.getProperty("os.name");
        if (osName != null) {
            if (osName.contains("Mac")) {
                Runtime.getRuntime().exec("open " + outDir);
            } else if (osName.contains("Windows")) {
                Runtime.getRuntime().exec(MessageFormat.format("cmd /c start \"\" \"{0}\"", outDir));
            } else {
                log.debug("文件输出目录:{}", outDir);
            }
        } else {
            log.warn("读取操作系统失败");
        }
    }

    public static void trimFields(Object obj) {
        if (obj == null) {
            return;
        }
        for (Field declaredField : obj.getClass().getDeclaredFields()) {
            if (!declaredField.canAccess(obj)) {
                declaredField.setAccessible(true);
            }
            Object value = null;
            try {
                value = declaredField.get(obj);
                if (value instanceof String str) {
                    declaredField.set(obj, StringUtils.trim(str));
                }
            } catch (Exception exception) {
                log.error("faile to trim value {}", value, exception);
            }
        }
    }
}
