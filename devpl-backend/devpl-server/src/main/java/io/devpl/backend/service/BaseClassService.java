package io.devpl.backend.service;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.Query;
import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.entity.GenBaseClass;

import java.util.List;

/**
 * 基类管理
 */
public interface BaseClassService extends BaseService<GenBaseClass> {

    ListResult<GenBaseClass> listPage(Query query);

    List<GenBaseClass> listAll();
}
