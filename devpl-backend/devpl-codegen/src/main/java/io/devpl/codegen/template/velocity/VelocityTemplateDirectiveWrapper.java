package io.devpl.codegen.template.velocity;

import io.devpl.codegen.template.TemplateDirective;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.node.Node;

public abstract class VelocityTemplateDirectiveWrapper extends VelocityTemplateDirective {

    private TemplateDirective directive;

    public VelocityTemplateDirectiveWrapper() {
    }

    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
        super.init(rs, context, node);
        VelocityTemplateDirectiveWrapper wrapper = (VelocityTemplateDirectiveWrapper) rs.getDirective(this.getName());
        this.directive = wrapper.directive;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return directive.getParameterTypes();
    }

    @Override
    public String render(Object[] params) {
        return directive.render(params);
    }
}
