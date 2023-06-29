package io.devpl.generator.service;

import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.BaseService;
import io.devpl.generator.entity.BaseClassEntity;

import java.util.List;

/**
 * 基类管理
 */
public interface BaseClassService extends BaseService<BaseClassEntity> {

    PageResult<BaseClassEntity> page(Query query);

    List<BaseClassEntity> getList();
}
