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
import io.devpl.generator.entity.JdbcConnInfo;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.utils.JdbcUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 数据源管理
 */
@Service
@AllArgsConstructor
@Slf4j
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, JdbcConnInfo> implements DataSourceService {

    /**
     * 程序内部的数据源
     */
    private final DataSource dataSource;

    @Override
    public PageResult<JdbcConnInfo> page(Query query) {
        Page<JdbcConnInfo> page = new Page<>(query.getPage(), query.getLimit());
        page.addOrder(OrderItem.desc("id"));

        QueryWrapper<JdbcConnInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getCode()), "code", query.getCode());
        wrapper.like(StringUtils.hasText(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StringUtils.hasText(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(StringUtils.hasText(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StringUtils.hasText(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.hasText(query.getDbType()), "db_type", query.getDbType());
        wrapper.like(StringUtils.hasText(query.getProjectName()), "project_name", query.getProjectName());

        page = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<JdbcConnInfo> getList() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<DataSourceVO> listIdAndNames() {
        LambdaQueryWrapper<JdbcConnInfo> qw = new LambdaQueryWrapper<>();
        qw.select(JdbcConnInfo::getId, JdbcConnInfo::getConnName);
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
    public boolean save(JdbcConnInfo entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(entity.getCreateTime());
        return super.save(entity);
    }

    @Override
    public Connection getConnection(JdbcConnInfo connInfo) {
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

    @Override
    public List<String> getDbNames(JdbcConnInfo entity) {
        DbType dbType = DbType.getValue(entity.getDbType());
        String connectionUrl = null;
        if (dbType != null) {
            JDBCDriver jdbcDriver = JDBCDriver.valueOfDriverName(dbType.getDriverClassName());
            if (jdbcDriver == null) {
                return Collections.emptyList();
            }
            connectionUrl = jdbcDriver.getConnectionUrl(entity.getIp(), entity.getPort(), "", null);
        }

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
    public String testJdbcConnection(Long id) {
        if (this.getConnection(id) != null) {
            return "连接成功";
        }
        return "连接失败";
    }
}
