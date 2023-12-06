package io.devpl.backend.service;

import io.devpl.backend.domain.param.Model2DDLParam;

public interface DevToolsService {
    String model2DDL(Model2DDLParam param);
}
