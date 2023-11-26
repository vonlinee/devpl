package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.TargetGenerationFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模板文件生成关联表
 */
@Mapper
public interface TargetGenerationFileMapper extends EntityMapper<TargetGenerationFile> {

    /**
     * 查询可选择的生成文件
     *
     * @return 生成文件列表
     */
    List<TargetGenerationFile> selectGeneratedFileTypes();
}