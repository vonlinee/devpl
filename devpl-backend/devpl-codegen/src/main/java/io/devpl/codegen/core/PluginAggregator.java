package io.devpl.codegen.core;

import java.util.Properties;

/**
 * This class is for internal use only. It contains a list of plugins for the
 * current context and is used to aggregate plugins together. This class
 * implements the rule that if any plugin returns "false" from a method, then no
 * subsequent plugin is called.
 *
 * <p>This class does not follow the normal plugin lifecycle and should not be
 * subclassed by clients.
 */
public final class PluginAggregator extends CompositePlugin {

    @Override
    public void setContext(Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperties(Properties properties) {
        throw new UnsupportedOperationException();
    }
}
