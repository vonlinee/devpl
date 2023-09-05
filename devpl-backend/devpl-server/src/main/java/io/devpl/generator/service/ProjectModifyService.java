package io.devpl.generator.service;

import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.BaseService;
import io.devpl.generator.entity.ProjectModify;

import java.io.IOException;

/**
 * 项目名变更
 */
public interface ProjectModifyService extends BaseService<ProjectModify> {

    PageResult<ProjectModify> page(Query query);

    byte[] download(ProjectModify project) throws IOException;
}
