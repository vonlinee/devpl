package io.devpl.generator.common;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ApiServiceImpl implements ApiService {

    @Resource
    RestTemplate restTemplate;

    @Override
    public void setAddress(String address) {

    }

    @Override
    public <T> List<T> getForList(String api, Class<T> dataClass) {
        return null;
    }
}
