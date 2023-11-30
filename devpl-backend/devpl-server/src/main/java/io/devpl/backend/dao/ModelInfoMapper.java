package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.ModelField;
import io.devpl.backend.entity.ModelInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基类管理
 */
@Mapper
public interface ModelInfoMapper extends BaseMapper<ModelInfo> {

    List<ModelField> selectModelFields(@Param("modelId") Long modelId);
}
