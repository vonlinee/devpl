package io.devpl.backend.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecurityUtils {

    /**
     * Base64编码
     * @param content 待编码文本
     * @return 编码之后的文本
     */
    public static String base64Encode(String content) {
        return new String(Base64.getEncoder().encode(content.getBytes()), StandardCharsets.UTF_8);
    }

    /**
     * Base64编码
     * @param content 待编码文本
     * @return 编码之后的文本
     */
    public static String base64Decode(String content) {
        return new String(Base64.getDecoder().decode(content.getBytes()), StandardCharsets.UTF_8);
    }
}
