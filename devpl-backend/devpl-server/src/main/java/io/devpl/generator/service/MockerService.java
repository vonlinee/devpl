package io.devpl.generator.service;

import io.devpl.generator.domain.vo.MockGeneratorVO;

import java.util.List;

public interface MockerService {

    List<MockGeneratorVO> listAllMockGenerator();
}
