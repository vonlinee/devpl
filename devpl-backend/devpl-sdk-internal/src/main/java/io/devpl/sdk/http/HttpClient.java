package io.devpl.sdk.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * HTTP请求工具类
 */
public final class HttpClient {

    private static final java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
        .version(java.net.http.HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(3))
        .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
        .cookieHandler(new CookieManager())
        .build();

    private static final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
        .version(java.net.http.HttpClient.Version.HTTP_2)
        .timeout(Duration.ofSeconds(3));

    /**
     * 构建请求对象
     * @param url
     * @param method
     * @param body
     * @param header
     * @return
     */
    public HttpRequest buildRequest(String url, String method, String body, Map<String, String> header) {
        HttpRequest.Builder builder = requestBuilder.copy();
        builder.uri(URI.create(url));
        // 请求体
        if ("GET".equalsIgnoreCase(method)) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else if ("POST".equalsIgnoreCase(method)) {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body));
        }
        addHeader(builder, header);
        return builder.build();
    }

    /**
     * 添加请求头
     * @param builder HttpRequest.Builder
     * @param header  请求头 header
     */
    private void addHeader(HttpRequest.Builder builder, Map<String, String> header) {
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
    }

    public <T> T send(String url, String method, String body, Map<String, String> header) {
        HttpRequest request = buildRequest(url, method, body, header);
        try {
            HttpResponse<InputStream> response = client.send(request, new ResponseBodyHandler());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
