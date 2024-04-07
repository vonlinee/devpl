package io.devpl.codegen.generator;

import java.util.List;

public interface FileGenerator {

    void initialize(GenerationTarget target);

    List<GeneratedFile> getGeneratedFiles();
}
