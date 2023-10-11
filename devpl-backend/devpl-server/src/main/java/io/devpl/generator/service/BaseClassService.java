package io.devpl.generator.service;

import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.entity.GenBaseClass;

import java.util.List;

/**
 * 基类管理
 */
public interface BaseClassService extends BaseService<GenBaseClass> {

    PageResult<GenBaseClass> page(Query query);

    List<GenBaseClass> getList();
}
