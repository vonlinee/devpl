package io.devpl.sdk.http;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

/**
 * 自定义的响应处理器
 */
public class ResponseBodyHandler implements HttpResponse.BodyHandler<InputStream> {
    @Override
    public HttpResponse.BodySubscriber<InputStream> apply(HttpResponse.ResponseInfo responseInfo) {
        HttpClient.Version version = responseInfo.version();
        HttpHeaders headers = responseInfo.headers();
        int statusCode = responseInfo.statusCode();
        return new HttpResponseInputStream();
    }
}
