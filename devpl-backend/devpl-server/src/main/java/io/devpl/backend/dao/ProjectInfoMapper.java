package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名变更
 */
@Mapper
public interface ProjectInfoMapper extends MyBatisPlusMapper<ProjectInfo> {

}
