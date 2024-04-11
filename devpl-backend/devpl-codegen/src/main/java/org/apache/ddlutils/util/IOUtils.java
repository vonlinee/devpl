package org.apache.ddlutils.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    public static byte[] readAllBytes(InputStream inputStream) {
        try (InputStream input = new BufferedInputStream(inputStream)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
            byte[] data = new byte[1024];
            int numRead;
            while ((numRead = input.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, numRead);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}