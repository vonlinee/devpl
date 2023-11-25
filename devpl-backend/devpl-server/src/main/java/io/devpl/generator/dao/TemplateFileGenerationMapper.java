package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.entity.TemplateFileGeneration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TemplateFileGenerationMapper extends BaseMapper<TemplateFileGeneration> {

    int deleteByUnitId(@Param("unitId") Long unitId);
}