package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.dao.BaseClassMapper;
import io.devpl.generator.entity.GenBaseClass;
import io.devpl.generator.service.BaseClassService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 基类管理
 */
@Service
public class BaseClassServiceImpl extends BaseServiceImpl<BaseClassMapper, GenBaseClass> implements BaseClassService {

    @Override
    public PageResult<GenBaseClass> page(Query query) {
        IPage<GenBaseClass> page = baseMapper.selectPage(
            getPage(query), getWrapper(query)
        );
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<GenBaseClass> getList() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean save(GenBaseClass entity) {
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
