package io.devpl.codegen.generator;

import io.devpl.codegen.util.DefaultContextObject;

import java.util.List;
import java.util.Objects;

/**
 * 代码生成上下文
 */
public abstract class Context extends DefaultContextObject {

    private String id;

    /**
     * 插件
     */
    private PluginManager plugins;

    public final void setId(String id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public final String getId() {
        return id;
    }

    @Override
    public void initialize() {
        this.plugins = new PluginManager();
    }

    /**
     * 收集所有生成的文件
     *
     * @param files 所有生成的文件
     */
    public abstract void generateFiles(List<GeneratedFile> files);

    public final void addPlugin(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin instance cannot be null");
        plugin.setContext(this);
        this.plugins.addPlugin(plugin);
    }

    public final Plugin getPlugins() {
        return plugins;
    }
}
