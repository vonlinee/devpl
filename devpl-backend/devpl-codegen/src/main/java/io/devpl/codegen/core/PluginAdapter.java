package io.devpl.codegen.core;

import java.util.Properties;

/**
 * This class is a convenient base class for implementing plugins.
 *
 * <p>This adapter does not implement the <code>validate</code> method - all plugins
 * must perform validation.
 */
public abstract class PluginAdapter implements Plugin {

    protected ContextImpl context;
    protected Properties properties;

    @Override
    public void setContext(ContextImpl context) {
        this.context = context;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
