package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.TargetGenFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模板文件生成关联表
 * @author xxx xxx
 * @since 1.0.0 2023-07-14
 */
@Mapper
public interface TargetGenFileMapper extends EntityMapper<TargetGenFile> {

    /**
     * 查询可选择的生成文件
     * @return 生成文件列表
     */
    List<TargetGenFile> selectGeneratedFileTypes();
}
