package io.devpl.generator.controller;

import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.Query;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.entity.GenTableField;
import io.devpl.generator.service.TableFieldService;
import io.devpl.generator.service.TableService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据表管理
 */
@RestController
@RequestMapping("/gen/table")
public class TableController {

    @Resource
    TableService tableService;
    @Resource
    TableFieldService tableFieldService;

    /**
     * 分页
     *
     * @param query 查询参数
     */
    @GetMapping("page")
    public ListResult<GenTable> page(Query query) {
        return tableService.page(query);
    }

    /**
     * 获取表信息
     *
     * @param id 表ID
     */
    @GetMapping("{id}")
    public Result<GenTable> get(@PathVariable("id") Long id) {
        GenTable table = tableService.getById(id);
        // 获取表的字段
        table.setFieldList(tableFieldService.listByTableId(table.getId()));
        return Result.ok(table);
    }

    /**
     * 修改
     *
     * @param table 表信息
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody GenTable table) {
        return Result.ok(tableService.updateById(table));
    }

    /**
     * 删除
     *
     * @param ids 表id数组
     */
    @DeleteMapping
    public Result<Boolean> delete(@RequestBody Long[] ids) {
        return Result.ok(tableService.deleteBatchIds(ids));
    }

    /**
     * 同步表结构
     *
     * @param id 表ID
     */
    @PostMapping("sync/{id}")
    public Result<String> sync(@PathVariable("id") Long id) {
        tableService.sync(id);
        return Result.ok();
    }

    /**
     * 导入数据源中的表到gen_table
     *
     * @param datasourceId  数据源ID
     * @param tableNameList 表名列表
     */
    @PostMapping("import/{datasourceId}")
    public Result<String> tableImport(@PathVariable("datasourceId") Long datasourceId, @RequestBody List<String> tableNameList) {
        for (String tableName : tableNameList) {
            tableService.importTable(datasourceId, tableName);
        }
        return Result.ok();
    }

    /**
     * 修改表字段数据
     *
     * @param tableId        表ID
     * @param tableFieldList 字段列表
     */
    @PutMapping("field/{tableId}")
    public Result<String> updateTableField(@PathVariable("tableId") Long tableId, @RequestBody List<GenTableField> tableFieldList) {
        tableFieldService.updateTableField(tableId, tableFieldList);
        return Result.ok();
    }
}
