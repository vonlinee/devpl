package io.devpl.codegen.config;

import io.devpl.codegen.template.model.ControllerTemplateArguments;
import io.devpl.codegen.template.model.EntityTemplateArguments;
import io.devpl.codegen.template.model.MapperTemplateArguments;
import io.devpl.codegen.template.model.ServiceTemplateArguments;

/**
 * 配置构建
 */
public class BaseBuilder {

    private final StrategyConfig strategyConfig;

    public BaseBuilder(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

    public EntityTemplateArguments.Builder entityBuilder() {
        return strategyConfig.entityBuilder();
    }

    public ControllerTemplateArguments.Builder controllerBuilder() {
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
