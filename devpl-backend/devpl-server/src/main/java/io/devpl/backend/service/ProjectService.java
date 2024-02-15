package io.devpl.backend.service;

import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.ProjectListParam;
import io.devpl.backend.domain.vo.ProjectSelectVO;
import io.devpl.backend.entity.ProjectInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 项目名变更
 */
public interface ProjectService extends BaseService<ProjectInfo> {

    List<ProjectSelectVO> listSelectableProject();

    ListResult<ProjectInfo> listProjectInfos(ProjectListParam param);

    byte[] download(ProjectInfo project) throws IOException;

    boolean isProjectRootDirectory(File file);

    /**
     * 分析项目结构
     *
     * @param projectRootDir 项目工具
     */
    void analyse(File projectRootDir);
}
