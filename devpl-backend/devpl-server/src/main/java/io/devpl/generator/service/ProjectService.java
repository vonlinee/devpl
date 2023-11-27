package io.devpl.generator.service;

import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.domain.param.ProjectListParam;
import io.devpl.generator.domain.vo.ProjectSelectVO;
import io.devpl.generator.entity.ProjectInfo;

import java.io.IOException;
import java.util.List;

/**
 * 项目名变更
 */
public interface ProjectService extends BaseService<ProjectInfo> {

    List<ProjectSelectVO> listSelectableProject();

    ListResult<ProjectInfo> listProjectInfos(ProjectListParam param);

    byte[] download(ProjectInfo project) throws IOException;
}
