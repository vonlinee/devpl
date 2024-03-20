package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.TableFileGeneration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表文件生成记录表
 **/
@Mapper
public interface TableFileGenerationMapper extends BaseMapper<TableFileGeneration> {

    /**
     * 按表 ID 查询列表
     *
     * @param tableId 表 ID
     * @return {@link List}<{@link TableFileGeneration}>
     */
    List<TableFileGeneration> selectListByTableId(@Param("tableId") Long tableId);
}
