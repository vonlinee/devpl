package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.bo.TableImportInfo;
import io.devpl.backend.domain.param.GenTableListParam;
import io.devpl.backend.domain.param.TableImportParam;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.backend.entity.TableGenerationField;
import io.devpl.backend.service.RdbmsConnectionInfoService;
import io.devpl.backend.service.TableFileGenerationService;
import io.devpl.backend.service.TableGenerationFieldService;
import io.devpl.backend.service.TableGenerationService;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文件生成表管理
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
    @Resource
    TableFileGenerationService tableFileGenerationService;
    @Resource
    TableGenerationService tableGenerationService;

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
     * 获取表信息及相关的所有数据
     *
     * @param id 表ID
     */
    @GetMapping("/{id}")
    public Result<TableGeneration> get(@PathVariable("id") Long id) {
        TableGeneration table = tableService.getById(id);
        // 获取表的字段
        table.setFieldList(tableFieldService.listByTableId(table.getId()));
        // 获取表生成的文件
        table.setGenerationFiles(tableFileGenerationService.listByTableId(table.getId()));
        return Result.ok(table);
    }

    /**
     * 修改表生成基本信息及字段信息
     *
     * @param table 表信息
     */
    @PutMapping("/edit")
    public Result<Boolean> editTableGeneration(@RequestBody TableGeneration table) {
        // 保存字段信息
        if (!CollectionUtils.isEmpty(table.getFieldList())) {
            tableFieldService.updateTableField(table.getId(), table.getFieldList());
        }
        // 保存生成的文件信息
        if (!CollectionUtils.isEmpty(table.getGenerationFiles())) {
            tableFileGenerationService.updateBatchById(table.getGenerationFiles());
        }

        // 更新表信息
        Map<String, Object> dataModel = tableGenerationService.initTableTemplateArguments(table);
        CollectionUtils.merge(dataModel, table.getTemplateArguments());
        table.setTemplateArguments(dataModel);
        tableService.updateById(table);
        return Result.ok(true);
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
     * 同步表生成配置
     *
     * @param id 表ID
     */
    @PostMapping("/sync/{id}")
    public Boolean syncTable(@PathVariable("id") Long id) {
        return tableService.sync(id);
    }

    /**
     * 导入数据源中的表到 table_generation
     *
     * @param param 数据源ID
     */
    @PostMapping("/import")
    public Result<Integer> tableImport(@RequestBody TableImportParam param) {
        RdbmsConnectionInfo connInfo = rdbmsConnectionInfoService.getConnectionInfo(param.getDataSourceId());
        if (connInfo == null) return Result.error("数据源不存在");
        param.setConnInfo(connInfo);
        BuiltinDatabaseType dbType = BuiltinDatabaseType.getValue(connInfo.getDbType());
        if (dbType == null) return Result.error("数据库类型" + connInfo.getDbType() + "不存在");
        param.setDbType(dbType);
        if (!CollectionUtils.isEmpty(param.getTables())) {
            List<TableImportInfo> tables = param.getTables();
            // 过滤重复导入的表信息
            Map<Long, List<TableImportInfo>> groupByDsId = CollectionUtils.groupingBy(tables, TableImportInfo::getDataSourceId);
            for (Map.Entry<Long, List<TableImportInfo>> entry : groupByDsId.entrySet()) {
                List<TableImportInfo> tablesOfSingleDataSource = entry.getValue();
                Map<String, List<TableImportInfo>> groupByDatabaseName = CollectionUtils.groupingBy(tablesOfSingleDataSource, TableImportInfo::getDatabaseName);
                for (Map.Entry<String, List<TableImportInfo>> dbEntry : groupByDatabaseName.entrySet()) {
                    List<TableImportInfo> importedTables = tableService.listImportedTables(entry.getKey(), dbEntry.getKey(), null);
                    for (TableImportInfo table : dbEntry.getValue()) {
                        if (importedTables.contains(table)) {
                            param.getTables().remove(table);
                        }
                    }
                }
            }
        }
        int count = 0;
        for (TableImportInfo table : param.getTables()) {
            table.setProjectId(param.getProjectId());
            table.setDbType(param.getDbType());
            count += tableService.importSingleTable(table) ? 1 : 0;
        }
        return Result.ok(count);
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
