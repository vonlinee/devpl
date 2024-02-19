package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.config.query.*;
import io.devpl.backend.dao.DbConnInfoMapper;
import io.devpl.backend.domain.param.DBTableDataParam;
import io.devpl.backend.domain.param.DbConnInfoListParam;
import io.devpl.backend.domain.vo.DBTableDataVO;
import io.devpl.backend.domain.vo.DataSourceVO;
import io.devpl.backend.domain.vo.TestConnVO;
import io.devpl.backend.entity.DbConnInfo;
import io.devpl.backend.jdbc.JdbcDriverManager;
import io.devpl.backend.service.DataSourceService;
import io.devpl.backend.utils.DBUtils;
import io.devpl.backend.utils.EncryptUtils;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.db.JDBCDriver;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.jdbc.meta.ResultSetColumnMetadata;
import io.devpl.sdk.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
public class DataSourceServiceImpl extends ServiceImpl<DbConnInfoMapper, DbConnInfo> implements DataSourceService {

    /**
     * 程序自身使用的数据源
     */
    private final DataSource dataSource;
    private final JdbcDriverManager driverManager;
    private final DbConnInfoMapper dbConnInfoMapper;

    @Override
    public boolean isSystemDataSource(Long id) {
        return id != null && id == -1;
    }

    @Override
    public DbConnInfo getConnectionInfo(long id) {
        DbConnInfo connInfo = getById(id);
        if (connInfo != null) {
            connInfo.setPassword(EncryptUtils.tryDecrypt(connInfo.getPassword()));
        }
        return connInfo;
    }

    @Override
    public ListResult<DbConnInfo> listPage(DbConnInfoListParam param) {
        LambdaQueryWrapper<DbConnInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(param.getConnName()), DbConnInfo::getConnName, param.getConnName());
        wrapper.eq(StringUtils.hasText(param.getDriverType()), DbConnInfo::getDriverType, param.getDriverType());
        return ListResult.ok(dbConnInfoMapper.selectPage(param, wrapper));
    }

    @Override
    public List<DbConnInfo> listAll() {
        return dbConnInfoMapper.selectList();
    }

    @Override
    public List<DataSourceVO> listIdAndNames() {
        LambdaQueryWrapper<DbConnInfo> qw = new LambdaQueryWrapper<>();
        qw.select(DbConnInfo::getId, DbConnInfo::getConnName);
        return baseMapper.selectList(qw).stream().map(i -> new DataSourceVO(i.getId(), i.getConnName())).toList();
    }

    @Override
    public String getDatabaseProductName(Long dataSourceId) {
        if (dataSourceId.intValue() == -1) {
            return DBType.MYSQL.name();
        } else {
            return getById(dataSourceId).getDbType();
        }
    }

    @Override
    public boolean addOne(DbConnInfo entity) {
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
    public Connection getConnection(DbConnInfo connInfo) throws SQLException {
        if (connInfo.getId() != null && isSystemDataSource(connInfo.getId())) {
            // 本系统连接的数据源
            return dataSource.getConnection();
        }
        return driverManager.getConnection(connInfo.getDriverClassName(), connInfo.getConnUrl(), connInfo.getUsername(), connInfo.getPassword(), null);
    }

    @Override
    public Connection getConnection(Long dataSourceId) throws SQLException {
        if (dataSourceId != null && isSystemDataSource(dataSourceId)) {
            return dataSource.getConnection();
        }
        if (dataSourceId == null) {
            return dataSource.getConnection();
        }

        DbConnInfo connectionInfo = getConnectionInfo(dataSourceId);
        if (connectionInfo == null) {
            return null;
        }
        return getConnection(connectionInfo);
    }

    @Override
    public AbstractQuery getQuery(DBType dbType) {
        AbstractQuery dbQuery = null;
        if (dbType == DBType.MYSQL) {
            dbQuery = new MySqlQuery();
        } else if (dbType == DBType.ORACLE) {
            dbQuery = new OracleQuery();
        } else if (dbType == DBType.POSTGRE_SQL) {
            dbQuery = new PostgreSqlQuery();
        } else if (dbType == DBType.SQL_SERVER) {
            dbQuery = new SQLServerQuery();
        } else if (dbType == DBType.DM) {
            dbQuery = new DmQuery();
        } else if (dbType == DBType.CLICK_HOUSE) {
            dbQuery = new ClickHouseQuery();
        }
        return dbQuery;
    }

    /**
     * 获取连接字符串
     *
     * @param entity 连接信息
     * @return 连接字符串
     */
    @Override
    public String getConnectionUrl(DbConnInfo entity) {
        JDBCDriver jdbcDriver = JDBCDriver.findByDriverClassName(entity.getDriverClassName());
        if (jdbcDriver == null) {
            return null;
        }
        return jdbcDriver.getConnectionUrl(entity.getHost(), entity.getPort(), entity.getDbName(), null);
    }

    @Override
    public List<String> getDbNames(DbConnInfo entity) {
        DBType dbType = DBType.getValue(entity.getDbType());
        if (dbType == null) {
            return Collections.emptyList();
        }
        entity.setConnUrl(getConnectionUrl(entity));

        JDBCDriver driver = dbType.getDriver(0);
        if (driver != null) {
            entity.setDriverClassName(driver.getDriverClassName());
        }

        try (Connection connection = getConnection(entity)) {
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
    public List<String> getTableNames(DbConnInfo connInfo, String databaseName) {
        DBType dbType = DBType.getValue(connInfo.getDbType());
        connInfo.setDbName(databaseName);
        String connectionUrl = this.getConnectionUrl(connInfo);
        if (dbType == null || connectionUrl == null) {
            return Collections.emptyList();
        }
        connInfo.setConnUrl(connectionUrl);
        List<String> list = new ArrayList<>();
        try (Connection connection = getConnection(connInfo)) {
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
        DbConnInfo connInfo = this.getConnectionInfo(dataSourceId);
        if (connInfo != null) {
            return getDbNames(connInfo);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ColumnMetadata> getColumns(DbConnInfo connInfo, String databaseName, String tableName) {
        List<ColumnMetadata> result = new ArrayList<>();
        try (Connection connection = getConnection(connInfo)) {
            DatabaseMetaData dmd = connection.getMetaData();
            String catalog = connection.getCatalog();
            try (ResultSet resultSet = dmd.getColumns(catalog, databaseName, tableName, "%")) {
                result.addAll(DBUtils.extractRows(resultSet, ColumnMetadata.class));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public TestConnVO testJdbcConnection(Long id) {
        TestConnVO vo = new TestConnVO();
        try (Connection connection = this.getConnection(id)) {
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
    public TestConnVO testJdbcConnection(DbConnInfo connInfo) {
        if (!StringUtils.hasText(connInfo.getConnUrl())) {
            connInfo.setConnUrl(getConnectionUrl(connInfo));
        }
        TestConnVO vo = new TestConnVO();
        try (Connection connection = getConnection(connInfo)) {
            DatabaseMetaData metaData = connection.getMetaData();
            vo.setDbmsType(metaData.getDatabaseProductName());
            vo.setUseSsl(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

    @Override
    public DbConnInfo updateOne(DbConnInfo entity) {
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
        param.getConnInfo().setConnUrl(getConnectionUrl(param.getConnInfo()));
        try (Connection connection = getConnection(param.getConnInfo())) {
            String sql = "select * from " + param.getTableName();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    // 表头
                    List<ResultSetColumnMetadata> columnMetadata = DBUtils.getColumnMetadata(resultSet);
                    // 表数据
                    vo.setHeaders(columnMetadata);
                    // vo.setRows(getTableData(resultSet));
                    vo.setRows1(getTableData1(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    private DbConnInfo fixMissingConnectionInfo(DbConnInfo connInfo, boolean saveOrUpdate) {
        if (saveOrUpdate) {
            // 新增
            if (!StringUtils.hasText(connInfo.getHost())) {
                connInfo.setHost("localhost");
            }
            if (connInfo.getPort() == null) {
                connInfo.setPort(3306);
            }
            if (!StringUtils.hasText(connInfo.getDriverType())) {
                connInfo.setDriverType(JDBCDriver.MYSQL8.name());
                connInfo.setDbType(DBType.MYSQL.name());
            }
            if (!StringUtils.hasText(connInfo.getConnName())) {
                connInfo.setConnName(String.join("-", connInfo.getHost(), String.valueOf(connInfo.getPort()), connInfo.getDriverType()));
            }
        } else {
            // 更新
            if (StringUtils.isBlank(connInfo.getDbType()) && StringUtils.hasText(connInfo.getConnUrl())) {
                DBType dbType = JdbcUtils.getDbType(connInfo.getConnUrl());
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
            DBType dbType = DBType.getValue(connInfo.getDbType(), null);
            if (dbType != null) {
                connInfo.setDriverClassName(dbType.getDriverClassName());
                connInfo.setDbType(dbType.name());
            }
        }
        if (!StringUtils.hasText(connInfo.getConnUrl())) {
            connInfo.setConnUrl(getConnectionUrl(connInfo));
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
}
