package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.config.DbType;
import io.devpl.generator.config.query.*;
import io.devpl.generator.dao.DataSourceMapper;
import io.devpl.generator.domain.vo.DataSourceVO;
import io.devpl.generator.entity.DbConnInfo;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.utils.EncryptUtils;
import io.devpl.generator.utils.JdbcUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Override
    public DbConnInfo getOne(long id) {
        DbConnInfo connInfo = getById(id);
        connInfo.setPassword(EncryptUtils.tryDecrypt(connInfo.getPassword()));
        return connInfo;
    }

    @Override
    public PageResult<DbConnInfo> listPage(Query query) {
        Page<DbConnInfo> page = new Page<>(query.getPage(), query.getLimit());
        page.addOrder(OrderItem.desc("id"));
        QueryWrapper<DbConnInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.hasText(query.getDbType()), "db_type", query.getDbType());
        page = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
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
            return DbType.MySQL.name();
        } else {
            return getById(dataSourceId).getDbType();
        }
    }

    @Override
    public boolean addOne(DbConnInfo entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(entity.getCreateTime());
        return super.save(entity);
    }

    @Override
    public Connection getConnection(DbConnInfo connInfo) {
        try {
            if (connInfo.getId().intValue() == 0) {
                // 本系统连接的数据源
                return dataSource.getConnection();
            } else {
                return JdbcUtils.getConnection(connInfo.getConnUrl(), connInfo.getUsername(), connInfo.getPassword(), DbType.valueOf(connInfo.getDbType()));
            }
        } catch (SQLException exception) {
            return null;
        }
    }

    @Override
    @Nullable
    public Connection getConnection(Long dataSourceId) {
        return getConnection(getById(dataSourceId));
    }

    @Override
    public AbstractQuery getQuery(DbType dbType) {
        AbstractQuery dbQuery = null;
        if (dbType == DbType.MySQL) {
            dbQuery = new MySqlQuery();
        } else if (dbType == DbType.Oracle) {
            dbQuery = new OracleQuery();
        } else if (dbType == DbType.PostgreSQL) {
            dbQuery = new PostgreSqlQuery();
        } else if (dbType == DbType.SQLServer) {
            dbQuery = new SQLServerQuery();
        } else if (dbType == DbType.DM) {
            dbQuery = new DmQuery();
        } else if (dbType == DbType.Clickhouse) {
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
        JDBCDriver jdbcDriver = JDBCDriver.valueOfDriverName(entity.getDriverClassName());
        if (jdbcDriver == null) {
            return null;
        }
        return jdbcDriver.getConnectionUrl(entity.getHost(), entity.getPort(), entity.getDbName(), null);
    }

    @Override
    public List<String> getDbNames(DbConnInfo entity) {
        DbType dbType = DbType.getValue(entity.getDbType());

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
        DbType dbType = DbType.getValue(connInfo.getDbType());
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
    public String testJdbcConnection(Long id) {
        if (this.getConnection(id) != null) {
            return "连接成功";
        }
        return "连接失败";
    }

    @Override
    public DbConnInfo updateOne(DbConnInfo entity) {
        if (!StringUtils.hasText(entity.getDriverClassName())) {
            DbType dbType = DbType.getValue(entity.getDbType(), null);
            if (dbType != null) {
                entity.setDriverClassName(dbType.getDriverClassName());
            }
        }
        updateById(entity);
        return entity;
    }
}
