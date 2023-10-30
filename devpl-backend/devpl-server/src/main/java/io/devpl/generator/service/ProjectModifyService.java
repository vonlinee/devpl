package io.devpl.generator.service;

import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.domain.param.Query;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.entity.ProjectModify;

import java.io.IOException;

/**
 * 项目名变更
 */
public interface ProjectModifyService extends BaseService<ProjectModify> {

    ListResult<ProjectModify> page(Query query);

    byte[] download(ProjectModify project) throws IOException;
}
