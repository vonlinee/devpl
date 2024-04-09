package io.devpl.codegen.generator.file;

import io.devpl.codegen.generator.GenerationTarget;
import io.devpl.codegen.generator.file.GeneratedFile;
import io.devpl.codegen.generator.file.TargetFile;

import java.util.List;

/**
 * @see TargetFile
 */
public interface FileGenerator {

    void initialize(GenerationTarget target);

    List<GeneratedFile> getGeneratedFiles();
}
