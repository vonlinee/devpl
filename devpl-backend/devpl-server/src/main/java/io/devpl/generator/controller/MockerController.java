package io.devpl.generator.controller;

import com.baomidou.mybatisplus.generator.jdbc.meta.ColumnMetadata;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.MockColumnListParam;
import io.devpl.generator.domain.vo.MockField;
import io.devpl.generator.domain.vo.MockGeneratorVO;
import io.devpl.generator.entity.DbConnInfo;
import io.devpl.generator.service.DataSourceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tools/mock")
public class MockerController {

    @Resource
    DataSourceService dataSourceService;

    @GetMapping(value = "/columns")
    public Result<List<MockField>> getMockItems(MockColumnListParam param) {
        DbConnInfo connInfo = dataSourceService.getOne(param.getDataSourceId());
        if (connInfo == null) {
            return Result.error("数据源不存在");
        }
        List<ColumnMetadata> columns = dataSourceService.getColumns(connInfo, param.getDatabaseName(), param.getTableName());
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
        return Result.ok(new ArrayList<>());
    }
}
