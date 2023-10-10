package io.devpl.generator.dao;

import io.devpl.generator.common.dao.BaseDao;
import io.devpl.generator.entity.TemplateParamEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模板变量 Dao
 *
 * @author vonline
 * @since 1.0.0 2023-07-29
 */
@Mapper
public interface TemplateParamDao extends BaseDao<TemplateParamEntity> {

}
