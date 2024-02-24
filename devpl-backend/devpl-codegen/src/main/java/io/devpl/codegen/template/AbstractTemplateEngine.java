package io.devpl.codegen.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 模板引擎抽象类
 */
public abstract class AbstractTemplateEngine implements TemplateEngine {

    protected Properties properties;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public final void setProperties(Properties properties) {
        this.properties = properties;
    }

    public final String getProperties(String key) {
        return properties.getProperty(key);
    }
}
