package io.devpl.backend.service;

import io.devpl.backend.entity.TargetGenerationFile;

import java.util.List;

/**
 * 模板文件生成关联表
 *
 * @author xxx xxx
 * @since 1.0.0 2023-07-14
 */
public interface TargetGenerationFileService {

    boolean saveOrUpdate(TargetGenerationFile file);

    List<TargetGenerationFile> listDefaultGeneratedFileTypes();

    List<TargetGenerationFile> listGeneratedFileTypes();

    boolean removeBatchByIds(List<Integer> ids);

    boolean saveOrUpdateBatch(List<TargetGenerationFile> files);
}
