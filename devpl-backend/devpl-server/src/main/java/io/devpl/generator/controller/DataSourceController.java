package io.devpl.generator.controller;

import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.utils.Result;
import io.devpl.generator.config.DataSourceInfo;
import io.devpl.generator.entity.GenDataSource;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.service.TableService;
import io.devpl.generator.utils.DbUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 数据源管理
 */
@Slf4j
@RestController
@RequestMapping("/gen")
@AllArgsConstructor
public class DataSourceController {
    private final DataSourceService datasourceService;
    private final TableService tableService;

    /**
     * 获取数据源列表
     * @param query 查询参数
     * @return 分页查询结果
     */
    @GetMapping("/datasource/page")
    public Result<PageResult<GenDataSource>> page(Query query) {
        PageResult<GenDataSource> page = datasourceService.page(query);
        return Result.ok(page);
    }

    /**
     * 数据源列表
     * @return 数据源列表
     */
    @GetMapping("/datasource/list")
    public Result<List<GenDataSource>> list() {
        List<GenDataSource> list = datasourceService.getList();
        return Result.ok(list);
    }

    /**
     * 根据ID获取数据源
     * @return 数据源信息
     */
    @GetMapping("/datasource/{id}")
    public Result<GenDataSource> get(@PathVariable("id") Long id) {
        GenDataSource data = datasourceService.getById(id);
        return Result.ok(data);
    }

    @GetMapping("/datasource/test/{id}")
    public Result<String> test(@PathVariable("id") Long id) {
        try {
            GenDataSource entity = datasourceService.getById(id);
            DbUtils.getConnection(new DataSourceInfo(entity));
            return Result.ok("连接成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("连接失败，请检查配置信息");
        }
    }

    @PostMapping("/datasource")
    public Result<String> save(@RequestBody GenDataSource entity) {
        datasourceService.save(entity);
        return Result.ok();
    }

    @PutMapping("/datasource")
    public Result<String> update(@RequestBody GenDataSource entity) {
        datasourceService.updateById(entity);
        return Result.ok();
    }

    @DeleteMapping("/datasource")
    public Result<String> delete(@RequestBody Long[] ids) {
        datasourceService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 根据数据源ID，获取全部数据表
     * @param id 数据源ID
     */
    @GetMapping("/datasource/table/list/{id}")
    public Result<List<GenTable>> tableList(@PathVariable("id") Long id) {
        try {
            // 获取数据源
            DataSourceInfo datasource = datasourceService.findById(id);
            // 根据数据源，获取全部数据表
            List<GenTable> tableList = tableService.getTableList(datasource);
            return Result.ok(tableList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("数据源配置错误，请检查数据源配置！");
        }
    }
}
