package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;
import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.impl.BaseServiceImpl;
import io.devpl.generator.config.ConnectionInfo;
import io.devpl.generator.config.DbType;
import io.devpl.generator.dao.DataSourceDao;
import io.devpl.generator.entity.DataSourceInfo;
import io.devpl.generator.service.DataSourceService;
import io.devpl.generator.utils.DbUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据源管理
 */
@Service
@AllArgsConstructor
@Slf4j
public class DataSourceServiceImpl extends BaseServiceImpl<DataSourceDao, DataSourceInfo> implements DataSourceService {

    /**
     * 程序内部的数据源
     */
    private final DataSource dataSource;

    @Override
    public PageResult<DataSourceInfo> page(Query query) {
        IPage<DataSourceInfo> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<DataSourceInfo> getList() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
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
    public ConnectionInfo findById(Long datasourceId) {
        // 初始化配置信息
        ConnectionInfo info = null;
        if (datasourceId.intValue() == 0) {
            // 本系统连接的数据源
            try {
                info = new ConnectionInfo(dataSource.getConnection());
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            info = new ConnectionInfo(this.getById(datasourceId));
        }
        return info;
    }

    @Override
    public boolean save(DataSourceInfo entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

    @Override
    @Nullable
    public Connection getConnection(Long dataSourceId) {
        try {
            if (dataSourceId.intValue() == 0) {
                // 本系统连接的数据源
                return dataSource.getConnection();
            } else {
                ConnectionInfo dataSourceInfo = new ConnectionInfo(this.getById(dataSourceId));
                return DbUtils.getConnection(dataSourceInfo);
            }
        } catch (SQLException exception) {
            return null;
        }
    }

    @Override
    public List<String> getDbNames(DataSourceInfo entity) {
        ConnectionInfo dataSourceInfo = new ConnectionInfo(entity);

        DbType dbType = DbType.getValue(entity.getDbType());
        if (dbType != null) {
            JDBCDriver jdbcDriver = JDBCDriver.valueOfDriverName(dbType.getDriverClass());
            if (jdbcDriver == null) {
                return Collections.emptyList();
            }
            String connectionUrl = jdbcDriver.getConnectionUrl(entity.getIp(), entity.getPort(), "", null);
            dataSourceInfo.setConnUrl(connectionUrl);
        }

        List<String> list = new ArrayList<>();
        try (Connection connection = DbUtils.getConnection(dataSourceInfo)) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            SingleColumnRowMapper<String> rowMapper = new SingleColumnRowMapper<>();
            int rowNum = 0;
            while (catalogs.next()) {
                list.add(rowMapper.mapRow(catalogs, rowNum++));
            }
        } catch (Exception exception) {
            log.error("", exception);
        }
        return list;
    }
}
