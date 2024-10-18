package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.dao.RdbmsConnectionInfoMapper;
import io.devpl.backend.domain.param.DBTableDataParam;
import io.devpl.backend.domain.param.DbConnInfoListParam;
import io.devpl.backend.domain.vo.DBTableDataVO;
import io.devpl.backend.domain.vo.DataSourceVO;
import io.devpl.backend.domain.vo.TestConnVO;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.backend.jdbc.JdbcDriverManager;
import io.devpl.backend.service.RdbmsConnectionInfoService;
import io.devpl.backend.utils.DBUtils;
import io.devpl.codegen.db.DBTypeEnum;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.common.utils.EncryptUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ddlutils.jdbc.meta.ColumnMetadata;
import org.apache.ddlutils.jdbc.meta.ResultSetColumnMetadata;
import org.apache.ddlutils.platform.DBType;
import org.apache.ddlutils.platform.JDBCDriver;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数据源管理
 */
@Slf4j
@Service
public class RdbmsConnectionInfoServiceImpl extends ServiceImpl<RdbmsConnectionInfoMapper, RdbmsConnectionInfo> implements RdbmsConnectionInfoService, InitializingBean {

    /**
     * 程序自身使用的数据源
     */
    @Resource
    DataSource dataSource;
    @Resource
    JdbcDriverManager driverManager;
    @Resource
    RdbmsConnectionInfoMapper dbConnInfoMapper;
    @Resource
    Environment environment;
    @Value(value = "${devpl.db.name}")
    private String databaseName;

    @Override
    public boolean isSystemDataSource(Long id) {
        return id != null && id == getSystemDataSourceId();
    }

    @Override
    public RdbmsConnectionInfo getConnectionInfo(Long dataSourceId) {
        RdbmsConnectionInfo connInfo = getById(dataSourceId);
        if (connInfo != null) {
            connInfo.setPassword(EncryptUtils.tryDecrypt(connInfo.getPassword()));
        }
        return connInfo;
    }

    @Override
    public long getSystemDataSourceId() {
        return 0L;
    }

    @NotNull
    @Override
    public RdbmsConnectionInfo getInternalConnectionInfo() {
        RdbmsConnectionInfo connectionInfo = new RdbmsConnectionInfo(environment.getProperty("spring.datasource.url"));
        /**
         * 关于使用数据库id自增策略后，无法自行指定id的问题
         * 指定了ID，数据不会被插入，同时不会报错，需要自行确认是否插入
         * https://gitee.com/baomidou/mybatis-plus/issues/I1MY0F
         */
        // connectionInfo.setId(getSystemDataSourceId());
        connectionInfo.setConnectionName("默认数据源");
        connectionInfo.setUsername(environment.getProperty("spring.datasource.username"));
        connectionInfo.setPassword(environment.getProperty("spring.datasource.password"));
        if (StringUtils.hasText(connectionInfo.getPassword())) {
            connectionInfo.setPassword(EncryptUtils.tryEncrypt(connectionInfo.getPassword()));
        }
        return connectionInfo;
    }

    @Override
    public ListResult<RdbmsConnectionInfo> listPage(DbConnInfoListParam param) {
        LambdaQueryWrapper<RdbmsConnectionInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(param.getConnName()), RdbmsConnectionInfo::getConnectionName, param.getConnName());
        wrapper.eq(StringUtils.hasText(param.getDriverType()), RdbmsConnectionInfo::getDriverType, param.getDriverType());
        return ListResult.ok(dbConnInfoMapper.selectPage(param, wrapper));
    }

    @Override
    public List<RdbmsConnectionInfo> listAll() {
        return dbConnInfoMapper.selectDataSources();
    }

    @Override
    public List<DataSourceVO> listIdAndNames() {
        LambdaQueryWrapper<RdbmsConnectionInfo> qw = new LambdaQueryWrapper<>();
        qw.select(RdbmsConnectionInfo::getId, RdbmsConnectionInfo::getConnectionName);
        return baseMapper.selectList(qw).stream().map(i -> new DataSourceVO(i.getId(), i.getConnectionName())).toList();
    }

    @Override
    public String getDatabaseProductName(Long dataSourceId) {
        if (dataSourceId.intValue() == -1) {
            return DBTypeEnum.MYSQL.name();
        } else {
            RdbmsConnectionInfo connectionInfo = getById(dataSourceId);
            if (connectionInfo != null) {
                return connectionInfo.getDbType();
            }
            return null;
        }
    }

    @Override
    public boolean addOne(RdbmsConnectionInfo entity) {
        return super.save(fixMissingConnectionInfo(entity, true));
    }

    /**
     * 获取数据库连接
     * 如果id不为空，则使用系统自带的数据源
     *
     * @param connInfo 连接信息
     * @return 数据库连接
     */
    @Override
    public Connection getRdbmsConnection(RdbmsConnectionInfo connInfo) throws RuntimeSQLException {
        try {
            if (connInfo.getId() != null && isSystemDataSource(connInfo.getId())) {
                // 本系统连接的数据源
                return dataSource.getConnection();
            }
            return driverManager.getConnection(connInfo.getDriverClassName(), connInfo.buildConnectionUrl(), connInfo.getUsername(), connInfo.getPassword(), connInfo.getDriverProperties());
        } catch (SQLException e) {
            throw RuntimeSQLException.wrap(e);
        }
    }

    @Override
    public Connection getConnection(Long dataSourceId, String databaseName, boolean useDatabaseName) throws RuntimeSQLException {
        try {
            if (dataSourceId == null || isSystemDataSource(dataSourceId)) {
                return dataSource.getConnection();
            }
            RdbmsConnectionInfo connectionInfo = getConnectionInfo(dataSourceId);
            if (connectionInfo == null) {
                throw new RuntimeSQLException("数据源%s不存在", dataSourceId);
            }
            if (useDatabaseName) {
                if (StringUtils.hasText(databaseName)) {
                    connectionInfo.setDbName(databaseName);
                }
            } else {
                connectionInfo.setDbName(null); // 不使用数据库名称获取连接
            }
            return getRdbmsConnection(connectionInfo);
        } catch (Throwable e) {
            throw RuntimeSQLException.wrap(e);
        }
    }

    /**
     * 获取连接字符串
     *
     * @param entity 连接信息
     * @return 连接字符串
     */
    @Override
    public String getConnectionUrl(RdbmsConnectionInfo entity) {
        io.devpl.codegen.db.JDBCDriver jdbcDriver = io.devpl.codegen.db.JDBCDriver.findByDriverClassName(entity.getDriverClassName());
        if (jdbcDriver == null) {
            return null;
        }
        return jdbcDriver.getConnectionUrl(entity.getHost(), entity.getPort(), entity.getDbName(), null);
    }

    @Override
    public List<String> getDbNames(RdbmsConnectionInfo entity) {
        DBTypeEnum dbType = DBTypeEnum.getValue(entity.getDbType());
        if (dbType == null) {
            return Collections.emptyList();
        }
        entity.setConnectionUrl(getConnectionUrl(entity));
        JDBCDriver[] supportedDrivers = dbType.getSupportedDrivers();
        JDBCDriver driver = dbType.getDriver(0);
        if (driver != null) {
            entity.setDriverClassName(driver.getDriverClassName());
        }

        try (Connection connection = getRdbmsConnection(entity)) {
            if (connection == null) {
                return Collections.emptyList();
            }
            return getDatabaseNames(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getDatabaseNames(Connection connection) throws SQLException {
        List<String> list;
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet catalogs = metaData.getCatalogs()) {
            list = DBUtils.extractSingleColumn(catalogs, String.class);
        }
        return list;
    }

    @Override
    public List<String> getTableNames(RdbmsConnectionInfo connInfo, String databaseName) {
        DBTypeEnum dbType = DBTypeEnum.getValue(connInfo.getDbType());
        connInfo.setDbName(databaseName);
        String connectionUrl = this.getConnectionUrl(connInfo);
        if (dbType == null || connectionUrl == null) {
            return Collections.emptyList();
        }
        connInfo.setConnectionUrl(connectionUrl);
        List<String> list = new ArrayList<>();
        try (Connection connection = getRdbmsConnection(connInfo)) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog();
            ResultSet rs = metaData.getTables(catalog, databaseName, null, null);
            while (rs.next()) {
                list.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception exception) {
            log.error("获取数据库表名称失败", exception);
        }
        return list;
    }

    @Override
    public List<String> getDatabaseNames(Long dataSourceId) {
        if (isSystemDataSource(dataSourceId)) {
            return Collections.singletonList(databaseName);
        }
        RdbmsConnectionInfo connInfo = this.getConnectionInfo(dataSourceId);
        if (connInfo != null) {
            return getDbNames(connInfo);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ColumnMetadata> getColumns(RdbmsConnectionInfo connInfo, String databaseName, String tableName) {
        List<ColumnMetadata> result = new ArrayList<>();
        try (Connection connection = getRdbmsConnection(connInfo)) {
            DatabaseMetaData dmd = connection.getMetaData();
            String catalog = connection.getCatalog();
            try (ResultSet resultSet = dmd.getColumns(catalog, databaseName, tableName, "%")) {
                result.addAll(DBUtils.extractRows(resultSet, ColumnMetadata.class));
            }
        } catch (SQLException e) {
            throw RuntimeSQLException.wrap(e);
        }
        return result;
    }

    @Override
    public TestConnVO testJdbcConnection(Long id) {
        TestConnVO vo = new TestConnVO();
        try (Connection connection = this.getConnection(id, null, false)) {
            if (connection == null) {
                vo.setFailed(true);
                vo.setErrorMsg("connection is null");
            } else {
                vo.setFailed(false);
                DatabaseMetaData metaData = connection.getMetaData();
                vo.setDbmsType(metaData.getDatabaseProductName());
            }
        } catch (SQLException e) {
            vo.setErrorMsg(e.getMessage());
            vo.setFailed(true);
        }
        return vo;
    }

    @Override
    public TestConnVO testJdbcConnection(RdbmsConnectionInfo connInfo) {
        if (!StringUtils.hasText(connInfo.getConnectionUrl())) {
            connInfo.setConnectionUrl(getConnectionUrl(connInfo));
        }
        TestConnVO vo = new TestConnVO();
        try (Connection connection = getRdbmsConnection(connInfo)) {
            // TODO API兼容性
            DatabaseMetaData metaData = connection.getMetaData();
            vo.setDbmsType(metaData.getDatabaseProductName());
            vo.setDriverName(metaData.getDriverName());
            vo.setDriverVersion(metaData.getDriverVersion());
            vo.setJdbcMajorVersion(metaData.getJDBCMajorVersion());
            vo.setJdbcMinorVersion(metaData.getJDBCMinorVersion());
            vo.setUserName(metaData.getUserName());
            vo.setProductName(metaData.getDatabaseProductName());
            vo.setProductVersion(metaData.getDatabaseProductVersion());
            vo.setUseSsl(false);
        } catch (SQLException e) {
            throw RuntimeSQLException.wrap(e);
        }
        return vo;
    }

    @Override
    public RdbmsConnectionInfo updateConnectionInfo(RdbmsConnectionInfo entity) {
        updateById(fixMissingConnectionInfo(entity, false));
        return entity;
    }

    /**
     * 加载数据库表的数据
     *
     * @param param 参数
     * @return 数据库表数据
     */
    @Override
    public DBTableDataVO getTableData(DBTableDataParam param) {
        DBTableDataVO vo = new DBTableDataVO();
        param.getConnInfo().setDbName(param.getDbName());
        // 更换连接地址
        param.getConnInfo().setConnectionUrl(getConnectionUrl(param.getConnInfo()));
        try (Connection connection = getRdbmsConnection(param.getConnInfo())) {
            String sql = "select * from " + param.getTableName();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // 表头
                    List<ResultSetColumnMetadata> columnMetadata = JdbcUtils.getColumnMetadata(resultSet);
                    // 表数据
                    vo.setHeaders(columnMetadata);
                    // vo.setRows(getTableData(resultSet));
                    vo.setRows1(getTableData1(resultSet));
                }
            }
        } catch (SQLException e) {
            throw RuntimeSQLException.wrap(e);
        }
        return vo;
    }

    /**
     * 以对象的形式进行返回，包含表头
     *
     * @param resultSet 结果集
     * @return 每行的数据
     */
    private List<Map<String, Object>> getTableData1(ResultSet resultSet) throws SQLException {
        return DBUtils.extractMaps(resultSet);
    }

    /**
     * 以字符串的形式进行返回，不包含表头
     *
     * @param resultSet 结果集
     * @return 每行的数据
     */
    private List<String[]> getTableData(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<String[]> result = new ArrayList<>();
        while (resultSet.next()) {
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = String.valueOf(resultSet.getObject(i + 1));
            }
            result.add(row);
        }
        return result;
    }

    /**
     * 补齐未填的字段
     *
     * @param connInfo 连接信息
     * @return 连接信息
     */
    private RdbmsConnectionInfo fixMissingConnectionInfo(RdbmsConnectionInfo connInfo, boolean saveOrUpdate) {
        if (saveOrUpdate) {
            // 新增
            if (!StringUtils.hasText(connInfo.getHost())) {
                connInfo.setHost("localhost");
            }
            if (connInfo.getPort() == null) {
                connInfo.setPort(3306);
            }
            if (!StringUtils.hasText(connInfo.getDriverType())) {
                connInfo.setDriverType(io.devpl.codegen.db.JDBCDriver.MYSQL8.name());
                connInfo.setDbType(DBTypeEnum.MYSQL.name());
            }
            if (!StringUtils.hasText(connInfo.getConnectionName())) {
                connInfo.setConnectionName(String.join("-", connInfo.getHost(), String.valueOf(connInfo.getPort()), connInfo.getDriverType()));
            }
        } else {
            // 更新
            if (StringUtils.isBlank(connInfo.getDbType()) && StringUtils.hasText(connInfo.getConnectionUrl())) {
                DBType dbType = JdbcUtils.getDbType(connInfo.getConnectionUrl());
                if (dbType != null) {
                    connInfo.setDbType(dbType.getName());
                    if (!StringUtils.hasText(connInfo.getDriverClassName())) {
                        for (JDBCDriver driver : dbType.getSupportedDrivers()) {
                            connInfo.setDriverClassName(driver.getDriverClassName());
                        }
                    }
                }
            }
        }
        if (!StringUtils.hasText(connInfo.getDriverClassName())) {
            DBTypeEnum dbType = DBTypeEnum.getValue(connInfo.getDbType(), null);
            if (dbType != null) {
                connInfo.setDriverClassName(dbType.getDriverClassName());
                connInfo.setDbType(dbType.name());
            }
        }
        if (!StringUtils.hasText(connInfo.getConnectionUrl())) {
            connInfo.setConnectionUrl(getConnectionUrl(connInfo));
        }
        if (connInfo.getCreateTime() == null) {
            connInfo.setCreateTime(LocalDateTime.now());
            connInfo.setUpdateTime(connInfo.getCreateTime());
        }
        if (connInfo.getUpdateTime() == null) {
            connInfo.setUpdateTime(LocalDateTime.now());
        }
        return connInfo;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            RdbmsConnectionInfo connectionInfo = baseMapper.getByDataSourceId(getSystemDataSourceId());
            if (connectionInfo == null) {
                connectionInfo = getInternalConnectionInfo();
                baseMapper.insert(connectionInfo);
            } else {
                RdbmsConnectionInfo _connectionInfo = getInternalConnectionInfo();
                _connectionInfo.setId(connectionInfo.getId());
                baseMapper.updateById(_connectionInfo);
            }
        } catch (Exception exception) {
            log.error("初始化系统内置数据源信息失败", exception);
        }
    }
}
