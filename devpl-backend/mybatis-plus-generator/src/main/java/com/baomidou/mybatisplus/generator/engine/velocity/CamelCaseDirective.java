package com.baomidou.mybatisplus.generator.engine.velocity;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * userdirective=com.baomidou.mybatisplus.generator.engine.velocity.CamelCaseDirective
 * 注册自定义指令: velocity.properties
 * userdirective=继承了Directive类的子类全类名
 */
public class CamelCaseDirective extends Directive {

    /**
     * 函数名，也就是模板调用这个函数的名称，比如#hellofun()
     *
     * @return 函数名
     */
    @Override
    public String getName() {
        return "toCamelCase";
    }

    /**
     * 函数类型，可以是行也可以是块函数
     *
     * @return
     * @see DirectiveConstants
     */
    @Override
    public int getType() {
        return DirectiveConstants.LINE;
    }

    /**
     * 如果需要输出，则使用入参的writer.write("some html")，context是当前velocity的容器，可以存取变量，比如在页面上使用#set($name="bwong")，可以通过context.get("name")取出"bwong"，node里面可以取出调用这个函数时的入参，比如#hellofun("a")，通过node.jjtGetChild(0).value(context)取出"a"
     *
     * @param context
     * @param writer
     * @param node
     * @return
     * @throws IOException
     * @throws ResourceNotFoundException
     * @throws ParseErrorException
     * @throws MethodInvocationException
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        // node里面可以取出调用这个函数时的入参
        Object param = node.jjtGetChild(0).value(context);
        // 将参数进行转换进行输出
        writer.write("hello " + node.jjtGetChild(0).value(context));
        return true;
    }
}
