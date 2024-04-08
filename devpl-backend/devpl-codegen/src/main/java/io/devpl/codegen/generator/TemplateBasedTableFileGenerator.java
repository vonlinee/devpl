package io.devpl.codegen.generator;

import io.devpl.codegen.template.TemplateEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
class TemplateBasedTableFileGenerator extends AbstractTableFileGenerator {

    private List<TemplateBasedTargetFile> targetFiles;
    private TemplateEngine templateEngine;

    public TemplateBasedTableFileGenerator() {
        super();
        this.targetFiles = new ArrayList<>();
    }

    public <T extends TemplateBasedTargetFile> void addTargetFile(T targetFile) {
        this.targetFiles.add(targetFile);
    }

    public <T extends TemplateBasedTargetFile> void addTargetFiles(Collection<T> targetFiles) {
        this.targetFiles.addAll(targetFiles);
    }

    @Override
    public void initialize(GenerationTarget target) {
        if (target instanceof TableGeneration) {
            this.tableGeneration = (TableGeneration) target;
        }
        for (TemplateBasedTargetFile targetFile : this.targetFiles) {
            targetFile.initialize(target);
        }
    }

    @Override
    public List<GeneratedFile> getGeneratedFiles() {
        List<GeneratedFile> generatedFiles = new ArrayList<>();
        for (TemplateBasedTargetFile targetFile : targetFiles) {
            TemplateGeneratedFile file = new TemplateGeneratedFile();
            file.setTargetFile(targetFile);
            file.setTemplate(targetFile.getTemplate());
            file.setTemplateEngine(this.templateEngine);
            generatedFiles.add(file);
        }
        return generatedFiles;
    }
}
