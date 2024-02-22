package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.backend.common.mvc.MyBatisPlusServiceImpl;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.dao.GenFieldTypeMapper;
import io.devpl.backend.domain.param.Query;
import io.devpl.backend.entity.GenFieldType;
import io.devpl.backend.service.GenFieldTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字段类型管理
 */
@Service
public class GenFieldTypeServiceImpl extends MyBatisPlusServiceImpl<GenFieldTypeMapper, GenFieldType> implements GenFieldTypeService {

    @Override
    public ListResult<GenFieldType> page(Query query) {
        Page<GenFieldType> page1 = new Page<>(query.getPage(), query.getLimit());
        page1.addOrder(OrderItem.desc("id"));

        QueryWrapper<GenFieldType> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getCode()), "code", query.getCode());
        wrapper.like(StringUtils.hasText(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StringUtils.hasText(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(StringUtils.hasText(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StringUtils.hasText(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.hasText(query.getDbType()), "db_type", query.getDbType());
        wrapper.like(StringUtils.hasText(query.getProjectName()), "project_name", query.getProjectName());
        return ListResult.ok(baseMapper.selectPage(page1, wrapper));
    }

    @Override
    public Map<String, GenFieldType> getMap() {
        List<GenFieldType> list = baseMapper.selectList(null);
        Map<String, GenFieldType> map = new LinkedHashMap<>(list.size());
        for (GenFieldType entity : list) {
            map.put(entity.getColumnType().toLowerCase(), entity);
        }
        return map;
    }

    @Override
    public Set<String> getPackageByTableId(Long tableId) {
        Set<String> importList = baseMapper.getPackageByTableId(tableId);
        return importList.stream().filter(StringUtils::hasText).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getList() {
        return baseMapper.list();
    }

    @Override
    public boolean save(GenFieldType entity) {
        entity.setCreateTime(LocalDateTime.now());
        return super.save(entity);
    }
}
