package io.devpl.codegen.config;

import io.devpl.codegen.config.Entity;

/**
 * 配置构建
 */
public class BaseBuilder implements GenericBuilder<StrategyConfig> {

    private final StrategyConfig strategyConfig;

    public BaseBuilder(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
    }

    public Entity.Builder entityBuilder() {
        return strategyConfig.entityBuilder();
    }

    public Controller.Builder controllerBuilder() {
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
