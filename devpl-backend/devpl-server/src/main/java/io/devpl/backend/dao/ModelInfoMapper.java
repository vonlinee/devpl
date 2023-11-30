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

    List<String> selectModelFieldKeys(@Param("modelId") Long modelId);

    List<Long> selectModelFieldIds(@Param("modelId") Long modelId);

    /**
     * 新增模型和字段关联
     *
     * @param modelId  模型ID
     * @param fieldIds 字段ID列表
     * @return 是否成功
     */
    int insertModeFieldRelation(@Param("modelId") Long modelId, @Param("fieldIds") List<Long> fieldIds);
}
