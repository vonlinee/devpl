package io.devpl.generator.common;

import jakarta.annotation.Resource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RestTemplateHttpApiService implements HttpApiService {

    @Resource
    RestTemplate restTemplate;

    @Override
    public void setApiAddress(String address) {

    }

    @Override
    public <T> List<T> getForList(String api, Class<T> dataClass) {
        return null;
    }

    @Override
    public <T> void setResponseExtractor(ResponseExtractor<T> extractor) {

    }
}
