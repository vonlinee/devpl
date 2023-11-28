package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.Query;
import io.devpl.backend.common.mvc.BaseServiceImpl;
import io.devpl.backend.dao.BaseClassMapper;
import io.devpl.backend.entity.GenBaseClass;
import io.devpl.backend.service.BaseClassService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基类管理
 */
@Service
public class BaseClassServiceImpl extends BaseServiceImpl<BaseClassMapper, GenBaseClass> implements BaseClassService {

    @Override
    public ListResult<GenBaseClass> listPage(Query query) {
        IPage<GenBaseClass> page = baseMapper.selectPage(
            getPage(query), getWrapper(query)
        );
        return ListResult.ok(page);
    }

    @Override
    public List<GenBaseClass> listAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public boolean save(GenBaseClass entity) {
        entity.setCreateTime(LocalDateTime.now());
        return super.save(entity);
    }
}
