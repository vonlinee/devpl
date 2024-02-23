package io.devpl.backend.service.impl;

import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.domain.param.Model2DDLParam;
import io.devpl.backend.domain.param.TableCreatorParam;
import io.devpl.backend.service.DevToolsService;
import io.devpl.backend.service.FieldInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DevToolsServiceImpl implements DevToolsService {

    @Resource
    FieldInfoService fieldInfoService;
    @Resource
    DataTypeMappingMapper dataTypeMappingMapper;

    @Override
    public String model2DDL(Model2DDLParam param) {
        FieldParseParam fieldParseParam = new FieldParseParam();
        fieldParseParam.setContent(param.getContent());
        fieldParseParam.setType("java");

        return "";
    }

    @Override
    public String getCreateTableDDL(TableCreatorParam param) {
        return null;
    }
}
