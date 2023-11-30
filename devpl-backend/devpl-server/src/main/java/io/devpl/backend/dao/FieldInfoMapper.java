package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.EntityMapper;
import io.devpl.backend.entity.FieldInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FieldInfoMapper extends EntityMapper<FieldInfo> {

    /**
     * 查询所有字段Key
     *
     * @return 所有字段Key
     */
    List<String> selectFieldKeys();
}
