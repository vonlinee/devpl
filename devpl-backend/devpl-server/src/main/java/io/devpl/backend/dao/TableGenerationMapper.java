package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.domain.bo.TableImportInfo;
import io.devpl.backend.domain.param.GenTableListParam;
import io.devpl.backend.entity.TableGeneration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据表
 */
@Mapper
public interface TableGenerationMapper extends MyBatisPlusMapper<TableGeneration> {

    /**
     * 查询单条数据
     *
     * @param dataSourceId 数据源 ID
     * @param databaseName 数据库名称
     * @param tableName    表名
     * @return {@link TableGeneration}
     */
    TableGeneration selectOne(Long dataSourceId, String databaseName, String tableName);

    /**
     * 查询表名列表
     *
     * @param dataSourceId 数据源 ID
     * @return {@link List}<{@link String}>
     */
    @Select(value = "SELECT table_name FROM table_generation WHERE datasource_id = #{dataSourceId}")
    List<String> selectTableNames(@Param("dataSourceId") Long dataSourceId);

    /**
     * 按条件选择列表
     *
     * @param param 参数
     * @return {@link List}<{@link TableGeneration}>
     */
    List<TableGeneration> selectListByCondition(@Param("param") GenTableListParam param);

    /**
     * 查询已经导入的表列表
     *
     * @param dataSourceId 数据源 ID
     * @param databaseName 数据库名称
     * @param tableName    表名
     * @return {@link List}<{@link TableImportInfo}>
     */
    List<TableImportInfo> selectImportedTableList(@Param("dataSourceId") Long dataSourceId, @Param("databaseName") String databaseName, @Param("tableName") String tableName);
}
