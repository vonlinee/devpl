package io.devpl.codegen.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class implements a composite plugin. It contains a list of plugins for the
 * current context and is used to aggregate plugins together. This class
 * implements the rule that if any plugin returns "false" from a method, then no
 * subsequent plugin is called.
 */
abstract class CompositePlugin implements Plugin {

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

    @Override
    public void initialize(GenerationUnit unit) {
        for (Plugin plugin : plugins) {
            plugin.initialize(unit);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        for (Plugin plugin : plugins) {
            plugin.setProperties(properties);
        }
    }

    @Override
    public List<GeneratedFile> generateFiles(GenerationUnit unit, List<GeneratedFile> generatedFiles) {
        for (Plugin plugin : plugins) {
            List<GeneratedFile> currentFiles = plugin.generateFiles(unit, generatedFiles);
            if (currentFiles != generatedFiles && currentFiles != null && !currentFiles.isEmpty()) {
                generatedFiles.addAll(currentFiles);
            }
        }
        return generatedFiles;
    }
}
