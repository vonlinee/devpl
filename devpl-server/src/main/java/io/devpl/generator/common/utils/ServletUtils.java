package io.devpl.generator.common.utils;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.http.HttpServletResponse;

public class ServletUtils {

    public static void downloadFile(HttpServletResponse response, String filename, byte[] data) {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.addHeader("Content-Length", String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");
        try {
            IoUtil.write(response.getOutputStream(), false, data);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
