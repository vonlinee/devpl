package io.devpl.generator.service;

import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.domain.param.Query;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.entity.GenBaseClass;

import java.util.List;

/**
 * 基类管理
 */
public interface BaseClassService extends BaseService<GenBaseClass> {

    ListResult<GenBaseClass> listPage(Query query);

    List<GenBaseClass> listAll();
}
