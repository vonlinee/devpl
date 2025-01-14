package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.TemplateParam;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模板变量 Dao
 *
 * @author vonline
 * @since 1.0.0 2023-07-29
 */
@Mapper
public interface TemplateParamMapper extends MyBatisPlusMapper<TemplateParam> {

}
