package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.jdbc.DBType;
import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.config.query.*;
import io.devpl.generator.dao.DataSourceMapper;
import io.devpl.generator.domain.param.Query;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.domain.vo.TestConnVO;
import io.devpl.generator.entity.DbConnInfo;
import io.devpl.generator.jdbc.JdbcDriverManager;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.utils.EncryptUtils;
import io.devpl.generator.utils.JdbcUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据源管理
 */
@Service
@AllArgsConstructor
@Slf4j
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
    public ListResult<DbConnInfo> listPage(Query query) {
        Page<DbConnInfo> page = new Page<>(query.getPage(), query.getLimit());
        page.addOrder(OrderItem.desc("id"));
        QueryWrapper<DbConnInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.hasText(query.getDbType()), "db_type", query.getDbType());
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
        return super.save(fixMissingFieldValue(entity));
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

        String connectionUrl = getConnectionUrl(entity);
        if (dbType == null || connectionUrl == null) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>();
        try (Connection connection = JdbcUtils.getConnection(connectionUrl, entity.getUsername(), entity.getPassword(), dbType)) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet catalogs = metaData.getCatalogs()) {
                JdbcUtils.extractSingleColumn(catalogs, list);
            }
        } catch (Exception exception) {
            log.error("", exception);
        }
        return list;
    }

    @Override
    public List<String> getTableNames(DbConnInfo connInfo, String databaseName) {
        DBType dbType = DBType.getValue(connInfo.getDbType());
        String connectionUrl = getConnectionUrl(connInfo);
        if (dbType == null || connectionUrl == null) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        try (Connection connection = JdbcUtils.getConnection(connectionUrl, connInfo.getUsername(), connInfo.getPassword(), dbType)) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

    @Override
    public DbConnInfo updateOne(DbConnInfo entity) {
        updateById(fixMissingFieldValue(entity));
        return entity;
    }

    /**
     * 补齐未填的字段
     *
     * @param connInfo 连接信息
     * @return 连接信息
     */
    private DbConnInfo fixMissingFieldValue(DbConnInfo connInfo) {
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
