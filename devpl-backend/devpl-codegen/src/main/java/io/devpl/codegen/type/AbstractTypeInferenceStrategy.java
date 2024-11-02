package io.devpl.codegen.type;

public abstract class AbstractTypeInferenceStrategy<T> implements TypeInferenceStrategy<T> {

    boolean active;
    int priority = 0;

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public final int getPriority() {
        return priority;
    }

    public final void setPriority(int priority) {
        this.priority = priority;
    }
}
