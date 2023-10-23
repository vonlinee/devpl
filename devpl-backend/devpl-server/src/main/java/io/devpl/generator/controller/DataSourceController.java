package io.devpl.generator.controller;

import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.config.ConnectionInfo;
import io.devpl.generator.entity.DataSourceInfo;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.service.TableService;
import io.devpl.generator.utils.JdbcUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 数据源管理
 */
@Slf4j
@RestController
@RequestMapping("/api/gen")
public class DataSourceController {

    @Resource
    DataSourceService datasourceService;
    @Resource
    TableService tableService;

    /**
     * 获取数据源列表
     *
     * @param query 查询参数
     * @return 分页查询结果
     */
    @GetMapping("/datasource/page")
    public Result<PageResult<DataSourceInfo>> page(Query query) {
        return Result.ok(datasourceService.page(query));
    }

    /**
     * 数据源列表
     *
     * @return 数据源列表
     */
    @GetMapping("/datasource/list")
    public Result<List<DataSourceInfo>> list() {
        return Result.ok(datasourceService.getList());
    }

    /**
     * 根据ID获取数据源
     *
     * @return 数据源信息
     */
    @GetMapping("/datasource/{id}")
    public Result<DataSourceInfo> get(@PathVariable("id") Long id) {
        DataSourceInfo data = datasourceService.getById(id);
        return Result.ok(data);
    }

    @GetMapping("/datasource/test/{id}")
    public Result<String> test(@PathVariable("id") Long id) {
        try {
            DataSourceInfo entity = datasourceService.getById(id);
            JdbcUtils.getConnection(new ConnectionInfo(entity));
            return Result.ok("连接成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("连接失败，请检查配置信息");
        }
    }

    /**
     * 保存数据源连接信息
     *
     * @param entity 数据库连接信息
     * @return 是否成功
     */
    @PostMapping("/datasource")
    public Result<Boolean> save(@RequestBody DataSourceInfo entity) {
        return Result.ok(datasourceService.save(entity));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param entity 连接信息
     * @return 数据库名称列表
     */
    @PostMapping(value = "/datasource/dbnames")
    public Result<List<String>> getDbNames(@RequestBody DataSourceInfo entity) {
        return Result.ok(datasourceService.getDbNames(entity));
    }

    @PutMapping("/datasource")
    public Result<Boolean> update(@RequestBody DataSourceInfo entity) {
        return Result.ok(datasourceService.updateById(entity));
    }

    @DeleteMapping("/datasource")
    public Result<Boolean> delete(@RequestBody Long[] ids) {
        return Result.ok(datasourceService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 根据数据源ID，获取全部数据表
     *
     * @param id 数据源ID
     */
    @GetMapping("/datasource/table/list/{id}")
    public Result<List<GenTable>> tableList(@PathVariable("id") Long id) {
        try {
            // 获取数据源
            ConnectionInfo datasource = datasourceService.findById(id);
            // 根据数据源，获取全部数据表
            List<GenTable> tableList = tableService.getTableList(datasource);
            return Result.ok(tableList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("数据源配置错误，请检查数据源配置！");
        }
    }
}
