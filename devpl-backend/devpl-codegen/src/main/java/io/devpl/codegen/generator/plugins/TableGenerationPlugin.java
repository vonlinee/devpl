package io.devpl.codegen.generator.plugins;

import io.devpl.codegen.generator.GenerationUnit;
import io.devpl.codegen.generator.PluginAdapter;
import io.devpl.codegen.generator.TableGeneration;

public abstract class TableGenerationPlugin extends PluginAdapter {

    @Override
    public final void initialize(GenerationUnit unit) {
        if (unit instanceof TableGeneration) {
            initialize((TableGeneration) unit);
        }
    }

    abstract void initialize(TableGeneration tableGeneration);
}
