package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.impl.BaseServiceImpl;
import io.devpl.generator.dao.FieldTypeDao;
import io.devpl.generator.entity.FieldTypeEntity;
import io.devpl.generator.service.FieldTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字段类型管理
 */
@Service
public class FieldTypeServiceImpl extends BaseServiceImpl<FieldTypeDao, FieldTypeEntity> implements FieldTypeService {

    @Override
    public PageResult<FieldTypeEntity> page(Query query) {
        IPage<FieldTypeEntity> page = baseMapper.selectPage(
            getPage(query),
            getWrapper(query)
        );
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public Map<String, FieldTypeEntity> getMap() {
        List<FieldTypeEntity> list = baseMapper.selectList(null);
        Map<String, FieldTypeEntity> map = new LinkedHashMap<>(list.size());
        for (FieldTypeEntity entity : list) {
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
    public boolean save(FieldTypeEntity entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
