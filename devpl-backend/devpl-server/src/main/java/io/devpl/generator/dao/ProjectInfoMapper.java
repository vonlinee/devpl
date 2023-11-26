package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名变更
 */
@Mapper
public interface ProjectInfoMapper extends EntityMapper<ProjectInfo> {

}
