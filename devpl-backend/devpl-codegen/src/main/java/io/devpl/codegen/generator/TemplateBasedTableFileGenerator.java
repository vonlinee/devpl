package io.devpl.codegen.generator;

import io.devpl.codegen.template.TemplateEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class TemplateBasedTableFileGenerator extends AbstractTableFileGenerator {

    TemplateBasedTargetFile targetFile;
    TemplateEngine templateEngine;

    public TemplateBasedTableFileGenerator(TemplateBasedTargetFile targetFile) {
        super();
        this.targetFile = targetFile;
    }

    @Override
    public void initialize(GenerationTarget target) {
        if (target instanceof TableGeneration) {
            this.tableGeneration = (TableGeneration) target;
        }
        this.targetFile.initialize(target);
    }

    @Override
    public List<GeneratedFile> getGeneratedFiles() {
        TemplateGeneratedFile file = new TemplateGeneratedFile();
        file.setTargetFile(targetFile);
        file.setTemplate(targetFile.getTemplate());
        file.setTemplateEngine(this.templateEngine);
        return Collections.singletonList(file);
    }
}
