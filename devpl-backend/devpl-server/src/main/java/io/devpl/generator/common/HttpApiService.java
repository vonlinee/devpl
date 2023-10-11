package io.devpl.generator.common;

import org.springframework.web.client.ResponseExtractor;

import java.util.List;

/**
 * 解封装
 */
public interface HttpApiService {

    void setApiAddress(String address);

    <T> List<T> getForList(String api, Class<T> dataClass);

    <T> void setResponseExtractor(ResponseExtractor<T> extractor);
}
