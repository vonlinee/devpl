package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.*;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.util.InternalUtils;
import io.devpl.sdk.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FastAutoGenerator {

    /**
     * 数据源配置 Builder
     */
    private final JdbcConfiguration.Builder dataSourceConfigBuilder;

    /**
     * 全局配置 Builder
     */
    private final GlobalConfiguration.Builder globalConfigBuilder;

    /**
     * 包配置 Builder
     */
    private final PackageConfiguration.Builder packageConfigBuilder;

    /**
     * 策略配置 Builder
     */
    private final StrategyConfiguration.Builder strategyConfigBuilder;

    /**
     * 模板配置 Builder
     */
    private final TemplateConfiguration.Builder templateConfigBuilder;
    /**
     * 读取控制台输入内容
     */
    private final Scanner scanner = new Scanner(System.in);
    /**
     * 模板引擎
     */
    private TemplateEngine templateEngine;

    private FastAutoGenerator(JdbcConfiguration.Builder dataSourceConfigBuilder) {
        this.dataSourceConfigBuilder = dataSourceConfigBuilder;
        this.globalConfigBuilder = GlobalConfiguration.builder();
        this.packageConfigBuilder = PackageConfiguration.builder();
        this.strategyConfigBuilder = StrategyConfiguration.builder();
        this.templateConfigBuilder = new TemplateConfiguration.Builder();
    }

    /**
     * 从本地.properties文件进行加载
     *
     * @param propsFile .properties文件
     * @return FastAutoGenerator
     */
    public static FastAutoGenerator create(File propsFile) {
        Properties properties = InternalUtils.loadProperties(propsFile);
        String url = (String) properties.get("url");
        String username = (String) properties.get("username");
        String password = (String) properties.get("password");
        return new FastAutoGenerator(new JdbcConfiguration.Builder(url, username, password));
    }

    public static FastAutoGenerator create(@NotNull String url, String username, String password) {
        return new FastAutoGenerator(new JdbcConfiguration.Builder(url, username, password));
    }

    public static FastAutoGenerator create(@NotNull JdbcConfiguration.Builder dataSourceConfigBuilder) {
        return new FastAutoGenerator(dataSourceConfigBuilder);
    }

    /**
     * 控制台输入内容读取并打印提示信息
     *
     * @param message 提示信息
     * @return String
     */
    public String scannerNext(String message) {
        System.out.println(message);
        String nextLine = scanner.nextLine();
        if (StringUtils.isBlank(nextLine)) {
            // 如果输入空行继续等待
            return scanner.next();
        }
        return nextLine;
    }

    /**
     * 全局配置
     *
     * @param consumer 自定义全局配置
     * @return FastAutoGenerator
     */
    public FastAutoGenerator dataSourceConfig(Consumer<JdbcConfiguration.Builder> consumer) {
        consumer.accept(this.dataSourceConfigBuilder);
        return this;
    }

    public FastAutoGenerator dataSourceConfig(BiConsumer<Function<String, String>, JdbcConfiguration.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.dataSourceConfigBuilder);
        return this;
    }

    /**
     * 全局配置
     *
     * @param consumer 自定义全局配置
     * @return FastAutoGenerator
     */
    public FastAutoGenerator globalConfig(Consumer<GlobalConfiguration.Builder> consumer) {
        consumer.accept(this.globalConfigBuilder);
        return this;
    }

    public FastAutoGenerator globalConfig(BiConsumer<Function<String, String>, GlobalConfiguration.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.globalConfigBuilder);
        return this;
    }

    /**
     * 包配置
     *
     * @param consumer 自定义包配置
     * @return FastAutoGenerator
     */
    public FastAutoGenerator packageConfig(Consumer<PackageConfiguration.Builder> consumer) {
        consumer.accept(this.packageConfigBuilder);
        return this;
    }

    public FastAutoGenerator packageConfig(BiConsumer<Function<String, String>, PackageConfiguration.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.packageConfigBuilder);
        return this;
    }

    /**
     * 策略配置
     *
     * @param consumer 自定义策略配置
     * @return FastAutoGenerator
     */
    public FastAutoGenerator strategyConfig(Consumer<StrategyConfiguration.Builder> consumer) {
        consumer.accept(this.strategyConfigBuilder);
        return this;
    }

    public FastAutoGenerator strategyConfig(BiConsumer<Function<String, String>, StrategyConfiguration.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.strategyConfigBuilder);
        return this;
    }

    /**
     * 模板配置
     *
     * @param consumer 自定义模板配置
     * @return FastAutoGenerator
     */
    public FastAutoGenerator templateConfig(Consumer<TemplateConfiguration.Builder> consumer) {
        consumer.accept(this.templateConfigBuilder);
        return this;
    }

    public FastAutoGenerator templateConfig(BiConsumer<Function<String, String>, TemplateConfiguration.Builder> biConsumer) {
        biConsumer.accept(this::scannerNext, this.templateConfigBuilder);
        return this;
    }

    /**
     * 模板引擎配置
     *
     * @param templateEngine 模板引擎
     * @return FastAutoGenerator
     */
    public FastAutoGenerator templateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        return this;
    }

    public AutoGenerator execute() {
        return new AutoGenerator(this.dataSourceConfigBuilder.build())
            // 全局配置
            .global(this.globalConfigBuilder.build())
            // 包配置
            .packageInfo(this.packageConfigBuilder.build())
            // 策略配置
            .strategy(this.strategyConfigBuilder.build())
            // 模板配置
            .template(this.templateConfigBuilder.build())
            // 执行
            .execute(this.templateEngine);
    }
}
