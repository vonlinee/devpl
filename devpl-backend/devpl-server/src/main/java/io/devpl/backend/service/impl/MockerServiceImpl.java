package io.devpl.backend.service.impl;

import io.devpl.backend.domain.vo.MockGeneratorVO;
import io.devpl.backend.service.MockerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockerServiceImpl implements MockerService {

    @Override
    public List<MockGeneratorVO> listAllMockGenerator() {
        List<MockGeneratorVO> result = new ArrayList<>();

        return result;
    }
}
