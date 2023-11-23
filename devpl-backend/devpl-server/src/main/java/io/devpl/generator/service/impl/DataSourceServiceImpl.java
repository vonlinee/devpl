package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.jdbc.DBType;
import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.config.query.*;
import io.devpl.generator.dao.DataSourceMapper;
import io.devpl.generator.domain.param.DBTableDataParam;
import io.devpl.generator.domain.param.DbConnInfoListParam;
import io.devpl.generator.domain.vo.DBTableDataVO;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.domain.vo.TestConnVO;
import io.devpl.generator.entity.DbConnInfo;
import io.devpl.generator.jdbc.JdbcDriverManager;
import io.devpl.generator.jdbc.metadata.ResultSetColumnMetadata;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.utils.EncryptUtils;
import io.devpl.generator.utils.JdbcUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数据源管理
 * TODO 结合数据库连接池
 */
@Slf4j
@Service
@AllArgsConstructor
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DbConnInfo> implements DataSourceService {

    /**
     * 程序内部的数据源
     */
    private final DataSource dataSource;
    private final JdbcDriverManager driverManager;

    @Override
    public DbConnInfo getOne(long id) {
        DbConnInfo connInfo = getById(id);
        if (connInfo != null) {
            connInfo.setPassword(EncryptUtils.tryDecrypt(connInfo.getPassword()));
        }
        return connInfo;
    }

    @Override
    public ListResult<DbConnInfo> listPage(DbConnInfoListParam param) {
        Page<DbConnInfo> page = new Page<>(param.getPage(), param.getLimit());
        LambdaQueryWrapper<DbConnInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(param.getConnName()), DbConnInfo::getConnName, param.getConnName());
        wrapper.eq(StringUtils.hasText(param.getDriverType()), DbConnInfo::getDriverType, param.getDriverType());
        page = baseMapper.selectPage(page, wrapper);
        return ListResult.ok(page);
    }

    @Override
    public List<DbConnInfo> listAll() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<DataSourceVO> listIdAndNames() {
        LambdaQueryWrapper<DbConnInfo> qw = new LambdaQueryWrapper<>();
        qw.select(DbConnInfo::getId, DbConnInfo::getConnName);
        return baseMapper.selectList(qw).stream().map(i -> new DataSourceVO(i.getId(), i.getConnName())).toList();
    }

    @Override
    public String getDatabaseProductName(Long dataSourceId) {
        if (dataSourceId.intValue() == 0) {
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
        if (connInfo.getId() != null && connInfo.getId().intValue() == 0) {
            // 本系统连接的数据源
            return dataSource.getConnection();
        }
        return driverManager.getConnection(connInfo.getDriverClassName(), connInfo.getConnUrl(), connInfo.getUsername(), connInfo.getPassword(), null);
    }

    @Override
    public Connection getConnection(Long dataSourceId) throws SQLException {
        if (dataSourceId != null && dataSourceId == 0) {
            return dataSource.getConnection();
        }
        return getConnection(getOne(dataSourceId));
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
        List<String> list = null;
        try (Connection connection = getConnection(entity)) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet catalogs = metaData.getCatalogs()) {
                list = JdbcUtils.extractSingleColumn(catalogs, String.class);
            }
        } catch (Exception exception) {
            log.error("", exception);
        }
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<String> getTableNames(DbConnInfo connInfo, String databaseName) {
        DBType dbType = DBType.getValue(connInfo.getDbType());
        String connectionUrl = getConnectionUrl(connInfo);
        if (dbType == null || connectionUrl == null) {
            return Collections.emptyList();
        }
        connInfo.setConnUrl(connectionUrl);
        List<String> list = new ArrayList<>();
        try (Connection connection = getConnection(connInfo)) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(databaseName, null, null, null);
            while (rs.next()) {
                list.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception exception) {
            log.error("", exception);
        }
        return list;
    }

    @Override
    public TestConnVO testJdbcConnection(Long id) {
        TestConnVO vo = new TestConnVO();
        try (Connection connection = this.getConnection(id)) {
            getTestConnectionInfo(connection);
        } catch (SQLException e) {
            vo.setErrorMsg(e.getMessage());
            vo.setFailed(true);
        }
        return vo;
    }

    public String getTestConnectionInfo(Connection connection) {
        return "";
    }

    @Override
    public TestConnVO testJdbcConnection(DbConnInfo connInfo) {
        if (!StringUtils.hasText(connInfo.getConnUrl())) {
            connInfo.setConnUrl(getConnectionUrl(connInfo));
        }
        TestConnVO vo = new TestConnVO();
        try (Connection connection = getConnection(connInfo)) {
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            vo.setDbmsType(databaseProductName);
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
                    List<ResultSetColumnMetadata> columnMetadata = JdbcUtils.getColumnMetadata(resultSet);
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
        return JdbcUtils.extractMaps(resultSet);
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
