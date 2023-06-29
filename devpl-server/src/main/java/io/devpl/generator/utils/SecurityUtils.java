package io.devpl.generator.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecurityUtils {

    public static String base64Encode(String content) {
        return new String(Base64.getEncoder().encode(content.getBytes()), StandardCharsets.UTF_8);
    }
}
