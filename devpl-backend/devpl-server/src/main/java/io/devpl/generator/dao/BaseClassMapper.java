package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.GenBaseClass;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 */
@Mapper
public interface BaseClassMapper extends EntityMapper<GenBaseClass> {

}
