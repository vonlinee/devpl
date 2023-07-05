package io.devpl.generator.controller;

import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.utils.Result;
import io.devpl.generator.config.GenDataSource;
import io.devpl.generator.entity.DataSourceEntity;
import io.devpl.generator.entity.TableEntity;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.utils.DbUtils;
import io.devpl.generator.utils.GenUtils;
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

    /**
     * 获取数据源列表
     * @param query
     * @return
     */
    @GetMapping("/datasource/page")
    public Result<PageResult<DataSourceEntity>> page(Query query) {
        PageResult<DataSourceEntity> page = datasourceService.page(query);
        return Result.ok(page);
    }

    @GetMapping("/datasource/list")
    public Result<List<DataSourceEntity>> list() {
        List<DataSourceEntity> list = datasourceService.getList();

        return Result.ok(list);
    }

    @GetMapping("/datasource/{id}")
    public Result<DataSourceEntity> get(@PathVariable("id") Long id) {
        DataSourceEntity data = datasourceService.getById(id);

        return Result.ok(data);
    }

    @GetMapping("/datasource/test/{id}")
    public Result<String> test(@PathVariable("id") Long id) {
        try {
            DataSourceEntity entity = datasourceService.getById(id);

            DbUtils.getConnection(new GenDataSource(entity));
            return Result.ok("连接成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("连接失败，请检查配置信息");
        }
    }

    @PostMapping("/datasource")
    public Result<String> save(@RequestBody DataSourceEntity entity) {
        datasourceService.save(entity);

        return Result.ok();
    }

    @PutMapping("/datasource")
    public Result<String> update(@RequestBody DataSourceEntity entity) {
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
    public Result<List<TableEntity>> tableList(@PathVariable("id") Long id) {
        try {
            // 获取数据源
            GenDataSource datasource = datasourceService.get(id);
            // 根据数据源，获取全部数据表
            List<TableEntity> tableList = GenUtils.getTableList(datasource);
            return Result.ok(tableList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("数据源配置错误，请检查数据源配置！");
        }
    }
}
