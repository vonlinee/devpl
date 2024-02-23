package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.CustomDirective;
import org.apache.ibatis.annotations.Mapper;

/**
 * 自定义指令Mapper
 */
@Mapper
public interface CustomDirectiveMapper extends MyBatisPlusMapper<CustomDirective> {

}
