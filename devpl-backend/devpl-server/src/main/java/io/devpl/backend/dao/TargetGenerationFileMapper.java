package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.TargetGenerationFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模板文件生成关联表
 */
@Mapper
public interface TargetGenerationFileMapper extends MyBatisPlusMapper<TargetGenerationFile> {

    /**
     * 查询可选择的生成文件
     *
     * @return 生成文件列表
     * @see TargetGenerationFile#isDefaultTarget()
     */
    List<TargetGenerationFile> selectDefaultGeneratedFileTypes();

    /**
     * 查询可选择的生成文件
     *
     * @return 生成文件列表
     * @see TargetGenerationFile#isDefaultTarget()
     */
    List<TargetGenerationFile> selectGeneratedFileTypes();
}
