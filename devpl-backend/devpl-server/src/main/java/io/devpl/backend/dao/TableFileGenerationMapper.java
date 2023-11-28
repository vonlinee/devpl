package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.TableFileGeneration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表文件生成记录表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-11-24
 **/
@Mapper
public interface TableFileGenerationMapper extends BaseMapper<TableFileGeneration> {

    List<TableFileGeneration> selectListByTableId(@Param("tableId") Long tableId);
}
