package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.entity.District;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地区信息 Mapper
 */
@Mapper
public interface DistrictDao extends BaseMapper<District> {

}
