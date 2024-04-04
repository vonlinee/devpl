package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.*;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.sdk.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 生成文件
 */
public class AutoGenerator {

    private static final Logger log = LoggerFactory.getLogger(AutoGenerator.class);
    /**
     * 数据源配置
     */
    private final JdbcConfiguration dataSourceConfig;
    /**
     * 配置信息
     */
    protected Context context;
    /**
     * 模板引擎
     */
    protected TemplateEngine templateEngine;
    /**
     * 数据库表配置
     */
    private StrategyConfiguration strategyConfiguration;
    /**
     * 包 相关配置
     */
    private PackageConfiguration packageConfig;
    /**
     * 模板 相关配置
     */
    private TemplateConfiguration templateConfiguration;
    /**
     * 全局 相关配置
     */
    private GlobalConfiguration globalConfiguration;

    /**
     * 构造方法
     *
     * @param dataSourceConfig 数据库配置
     * @since 3.5.0
     */
    public AutoGenerator(JdbcConfiguration dataSourceConfig) {
        // 这个是必须参数,其他都是可选的,后续去除默认构造更改成final
        this.dataSourceConfig = dataSourceConfig;
    }

    /**
     * 生成策略
     *
     * @param strategyConfiguration 策略配置
     */
    public AutoGenerator strategy(StrategyConfiguration strategyConfiguration) {
        this.strategyConfiguration = strategyConfiguration;
        return this;
    }

    /**
     * 指定包配置信息
     *
     * @param packageConfig 包配置
     * @return this
     * @since 3.5.0
     */
    public AutoGenerator packageInfo(PackageConfiguration packageConfig) {
        this.packageConfig = packageConfig;
        return this;
    }

    /**
     * 指定模板配置
     *
     * @param templateConfiguration 模板配置
     * @return this
     * @since 3.5.0
     */
    public AutoGenerator template(TemplateConfiguration templateConfiguration) {
        this.templateConfiguration = templateConfiguration;
        return this;
    }

    /**
     * 指定全局配置
     *
     * @param globalConfiguration 全局配置
     * @return this
     * @see 3.5.0
     */
    public AutoGenerator global(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
        return this;
    }

    /**
     * 生成代码
     */
    public void execute() {
        this.execute(null);
    }

    /**
     * 生成代码
     *
     * @param templateEngine 模板引擎
     */
    public AutoGenerator execute(TemplateEngine templateEngine) {
        context = initContext();
        List<GeneratedFile> files = new ArrayList<>();
        context.generateFiles(files);
        writeFiles(files);
        return this;
    }

    // 写入文件
    public void writeFiles(List<GeneratedFile> files) {
        for (GeneratedFile file : files) {
            try {
                file.write(null, StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("error when write file {}", file.getFilename());
            }
        }
    }

    public Context initContext() {
        if (context == null) {
            if (this.strategyConfiguration == null) {
                this.strategyConfiguration = StrategyConfiguration.builder().build();
            }
            if (this.globalConfiguration == null) {
                this.globalConfiguration = GlobalConfiguration.builder().build();
            }
            if (this.templateConfiguration == null) {
                this.templateConfiguration = TemplateConfiguration.builder().build();
            }
            if (this.packageConfig == null) {
                this.packageConfig = PackageConfiguration.builder().build();
            }
            context = new RdbmsTableGenerationContext(packageConfig, dataSourceConfig, strategyConfiguration, templateConfiguration, globalConfiguration);
            context.initialize();
        }
        return context;
    }

    /**
     * 打开输出目录
     */
    public void open() {
        show(dir -> {
            try {
                openDir(dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void show(Consumer<String> consumer) {
        GlobalConfiguration globalConfiguration = context.getObject(GlobalConfiguration.class);
        String outDir = globalConfiguration.getOutputDir();
        if (!StringUtils.hasText(outDir) || !new File(outDir).exists()) {
            System.err.println("未找到输出目录：" + outDir);
        } else if (globalConfiguration.isOpen()) {
            try {
                consumer.accept(outDir);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 打开指定输出文件目录
     *
     * @param outDir 输出文件目录
     * @throws IOException 执行命令出错
     */
    public void openDir(String outDir) throws IOException {
        String osName = System.getProperty("os.name");
        if (osName != null) {
            if (osName.contains("Mac")) {
                Runtime.getRuntime().exec("open " + outDir);
            } else if (osName.contains("Windows")) {
                Runtime.getRuntime().exec(MessageFormat.format("cmd /c start \"\" \"{0}\"", outDir));
            }
        }
    }
}
