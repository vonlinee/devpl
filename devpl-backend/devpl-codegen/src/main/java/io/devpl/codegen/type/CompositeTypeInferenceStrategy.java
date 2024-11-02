package io.devpl.codegen.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CompositeTypeInferenceStrategy<T> implements TypeInferenceStrategy<T> {

    List<TypeInferenceStrategy<T>> strategies;

    @Override
    public void addStrategy(TypeInferenceStrategy<T> strategy) {
        if (strategies == null) {
            strategies = new ArrayList<>();
        }
        strategies.add(strategy);
    }

    @Override
    public final void setActive(boolean active) {
        if (this.strategies == null || this.strategies.isEmpty()) {
            return;
        }
        for (TypeInferenceStrategy<T> strategy : this.strategies) {
            strategy.setActive(active);
        }
    }

    @Override
    public boolean isActive() {
        if (this.strategies == null || this.strategies.isEmpty()) {
            return false;
        }
        for (TypeInferenceStrategy<T> strategy : this.strategies) {
            if (strategy.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void inferType(Collection<T> collection) {
        if (this.strategies == null || this.strategies.isEmpty()) {
            return;
        }
        for (TypeInferenceStrategy<T> strategy : this.strategies) {
            if (strategy.isActive()) {
                strategy.accept(collection);
            }
        }
    }

    @Override
    public int getPriority() {
        return TypeInferenceStrategy.LOWEST_PRIORITY;
    }
}
