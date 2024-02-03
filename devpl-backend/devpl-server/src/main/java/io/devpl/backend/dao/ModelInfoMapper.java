package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.ModelField;
import io.devpl.backend.entity.ModelInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 基类管理
 */
@Mapper
public interface ModelInfoMapper extends BaseMapper<ModelInfo> {

    List<ModelField> selectModelFields(@Param("modelId") Long modelId);

    List<String> selectModelFieldKeys(@Param("modelId") Long modelId);

    /**
     * 查询模型关联的字段ID列表
     *
     * @param modelId 模型ID
     * @return 字段ID列表
     */
    List<Long> selectModelFieldIds(@Param("modelId") Long modelId);

    /**
     * 新增模型和字段关联
     *
     * @param modelId  模型ID
     * @param fieldIds 字段ID列表
     * @return 是否成功
     */
    int insertModeFieldRelation(@Param("modelId") Long modelId, @Param("fieldIds") Collection<Long> fieldIds);

    /**
     * 移除模型的字段
     *
     * @param modelId  模型ID
     * @param fieldIds 字段ID列表
     * @return 是否成功
     */
    int removeFields(@Param("modelId") Long modelId, @Param("fieldIds") Collection<Long> fieldIds);
}
