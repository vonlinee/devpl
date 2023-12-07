package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.common.mvc.BaseServiceImpl;
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
public class GenFieldTypeServiceImpl extends BaseServiceImpl<GenFieldTypeMapper, GenFieldType> implements GenFieldTypeService {

    @Override
    public ListResult<GenFieldType> page(Query query) {
        IPage<GenFieldType> page = baseMapper.selectPage(
            getPage(query),
            getWrapper(query)
        );
        return ListResult.ok(page);
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
