package io.devpl.generator.dao;

import io.devpl.generator.common.dao.BaseDao;
import io.devpl.generator.entity.TemplateFileGeneration;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模板文件生成关联表
 * @author xxx xxx
 * @since 1.0.0 2023-07-14
 */
@Mapper
public interface TemplateFileGenerationMapper extends BaseDao<TemplateFileGeneration> {

}
