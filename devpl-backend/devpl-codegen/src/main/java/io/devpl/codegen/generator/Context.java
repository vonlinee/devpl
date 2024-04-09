package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.PluginConfiguration;
import io.devpl.codegen.generator.file.GeneratedFile;
import io.devpl.codegen.generator.file.TargetFile;
import io.devpl.codegen.util.DefaultContextObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 代码生成上下文
 */
public abstract class Context extends DefaultContextObject {

    /**
     * 唯一ID
     */
    private String id;

    /**
     * 插件
     */
    private PluginManager plugins;

    /**
     * 插件配置列表
     */
    private List<PluginConfiguration> pluginConfigurations = new ArrayList<>();

    public final void setId(String id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public final String getId() {
        return id;
    }

    @Override
    public void initialize() {
        this.plugins = new PluginManager();
        this.pluginConfigurations = new ArrayList<>();
        this.setId(this.toString());
    }

    /**
     * 收集所有生成的文件
     *
     * @param files 所有生成的文件
     */
    public abstract void generateFiles(ProgressCallback callback, List<GeneratedFile> files, List<String> errors) throws InterruptedException;

    /**
     * 注册生成的目标文件
     *
     * @param targetFile 目标文件
     */
    public abstract void registerTargetFile(TargetFile targetFile);

    /**
     * 校验配置项
     *
     * @param errors 存放错误信息
     */
    public abstract void validate(List<String> errors);

    public final void addPlugin(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin instance cannot be null");
        plugin.setContext(this);
        this.plugins.addPlugin(plugin);
    }

    public final Plugin getPlugins() {
        return plugins;
    }

    public final void addPluginConfiguration(PluginConfiguration pluginConfiguration) {
        Objects.requireNonNull(pluginConfiguration, "plugin configuration cannot be null");
        pluginConfigurations.add(pluginConfiguration);
    }

    public List<PluginConfiguration> getPluginConfigurations() {
        return pluginConfigurations == null ? Collections.emptyList() : pluginConfigurations;
    }
}
