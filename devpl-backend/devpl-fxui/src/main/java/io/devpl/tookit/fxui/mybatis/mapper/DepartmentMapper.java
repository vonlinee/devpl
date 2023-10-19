package io.devpl.tookit.fxui.mybatis.mapper;

import io.devpl.tookit.fxui.mybatis.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentMapper {

    List<Department> selectList(@Param("param") Map<String, Object> paramMap);
}
