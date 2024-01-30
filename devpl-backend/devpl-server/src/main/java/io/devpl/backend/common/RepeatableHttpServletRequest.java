package io.devpl.backend.common;

import io.devpl.sdk.io.IOUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <a href="https://cloud.tencent.com/developer/article/1398795">重复读取HttpServletRequest的HTTP请求体数据</a>
 */
public class RepeatableHttpServletRequest extends HttpServletRequestWrapper {

    /**
     * 缓存字节数据
     */
    private final byte[] bytes;

    public RepeatableHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        bytes = IOUtils.toByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private int lastIndexRetrieved = -1;
            private ReadListener readListener = null;

            @Override
            public boolean isFinished() {
                return (lastIndexRetrieved == bytes.length - 1);
            }

            @Override
            public boolean isReady() {
                // This implementation will never block
                // We also never need to call the readListener from this method, as this method will never return false
                return isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }

            @Override
            public int read() throws IOException {
                int i;
                if (!isFinished()) {
                    i = bytes[lastIndexRetrieved + 1];
                    lastIndexRetrieved++;
                    if (isFinished() && (readListener != null)) {
                        try {
                            readListener.onAllDataRead();
                        } catch (IOException ex) {
                            readListener.onError(ex);
                            throw ex;
                        }
                    }
                    return i;
                }
                return -1;
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        return new BufferedReader(new InputStreamReader(is));
    }
}
