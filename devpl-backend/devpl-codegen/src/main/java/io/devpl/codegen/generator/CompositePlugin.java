package io.devpl.codegen.generator;

import io.devpl.codegen.generator.file.GeneratedFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * This class implements a composite plugin. It contains a list of plugins for the
 * current context and is used to aggregate plugins together. This class
 * implements the rule that if any plugin returns "false" from a method, then no
 * subsequent plugin is called.
 */
public abstract class CompositePlugin implements Plugin {

    /**
     * 插件列表
     */
    private final List<Plugin> plugins = new ArrayList<>();

    protected CompositePlugin() {
        super();
    }

    @Override
    public void setContext(Context context) {
        for (Plugin plugin : plugins) {
            plugin.setContext(context);
        }
    }

    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    public void sort() {
        this.plugins.sort(Comparator.comparing(Plugin::getPriority));
    }

    @Override
    public void initialize(GenerationTarget target) {
        for (Plugin plugin : plugins) {
            plugin.initialize(target);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        for (Plugin plugin : plugins) {
            plugin.setProperties(properties);
        }
    }

    @Override
    public void generateFiles(GenerationTarget unit, List<GeneratedFile> generatedFiles) {
        for (Plugin plugin : plugins) {
            List<GeneratedFile> currentFiles = plugin.generateFiles(unit);
            if (currentFiles != generatedFiles && currentFiles != null && !currentFiles.isEmpty()) {
                generatedFiles.addAll(currentFiles);
            }
        }
    }

    @Override
    public boolean shouldGenerate(GenerationTarget unit) {
        int count = 0;
        for (Plugin plugin : plugins) {
            if (!plugin.shouldGenerate(unit)) {
                count++;
            }
        }
        return count != plugins.size();
    }
}
