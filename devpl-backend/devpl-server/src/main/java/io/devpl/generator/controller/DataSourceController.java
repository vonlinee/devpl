package io.devpl.generator.controller;

import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.config.DbType;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.domain.vo.DbTypeVO;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.entity.JdbcConnInfo;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.service.TableService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public Result<PageResult<JdbcConnInfo>> page(Query query) {
        return Result.ok(datasourceService.page(query));
    }

    /**
     * 数据源列表
     *
     * @return 数据源列表
     */
    @GetMapping("/datasource/list")
    public Result<List<JdbcConnInfo>> list() {
        return Result.ok(datasourceService.getList());
    }

    /**
     * 数据源ID和名称列表
     *
     * @return 数据源列表
     */
    @GetMapping("/datasource/list/selectable")
    public Result<List<DataSourceVO>> listSelectableDataSources() {
        return Result.ok(datasourceService.listIdAndNames());
    }

    /**
     * 根据ID获取数据源
     *
     * @return 数据源信息
     */
    @GetMapping("/datasource/{id}")
    public Result<JdbcConnInfo> get(@PathVariable("id") Long id) {
        JdbcConnInfo data = datasourceService.getById(id);
        return Result.ok(data);
    }

    @GetMapping("/datasource/test/{id}")
    public Result<String> test(@PathVariable("id") Long id) {
        try {
            return Result.ok(datasourceService.testJdbcConnection(id));
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
    public Result<Boolean> save(@RequestBody JdbcConnInfo entity) {
        entity.setDriverClassName(DbType.getValue(entity.getDbType()).getDriverClassName());
        return Result.ok(datasourceService.save(entity));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param id 数据源ID
     * @return 数据库名称列表
     */
    @GetMapping(value = "/datasource/dbnames/{dataSourceId}")
    public Result<List<String>> getDbNames(@PathVariable(value = "dataSourceId") Long id) {
        JdbcConnInfo connInfo = datasourceService.getById(id);
        if (connInfo == null) {
            return Result.error("资源不存在");
        }
        return Result.ok(datasourceService.getDbNames(connInfo));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param entity 连接信息
     * @return 数据库名称列表
     */
    @PostMapping(value = "/datasource/dbnames")
    public Result<List<String>> getDbNames(@RequestBody JdbcConnInfo entity) {
        return Result.ok(datasourceService.getDbNames(entity));
    }

    /**
     * 获取支持的所有数据库类型列表
     *
     * @return 数据库类型列表
     */
    @GetMapping(value = "/datasource/dbtypes")
    public Result<List<DbTypeVO>> getSupportedDbTypes() {
        List<DbTypeVO> result = new ArrayList<>();
        for (DbType item : DbType.values()) {
            result.add(new DbTypeVO(item.name(), item.getName()));
        }
        return Result.ok(result);
    }

    /**
     * 更新数据源信息
     *
     * @param entity 数据源信息
     * @return 是否成功
     */
    @PutMapping("/datasource")
    public Result<Boolean> update(@RequestBody JdbcConnInfo entity) {
        return Result.ok(datasourceService.updateById(entity));
    }

    /**
     * 批量删除数据源
     *
     * @param ids 数据源ID
     * @return 是否成功
     */
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
            // 根据数据源，获取全部数据表
            return Result.ok(tableService.getTableList(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("数据源配置错误，请检查数据源配置！");
        }
    }
}
