package io.devpl.generator.domain.param;

import io.devpl.generator.entity.FileGenerationUnit;
import io.devpl.generator.entity.TemplateFileGeneration;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FileGenUnitParam {

    /**
     * 自定义单元
     */
    private FileGenerationUnit customUnit;

    /**
     * 文件生成单元
     */
    private List<FileGenerationUnit> units;

    /**
     * 模板文件生成参数
     */
    private List<TemplateFileGeneration> tempFileGenList;

    /**
     * 删除
     */
    private Long id;

    private Integer type;

    /**
     * 填充策略
     *
     * @see io.devpl.generator.domain.TemplateFillStrategy
     */
    private String genStrategy;
}
