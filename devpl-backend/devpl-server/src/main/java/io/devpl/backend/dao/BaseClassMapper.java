package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.EntityMapper;
import io.devpl.backend.entity.GenBaseClass;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 */
@Mapper
public interface BaseClassMapper extends EntityMapper<GenBaseClass> {

}
