package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.entity.BaseClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 */
@Mapper
public interface BaseClassDao extends BaseMapper<BaseClassEntity> {

}
