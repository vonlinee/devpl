package io.devpl.codegen.template.velocity;

import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateException;
import org.apache.velocity.VelocityContext;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;
import java.util.Map;
import java.util.Set;

/**
 * 针对Velocity模板的包装
 *
 * @see org.apache.velocity.Template
 */
class VelocityTemplate implements Template {

    @NotNull org.apache.velocity.Template template;

    public VelocityTemplate(@NotNull org.apache.velocity.Template template) {
        this.template = template;
    }

    @Override
    public @NotNull String getName() {
        return template.getName();
    }

    @Override
    public String getContent() {
        return template.getName();
    }

    @Override
    public void setContent(String content) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(TemplateEngine engine, Object arguments, Writer writer) throws TemplateException {
        if (arguments instanceof Map<?, ?> argumentsMap) {
            if (argumentsMap.isEmpty()) {
                throw new TemplateException("template arguments is empty");
            }
            final Set<?> keys = ((Map<?, ?>) arguments).keySet();
            // 运行时检查
            for (Object key : keys) {
                if (!(key instanceof String)) {
                    throw new TemplateException("use map as template data model, but some keys is not a String type");
                }
            }
            template.merge(new VelocityContext((Map<String, Object>) argumentsMap), writer);
            return;
        }
        throw new TemplateException("Velocity仅支持Map作为模板参数");
    }
}
