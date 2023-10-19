package io.devpl.fxui.controller.template;

import org.apache.velocity.Template;
import org.apache.velocity.runtime.parser.node.*;

/**
 * <a href="https://developer.aliyun.com/article/64918">...</a>
 */
public class VelocityTemplateAnalyzer implements TemplateVariableAnalyzer<Template> {

    @Override
    public boolean support(Class<?> templateClass) {
        return templateClass == Template.class;
    }

    @Override
    public void analyze(Template template) {
        parseTemplate(template);
    }

    public static void parseTemplate(Template template) {
        Object data = template.getData();
        if (data instanceof SimpleNode) {
            SimpleNode sn = (SimpleNode) data;
            recursive(sn);
        } else {
            throw new RuntimeException(String.valueOf(data.getClass()));
        }
    }

    /**
     * 递归遍历AST树
     * 深度优先
     *
     * @param parent AST点
     */
    public static void recursive(Node parent) {
        int numOfChildren = parent.jjtGetNumChildren();
        if (numOfChildren <= 0) {
            return;
        }
        for (int i = 0; i < numOfChildren; i++) {
            Node node = parent.jjtGetChild(i);
            if (node instanceof ASTText) {
                // 普通文本节点
                ASTText astText = (ASTText) node;
                recursive(astText);
            } else if (node instanceof ASTReference) {
                ASTReference astReference = (ASTReference) node;
                recursive(astReference);
            } else if (node instanceof ASTDirective) {
                ASTDirective astDirective = (ASTDirective) node;
                recursive(astDirective);
            } else if (node instanceof ASTIfStatement) {
                ASTIfStatement astIfStatement = (ASTIfStatement) node;
                recursive(astIfStatement);
            } else if (node instanceof ASTComment) {
                ASTComment astComment = (ASTComment) node;
                recursive(astComment);
            } else if (node instanceof ASTBlock) {
                ASTBlock astBlock = (ASTBlock) node;
                recursive(astBlock);
            } else if (node instanceof ASTDivNode) {
                ASTDivNode astDivNode = (ASTDivNode) node;
                recursive(astDivNode);
            } else if (node instanceof ASTExpression) {
                ASTExpression astExpression = (ASTExpression) node;
                recursive(astExpression);
            } else if (node instanceof ASTElseIfStatement) {
                ASTElseIfStatement astElseIfStatement = (ASTElseIfStatement) node;
                recursive(astElseIfStatement);
            } else if (node instanceof ASTVariable) {
                ASTVariable astVariable = (ASTVariable) node;
                recursive(astVariable);
            } else if (node instanceof ASTMethod) {
                ASTMethod astMethod = (ASTMethod) node;
                recursive(astMethod);
            } else if (node instanceof ASTIdentifier) {
                ASTIdentifier astIdentifier = (ASTIdentifier) node;
                System.out.println(astIdentifier.literal());
                recursive(astIdentifier);
            }
        }
    }
}
