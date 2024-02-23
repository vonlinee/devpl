package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目名变更
 */
@Mapper
public interface ProjectInfoMapper extends MyBatisPlusMapper<ProjectInfo> {

    /**
     * 项目根路径
     *
     * @return 项目根路径列表
     */
    @Select(value = "select DISTINCT project_path from project_info")
    List<String> selectProjectRootPath();
}
