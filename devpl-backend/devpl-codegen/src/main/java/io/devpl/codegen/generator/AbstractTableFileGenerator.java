package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.TableConfiguration;
import org.mybatis.generator.api.ProgressCallback;

import java.util.Collections;
import java.util.List;

/**
 * @see org.mybatis.generator.config.Context#generateFiles(ProgressCallback, List, List, List, List, List)
 */
public class AbstractTableFileGenerator implements FileGenerator, ContextAware {

    TargetFile file;
    Context context;
    TableGeneration tableGeneration;
    TableConfiguration tableConfiguration;

    @Override
    public void initialize(GenerationTarget target) {
        if (!(target instanceof TableGeneration)) {
            return;
        }
    }

    @Override
    public List<GeneratedFile> getGeneratedFiles() {
        return Collections.emptyList();
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
