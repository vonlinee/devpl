package io.devpl.fxui.controller.template;

import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.visitor.BaseVisitor;

/**
 * https://lists.apache.org/thread/oymlqgqnnc6wqrfwrynkooqf89oxx993
 * <p>
 * https://stackoverflow.com/questions/18572610/how-can-i-see-all-variables-available-inside-a-velocity-template
 */
public class VariableExtractor extends BaseVisitor {

    public static void extract(SimpleNode node, Object object) {
        VariableExtractor extractor = new VariableExtractor();
        node.jjtAccept(extractor, object);
    }

    /**
     * will be called on each reference (variable) visiting.
     *
     * @param node node
     * @param data data
     * @return
     */
    @Override
    public Object visit(ASTReference node, Object data) {
        String name = node.literal(); //here we`ll have variable name
        System.out.println(name);
        return super.visit(node, data);
    }

    /**
     * will be called on each directive visiting.
     *
     * @param node
     * @param data
     * @return
     */
    @Override
    public Object visit(ASTDirective node, Object data) {
        return super.visit(node, data);
    }
}
