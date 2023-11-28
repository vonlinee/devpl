package io.devpl.backend.service;

import io.devpl.backend.domain.vo.MockGeneratorVO;

import java.util.List;

public interface MockerService {

    List<MockGeneratorVO> listAllMockGenerator();
}
