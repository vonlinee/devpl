package io.devpl.generator.service;

import io.devpl.generator.common.service.BaseService;
import io.devpl.generator.entity.TargetGenFile;

import java.util.List;

/**
 * 模板文件生成关联表
 * @author xxx xxx
 * @since 1.0.0 2023-07-14
 */
public interface TemplateFileGenerationService extends BaseService<TargetGenFile> {

    List<TargetGenFile> listGeneratedFileTypes();
}
