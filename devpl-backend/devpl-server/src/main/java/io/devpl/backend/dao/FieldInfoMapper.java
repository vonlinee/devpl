package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.EntityMapper;
import io.devpl.backend.entity.FieldInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FieldInfoMapper extends EntityMapper<FieldInfo> {
}
