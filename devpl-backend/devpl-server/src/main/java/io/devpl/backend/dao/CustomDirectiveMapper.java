package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.CustomDirective;
import org.apache.ibatis.annotations.Mapper;

/**
 * 自定义指令Mapper
 */
@Mapper
public interface CustomDirectiveMapper extends BaseMapper<CustomDirective> {

}
