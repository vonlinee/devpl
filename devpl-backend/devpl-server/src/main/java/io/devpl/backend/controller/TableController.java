package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.GenTableListParam;
import io.devpl.backend.domain.param.TableImportParam;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.backend.entity.TableGenerationField;
import io.devpl.backend.service.RdbmsConnectionInfoService;
import io.devpl.backend.service.TableGenerationFieldService;
import io.devpl.backend.service.TableGenerationService;
import io.devpl.codegen.db.DBType;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据表管理
 */
@RestController
@RequestMapping("/api/filegen/table")
public class TableController {

    @Resource
    TableGenerationService tableService;
    @Resource
    TableGenerationFieldService tableFieldService;
    @Resource
    RdbmsConnectionInfoService rdbmsConnectionInfoService;

    /**
     * 分页
     *
     * @param param 查询参数
     */
    @GetMapping("/page")
    public ListResult<TableGeneration> page(GenTableListParam param) {
        return tableService.pageByCondition(param);
    }

    /**
     * 获取表信息
     *
     * @param id 表ID
     */
    @GetMapping("/{id}")
    public Result<TableGeneration> get(@PathVariable("id") Long id) {
        TableGeneration table = tableService.getById(id);
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
    public Result<Boolean> update(@RequestBody TableGeneration table) {
        return Result.ok(tableService.updateById(table));
    }

    /**
     * 删除
     *
     * @param ids 表id数组
     */
    @DeleteMapping("/remove")
    public Result<Boolean> delete(@RequestBody Long[] ids) {
        return Result.ok(tableService.batchRemoveTablesById(ids));
    }

    /**
     * 同步表结构
     *
     * @param id 表ID
     */
    @PostMapping("/sync/{id}")
    public Result<String> sync(@PathVariable("id") Long id) {
        tableService.sync(id);
        return Result.ok();
    }

    /**
     * 导入数据源中的表到gen_table
     *
     * @param param 数据源ID
     */
    @PostMapping("/import")
    public Result<String> tableImport(@RequestBody TableImportParam param) {
        RdbmsConnectionInfo connInfo = rdbmsConnectionInfoService.getConnectionInfo(param.getDataSourceId());
        if (connInfo == null) {
            return Result.error("数据源不存在");
        }
        DBType dbType = DBType.getValue(connInfo.getDbType());
        if (dbType == null) {
            return Result.error("数据库类型" + connInfo.getDbType() + "不存在");
        }
        param.setConnInfo(connInfo);
        param.setDbType(dbType);
        tableService.importTable(param);
        return Result.ok();
    }

    /**
     * 修改表字段数据
     *
     * @param tableId        表ID
     * @param tableFieldList 字段列表
     */
    @PutMapping("/field/{tableId}")
    public Result<String> updateTableField(@PathVariable("tableId") Long tableId, @RequestBody List<TableGenerationField> tableFieldList) {
        tableFieldService.updateTableField(tableId, tableFieldList);
        return Result.ok();
    }
}
