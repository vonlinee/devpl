package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.DBTableDataParam;
import io.devpl.backend.domain.param.DataSourceMetadataSyncParam;
import io.devpl.backend.domain.param.DbConnInfoListParam;
import io.devpl.backend.domain.vo.*;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.backend.service.DataSourceService;
import io.devpl.backend.service.RdbmsConnectionInfoService;
import io.devpl.backend.service.TableGenerationService;
import io.devpl.sdk.util.ArrayUtils;
import io.devpl.sdk.validation.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.apache.ddlutils.platform.DriverType;
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
@RequestMapping("/api/datasource")
public class DataSourceController {

    private DataSourceService dataSourceService;
    private RdbmsConnectionInfoService datasourceService;
    private TableGenerationService tableService;

    /**
     * 获取数据源列表
     *
     * @param param 查询参数
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public ListResult<RdbmsConnectionInfo> page(DbConnInfoListParam param) {
        return datasourceService.listPage(param);
    }

    /**
     * 数据源列表
     *
     * @return 数据源列表
     */
    @GetMapping("/list")
    public ListResult<RdbmsConnectionInfo> list() {
        return ListResult.ok(datasourceService.listAll());
    }

    /**
     * 数据源ID和名称列表
     *
     * @return 数据源列表
     */
    @GetMapping("/list/selectable")
    public ListResult<DataSourceVO> listSelectableDataSources(@RequestParam(required = false) String internal) {
        List<DataSourceVO> dataSourceVOS = datasourceService.listIdAndNames();
        if ("true".equals(internal)) {
            dataSourceVOS = new ArrayList<>(dataSourceVOS);
            dataSourceVOS.add(0, new DataSourceVO(-1L, "内置数据源"));
        }
        return ListResult.ok(dataSourceVOS);
    }

    /**
     * 根据ID获取数据源
     *
     * @return 数据源信息
     */
    @GetMapping("/{id}")
    public RdbmsConnectionInfo get(@PathVariable("id") Long id) {
        return datasourceService.getById(id);
    }

    /**
     * 测试数据库连接
     *
     * @param id 数据源ID
     * @return 连接信息
     */
    @GetMapping("/test/{id}")
    public TestConnVO test(@PathVariable("id") Long id) {
        return datasourceService.testJdbcConnection(id);
    }

    /**
     * 测试数据库连接
     *
     * @param connInfo 数据库连接信息
     * @return 连接成功，返回驱动及数据库信息
     */
    @PostMapping("/connection/test")
    public Result<TestConnVO> test(@RequestBody RdbmsConnectionInfo connInfo) {
        return Result.ok(datasourceService.testJdbcConnection(connInfo));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param id 数据源ID
     * @return 数据库名称列表
     */
    @GetMapping(value = "/dbnames/{dataSourceId}")
    public Result<List<String>> getDbNames(@PathVariable(value = "dataSourceId") Long id) {
        Assert.notNull(id, "id不能为空");
        return Result.ok(datasourceService.getDatabaseNames(id));
    }

    /**
     * 获取连接的所有数据库名称列表
     *
     * @param entity 连接信息
     * @return 数据库名称列表
     */
    @PostMapping(value = "/dbnames")
    public List<String> getDbNames(@RequestBody RdbmsConnectionInfo entity) {
        return datasourceService.getDbNames(entity);
    }

    /**
     * 获取连接的所有数据库表名称列表
     *
     * @param id     数据源ID
     * @param dbName 数据库名称
     * @return 数据库名称列表
     */
    @GetMapping(value = "/table/names")
    public Result<List<String>> getTableNames(@RequestParam(value = "dataSourceId") Long id, @RequestParam(value = "databaseName", required = false) String dbName, @RequestParam(value = "pattern", required = false) String tableNamePattern) {
        RdbmsConnectionInfo connInfo = datasourceService.getConnectionInfo(id);
        if (connInfo == null) {
            return Result.error("资源不存在");
        }
        return Result.ok(datasourceService.getTableNames(connInfo, dbName));
    }

    /**
     * 获取支持的所有驱动类型列表
     *
     * @return 数据库类型列表
     */
    @GetMapping(value = "/drivers")
    public ListResult<DriverTypeVO> getSupportedDriverTypes() {
        List<DriverTypeVO> result = new ArrayList<>();
        for (BuiltinDatabaseType dbType : BuiltinDatabaseType.values()) {
            DriverType[] drivers = dbType.getSupportedDriverTypes();
            if (drivers != null) {
                for (DriverType driver : drivers) {
                    result.add(new DriverTypeVO(driver.getName(), driver.getName(), driver.getDefaultPort()));
                }
            }
        }
        return ListResult.ok(result);
    }

    /**
     * 获取支持的所有数据库类型列表
     *
     * @return 数据库类型列表
     */
    @GetMapping(value = "/dbtypes")
    public Result<List<SelectOptionVO>> getSupportedDBTypes() {
        return Result.ok(ArrayUtils.asList(BuiltinDatabaseType.values(),
            dbType -> new SelectOptionVO(dbType.name(), dbType.getName(), dbType.name())));
    }

    /**
     * 保存/更新数据源连接信息
     *
     * @param entity 数据库连接信息
     * @return 是否成功
     */
    @PostMapping("/update")
    public boolean save(@RequestBody RdbmsConnectionInfo entity) {
        if (entity.getId() != null) {
            datasourceService.updateConnectionInfo(entity);
            return true;
        }
        return datasourceService.addOne(entity);
    }

    /**
     * 批量删除数据源
     *
     * @param ids 数据源ID
     * @return 是否成功
     */
    @DeleteMapping("/remove")
    public Result<Boolean> delete(@RequestBody Long[] ids) {
        return Result.ok(datasourceService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 根据数据源ID，获取全部数据表
     *
     * @param id 数据源ID
     */
    @GetMapping("/table/list")
    public ListResult<TableGeneration> tableList(@RequestParam(value = "dataSourceId") Long id, @RequestParam(value = "databaseName", required = false) String databaseName, @RequestParam(value = "tableNamePattern", required = false) String tableNamePattern) {
        try {
            // 根据数据源，获取全部数据表
            return ListResult.ok(tableService.listGenerationTargetTables(id, databaseName, tableNamePattern));
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
    @PostMapping("/table/data")
    public Result<DBTableDataVO> getTableData(@RequestBody DBTableDataParam param) {
        try {
            if (param.getConnInfo() == null) {
                RdbmsConnectionInfo connInfo = datasourceService.getConnectionInfo(param.getDataSourceId());
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

    /**
     * 同步表元数据
     *
     * @return 是否成功
     */
    @PostMapping("/metadata/table/sync")
    public Result<Boolean> syncTableMetadata(DataSourceMetadataSyncParam param) {
        dataSourceService.syncTableMetadata(param);
        return Result.ok();
    }
}
