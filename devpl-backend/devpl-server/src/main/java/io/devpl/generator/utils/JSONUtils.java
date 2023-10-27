package io.devpl.generator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

/**
 * JSON 工具类
 */
public abstract class JSONUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toString(Object object) {
        return toString(object, false);
    }

    public static String toString(Object object, boolean prettyStyle) {
        try {
            if (prettyStyle) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                return objectMapper.writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        try {
            return objectMapper.readValue(text, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFile(Object obj, File file) {
        writeFile(obj, file, false);
    }

    public static void writeFile(Object obj, File file, boolean prettyStyle) {
        try {
            Files.writeString(file.toPath(), toString(obj, prettyStyle), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(text, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(text, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析字符串，有语法错误直接抛异常
     *
     * @param text 可能为JSON的字符串
     */
    public static void validateJson(String text) throws RuntimeException {
        parseObject(text, Object.class);
    }
}
