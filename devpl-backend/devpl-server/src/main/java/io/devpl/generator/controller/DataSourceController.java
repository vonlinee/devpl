package io.devpl.generator.controller;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.DBTableDataParam;
import io.devpl.generator.domain.param.DbConnInfoListParam;
import io.devpl.generator.domain.vo.DBTableDataVO;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.domain.vo.DriverTypeVO;
import io.devpl.generator.domain.vo.TestConnVO;
import io.devpl.generator.entity.DbConnInfo;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.service.GenTableService;
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
    private GenTableService tableService;

    /**
     * 获取数据源列表
     *
     * @param query 查询参数
     * @return 分页查询结果
     */
    @GetMapping("/datasource/page")
    public ListResult<DbConnInfo> page(DbConnInfoListParam query) {
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
        return Result.ok(datasourceService.getById(id));
    }

    /**
     * 测试数据库连接
     *
     * @param id 数据源ID
     * @return 连接信息
     */
    @GetMapping("/datasource/test/{id}")
    public Result<TestConnVO> test(@PathVariable("id") Long id) {
        return Result.ok(datasourceService.testJdbcConnection(id));
    }

    /**
     * 测试数据库连接
     *
     * @param connInfo 数据库连接信息
     * @return 连接成功，返回驱动及数据库信息
     */
    @PostMapping("/datasource/connection/test")
    public Result<TestConnVO> test(@RequestBody DbConnInfo connInfo) {
        return Result.ok(datasourceService.testJdbcConnection(connInfo));

    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param id 数据源ID
     * @return 数据库名称列表
     */
    @GetMapping(value = "/datasource/dbnames/{dataSourceId}")
    public Result<List<String>> getDbNames(@PathVariable(value = "dataSourceId") Long id) {
        Assert.notNull(id, "id不能为空");
        DbConnInfo connInfo = datasourceService.getOne(id);
        Assert.notNull(connInfo, "数据源不存在");
        return Result.ok(datasourceService.getDbNames(connInfo));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param entity 连接信息
     * @return 数据库名称列表
     */
    @PostMapping(value = "/datasource/dbnames")
    public Result<List<String>> getDbNames(@RequestBody DbConnInfo entity) {
        return Result.ok(datasourceService.getDbNames(entity));
    }

    /**
     * 获取连接的所有数据库表名称列表
     *
     * @param id     数据源ID
     * @param dbName 数据库名称
     * @return 数据库名称列表
     */
    @GetMapping(value = "/datasource/{dataSourceId}/{dbName}/table/names")
    public ListResult<String> getTableNames(@PathVariable(value = "dataSourceId") Long id, @PathVariable(value = "dbName") String dbName) {
        DbConnInfo connInfo = datasourceService.getOne(id);
        if (connInfo == null) {
            return ListResult.error("资源不存在");
        }
        return ListResult.ok(datasourceService.getTableNames(connInfo, dbName));
    }

    /**
     * 获取支持的所有驱动类型列表
     *
     * @return 数据库类型列表
     */
    @GetMapping(value = "/datasource/drivers")
    public ListResult<DriverTypeVO> getSupportedDbTypes() {
        List<DriverTypeVO> result = new ArrayList<>();
        for (DBType dbType : DBType.values()) {
            JDBCDriver[] drivers = dbType.getDrivers();
            if (drivers != null) {
                for (JDBCDriver driver : drivers) {
                    result.add(new DriverTypeVO(driver.name(), driver.name(), dbType.getDefaultPort()));
                }
            }
        }
        return ListResult.ok(result);
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
    public ListResult<GenTable> tableList(@PathVariable("id") Long id, @RequestParam("tableNamePattern") String tableNamePattern) {
        try {
            // 根据数据源，获取全部数据表
            return ListResult.ok(tableService.getTableList(id, tableNamePattern));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ListResult.error("数据源配置错误，请检查数据源配置！");
        }
    }

    /**
     * 获取数据库表的数据
     *
     * @param param 参数
     * @return 数据库表的数据，包括表头及表格每行的数据
     */
    @PostMapping("/datasource/table/data")
    public Result<DBTableDataVO> getTableData(@RequestBody DBTableDataParam param) {
        try {
            if (param.getConnInfo() == null) {
                DbConnInfo connInfo = datasourceService.getOne(param.getDataSourceId());
                if (connInfo == null) {
                    return Result.error("数据源不存在");
                }
                param.setConnInfo(connInfo);
            }
            // 根据数据源，获取全部数据表
            return Result.ok(datasourceService.getTableData(param));
        } catch (Exception e) {
            log.error("获取数据库表数据失败", e);
            return Result.error("获取数据库表数据失败 " + e.getMessage());
        }
    }
}
