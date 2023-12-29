package io.devpl.codegen.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模板引擎抽象类
 */
public abstract class AbstractTemplateEngine implements TemplateEngine {

    protected final Logger log = LoggerFactory.getLogger(getClass());
}
