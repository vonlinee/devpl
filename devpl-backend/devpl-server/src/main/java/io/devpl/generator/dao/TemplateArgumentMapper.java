package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.entity.TemplateArgument;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模板参数表，模板实际的参数值
 **/
@Mapper
public interface TemplateArgumentMapper extends BaseMapper<TemplateArgument> {

}
