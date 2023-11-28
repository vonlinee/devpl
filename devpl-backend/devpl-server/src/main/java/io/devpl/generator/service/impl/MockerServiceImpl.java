package io.devpl.generator.service.impl;

import io.devpl.generator.domain.vo.MockGeneratorVO;
import io.devpl.generator.mock.MockValueGenerator;
import io.devpl.generator.mock.MockValueType;
import io.devpl.generator.service.MockerService;
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
