package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.TableConfiguration;

public abstract class AbstractTableFileGenerator implements FileGenerator, ContextAware {

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
    public void setContext(Context context) {
        this.context = context;
    }
}
