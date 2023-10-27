package io.devpl.generator.utils;

import io.devpl.sdk.io.IOUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

public abstract class ServletUtils {

    private static final Logger log = LoggerFactory.getLogger(ServletUtils.class);

    public static void downloadFile(HttpServletResponse response, String filename, ByteArrayOutputStream byteArrayOutputStream) {
        downloadFile(response, filename, byteArrayOutputStream.toByteArray());
    }

    /**
     * 浏览器直接下载文件
     *
     * @param response HttpServletResponse
     * @param filename 保存文件名
     * @param data     文件字节
     */
    public static void downloadFile(HttpServletResponse response, String filename, byte[] data) {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.addHeader("Content-Length", String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            IOUtils.write(data, outputStream);
        } catch (Exception exception) {
            log.error("failed to download file", exception);
        }
    }
}
