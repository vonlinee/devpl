package io.devpl.backend.service;

import io.devpl.backend.domain.param.Model2DDLParam;
import io.devpl.backend.domain.param.TableCreatorParam;

public interface DevToolsService {
    String model2DDL(Model2DDLParam param);

    String getCreateTableDDL(TableCreatorParam param);
}
