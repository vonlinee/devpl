package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.impl.BaseServiceImpl;
import io.devpl.generator.config.DataSourceInfo;
import io.devpl.generator.config.DbType;
import io.devpl.generator.dao.DataSourceDao;
import io.devpl.generator.entity.GenDataSource;
import io.devpl.generator.service.DataSourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 数据源管理
 */
@Service
@AllArgsConstructor
@Slf4j
public class DataSourceServiceImpl extends BaseServiceImpl<DataSourceDao, GenDataSource> implements DataSourceService {
    private final DataSource dataSource;

    @Override
    public PageResult<GenDataSource> page(Query query) {
        IPage<GenDataSource> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<GenDataSource> getList() {
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
    public DataSourceInfo get(Long datasourceId) {
        // 初始化配置信息
        DataSourceInfo info = null;
        if (datasourceId.intValue() == 0) {
            try {
                info = new DataSourceInfo(dataSource.getConnection());
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            info = new DataSourceInfo(this.getById(datasourceId));
        }

        return info;
    }

    @Override
    public boolean save(GenDataSource entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
