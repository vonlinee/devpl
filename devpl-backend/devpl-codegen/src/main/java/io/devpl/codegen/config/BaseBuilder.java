package io.devpl.codegen.config;

/**
 * 配置构建
 */
public class BaseBuilder implements GenericBuilder<StrategyConfig> {

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

    public Mapper.Builder mapperBuilder() {
        return strategyConfig.mapperBuilder();
    }

    public Service.Builder serviceBuilder() {
        return strategyConfig.serviceBuilder();
    }

    @Override
    public StrategyConfig build() {
        this.strategyConfig.validate();
        return this.strategyConfig;
    }
}
