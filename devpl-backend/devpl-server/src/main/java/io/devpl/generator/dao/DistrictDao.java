package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.District;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地区信息 Mapper
 */
@Mapper
public interface DistrictDao extends EntityMapper<District> {

}
