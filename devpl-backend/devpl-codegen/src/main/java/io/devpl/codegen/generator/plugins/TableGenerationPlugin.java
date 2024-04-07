package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.generator.GenerationTarget;
import io.devpl.codegen.generator.PluginAdapter;
import io.devpl.codegen.generator.TableGeneration;

public abstract class TableGenerationPlugin extends PluginAdapter {

    @Override
    public final void initialize(GenerationTarget target) {
        if (support(target)) {
            initialize((TableGeneration) target);
        }
    }

    @Override
    public final boolean support(GenerationTarget target) {
        return target instanceof TableGeneration;
    }

    abstract void initialize(TableGeneration tableGeneration);
}
