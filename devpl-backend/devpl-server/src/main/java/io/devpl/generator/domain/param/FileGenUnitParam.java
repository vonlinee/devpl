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
     * 模板文件生成参数
     */
    private List<TemplateFileGeneration> tfgs;

    /**
     * 删除
     */
    private Long id;

    private Integer type;
}
