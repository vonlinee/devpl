package io.devpl.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志记录类
 */
public final class Log {

    private static final Map<Class<?>, Logger> loggerMap = new HashMap<>();

    private Log() {
    }

    public static void info(Object obj, String msg, Object... args) {
        Logger logger = getLogger(obj);
        if (logger.isInfoEnabled()) {
            logger.info(msg, args);
        }
    }

    public static void debug(Object obj, String msg, Object... args) {
        Logger logger = getLogger(obj);
        if (logger.isDebugEnabled()) {
            logger.debug(msg, args);
        }
    }

    private static Logger getLogger(Object obj) {
        Class<?> clazz = obj.getClass();
        return loggerMap.computeIfAbsent(clazz, LoggerFactory::getLogger);
    }
}
