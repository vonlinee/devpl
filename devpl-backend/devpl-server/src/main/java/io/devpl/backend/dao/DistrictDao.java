package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.EntityMapper;
import io.devpl.backend.entity.District;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地区信息 Mapper
 */
@Mapper
public interface DistrictDao extends EntityMapper<District> {

}
