package io.devpl.generator.controller;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.Query;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.domain.vo.DriverTypeVO;
import io.devpl.generator.entity.DbConnInfo;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.service.TableService;
import io.devpl.sdk.validation.Assert;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping("/api/gen")
public class DataSourceController {

    private DataSourceService datasourceService;
    private TableService tableService;

    /**
     * 获取数据源列表
     *
     * @param query 查询参数
     * @return 分页查询结果
     */
    @GetMapping("/datasource/page")
    public ListResult<DbConnInfo> page(Query query) {
        return datasourceService.listPage(query);
    }

    /**
     * 数据源列表
     *
     * @return 数据源列表
     */
    @GetMapping("/datasource/list")
    public ListResult<DbConnInfo> list() {
        return ListResult.ok(datasourceService.listAll());
    }

    /**
     * 数据源ID和名称列表
     *
     * @return 数据源列表
     */
    @GetMapping("/datasource/list/selectable")
    public ListResult<DataSourceVO> listSelectableDataSources() {
        return ListResult.ok(datasourceService.listIdAndNames());
    }

    /**
     * 根据ID获取数据源
     *
     * @return 数据源信息
     */
    @GetMapping("/datasource/{id}")
    public Result<DbConnInfo> get(@PathVariable("id") Long id) {
        DbConnInfo data = datasourceService.getById(id);
        return Result.ok(data);
    }

    /**
     * 测试数据库连接
     *
     * @param id 数据源ID
     * @return 连接信息
     */
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
     * 测试数据库连接
     *
     * @param connInfo 数据库连接信息
     * @return 连接成功，返回驱动及数据库信息
     */
    @PostMapping("/datasource/connection/test")
    public Result<String> test(@RequestBody DbConnInfo connInfo) {
        try {
            return Result.ok(datasourceService.testJdbcConnection(connInfo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("连接失败，请检查配置信息");
        }
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param id 数据源ID
     * @return 数据库名称列表
     */
    @GetMapping(value = "/datasource/dbnames/{dataSourceId}")
    public ListResult<String> getDbNames(@PathVariable(value = "dataSourceId") Long id) {
        Assert.notNull(id, "id不能为空");
        DbConnInfo connInfo = datasourceService.getOne(id);
        Assert.notNull(connInfo, "数据源不存在");
        return ListResult.ok(datasourceService.getDbNames(connInfo));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param entity 连接信息
     * @return 数据库名称列表
     */
    @PostMapping(value = "/datasource/dbnames")
    public ListResult<String> getDbNames(@RequestBody DbConnInfo entity) {
        return ListResult.ok(datasourceService.getDbNames(entity));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param id     数据源ID
     * @param dbName 数据库名称
     * @return 数据库名称列表
     */
    @GetMapping(value = "/datasource/{dataSourceId}/{dbName}/table/names")
    public Result<List<String>> getTableNames(@PathVariable(value = "dataSourceId") Long id, @PathVariable(value = "dbName") String dbName) {
        DbConnInfo connInfo = datasourceService.getOne(id);
        return Result.ok(datasourceService.getTableNames(connInfo, dbName));
    }

    /**
     * 获取支持的所有驱动类型列表
     *
     * @return 数据库类型列表
     */
    @GetMapping(value = "/datasource/drivers")
    public Result<List<DriverTypeVO>> getSupportedDbTypes() {
        List<DriverTypeVO> result = new ArrayList<>();
        for (DBType dbType : DBType.values()) {
            JDBCDriver[] drivers = dbType.getDrivers();
            if (drivers != null) {
                for (JDBCDriver driver : drivers) {
                    result.add(new DriverTypeVO(driver.name(), driver.name(), dbType.getDefaultPort()));
                }
            }
        }
        return Result.ok(result);
    }

    /**
     * 保存/更新数据源连接信息
     *
     * @param entity 数据库连接信息
     * @return 是否成功
     */
    @PostMapping("/datasource")
    public Result<Boolean> save(@RequestBody DbConnInfo entity) {
        if (entity.getId() != null) {
            datasourceService.updateOne(entity);
            return Result.ok();
        }
        entity.setDriverClassName(DBType.getValue(entity.getDbType()).getDriverClassName());
        return Result.ok(datasourceService.addOne(entity));
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
    public ListResult<GenTable> tableList(@PathVariable("id") Long id) {
        try {
            // 根据数据源，获取全部数据表
            return ListResult.ok(tableService.getTableList(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ListResult.error("数据源配置错误，请检查数据源配置！");
        }
    }
}
