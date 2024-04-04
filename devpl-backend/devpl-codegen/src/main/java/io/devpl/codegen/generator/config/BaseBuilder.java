package io.devpl.codegen.generator.config;

import io.devpl.codegen.template.model.ControllerTemplateArguments;
import io.devpl.codegen.template.model.EntityTemplateArguments;
import io.devpl.codegen.template.model.MapperTemplateArguments;
import io.devpl.codegen.template.model.ServiceTemplateArguments;

/**
 * 配置构建
 */
public class BaseBuilder {

    private final StrategyConfiguration strategyConfiguration;

    public BaseBuilder(StrategyConfiguration strategyConfiguration) {
        this.strategyConfiguration = strategyConfiguration;
    }

    public EntityTemplateArguments.Builder entityBuilder() {
        return strategyConfiguration.entityBuilder();
    }

    public ControllerTemplateArguments.Builder controllerBuilder() {
        return strategyConfiguration.controllerBuilder();
    }

    public MapperTemplateArguments.Builder mapperBuilder() {
        return strategyConfiguration.mapperBuilder();
    }

    public ServiceTemplateArguments.Builder serviceBuilder() {
        return strategyConfiguration.serviceBuilder();
    }

    public StrategyConfiguration build() {
        this.strategyConfiguration.validate();
        return this.strategyConfiguration;
    }
}
