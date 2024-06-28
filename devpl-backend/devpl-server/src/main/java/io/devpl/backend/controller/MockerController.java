package io.devpl.backend.controller;

import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.MockColumnListParam;
import io.devpl.backend.domain.bo.MockField;
import io.devpl.backend.domain.vo.MockGeneratorVO;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.backend.service.RdbmsConnectionInfoService;
import io.devpl.backend.service.MockerService;
import jakarta.annotation.Resource;
import org.apache.ddlutils.jdbc.meta.ColumnMetadata;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tools/mock")
public class MockerController {

    @Resource
    RdbmsConnectionInfoService rdbmsConnectionInfoService;
    @Resource
    MockerService mockerService;

    @GetMapping(value = "/columns")
    public Result<List<MockField>> getMockItems(MockColumnListParam param) {
        RdbmsConnectionInfo connInfo = rdbmsConnectionInfoService.getConnectionInfo(param.getDataSourceId());
        if (connInfo == null) {
            return Result.error("数据源不存在");
        }
        List<ColumnMetadata> columns = rdbmsConnectionInfoService.getColumns(connInfo, param.getDatabaseName(), param.getTableName());
        List<MockField> mockFields = new ArrayList<>();
        for (ColumnMetadata column : columns) {
            MockField field = new MockField();
            field.setFieldName(column.getColumnName());
            field.setDataType(column.getDataType());
            mockFields.add(field);
        }
        return Result.ok(mockFields);
    }

    @GetMapping(value = "/generators")
    public Result<List<MockGeneratorVO>> getMockGenerators(MockColumnListParam param) {
        return Result.ok(mockerService.listAllMockGenerator());
    }
}
