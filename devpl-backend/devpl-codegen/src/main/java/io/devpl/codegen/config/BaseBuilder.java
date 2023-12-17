package io.devpl.codegen.config;

import io.devpl.codegen.config.args.ControllerTempateArguments;
import io.devpl.codegen.config.args.EntityTemplateArugments;
import io.devpl.codegen.config.args.MapperTemplateArguments;
import io.devpl.codegen.config.args.ServiceTemplateArguments;

/**
 * 配置构建
 */
public class BaseBuilder {

    private final StrategyConfig strategyConfig;

    public BaseBuilder(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

    public EntityTemplateArugments.Builder entityBuilder() {
        return strategyConfig.entityBuilder();
    }

    public ControllerTempateArguments.Builder controllerBuilder() {
        return strategyConfig.controllerBuilder();
    }

    public MapperTemplateArguments.Builder mapperBuilder() {
        return strategyConfig.mapperBuilder();
    }

    public ServiceTemplateArguments.Builder serviceBuilder() {
        return strategyConfig.serviceBuilder();
    }

    public StrategyConfig build() {
        this.strategyConfig.validate();
        return this.strategyConfig;
    }
}
