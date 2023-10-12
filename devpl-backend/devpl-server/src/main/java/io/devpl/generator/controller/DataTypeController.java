package io.devpl.generator.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.DataTypeAddParam;
import io.devpl.generator.entity.DataTypeItem;
import io.devpl.generator.service.IDataTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据类型管理
 */
@RestController
@RequestMapping("/api/datatype")
@AllArgsConstructor
public class DataTypeController {

    IDataTypeService dataTypeService;

    /**
     * 根据ID获取数据源
     *
     * @return 数据源信息
     */
    @PostMapping("/add")
    public Result<?> addDataTypes(@RequestBody DataTypeAddParam param) {
        int i = dataTypeService.saveDataTypes(param.getDataTypeItems());
        return Result.ok(param);
    }

    @GetMapping("/page")
    public Result<Page<DataTypeItem>> listDataTypes(PageQuery param) {
        return Result.ok(dataTypeService.selectPage(param));
    }
}
