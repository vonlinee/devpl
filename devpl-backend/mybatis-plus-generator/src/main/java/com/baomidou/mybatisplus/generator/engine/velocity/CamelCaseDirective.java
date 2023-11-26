package com.baomidou.mybatisplus.generator.engine.velocity;

import com.baomidou.mybatisplus.generator.codegen.NamingStrategy;
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
     * BLOCK: 表示该指令会替换其所在位置的所有内容，需要end结束符
     * LINE: 不要end结束符
     *
     * @return 指令类型
     * @see DirectiveConstants
     */
    @Override
    public int getType() {
        return DirectiveConstants.LINE;
    }

    /**
     * @param context 上下文 当前velocity的容器，可以存取变量，比如在页面上使用#set($name="bwong")，可以通过context.get("name")取出"bwong"
     * @param writer  输出位置
     * @param node    节点 node里面可以取出调用这个函数时的入参，比如#hellofun("a")，通过node.jjtGetChild(0).value(context)取出"a"
     * @return 是否成功
     * @throws IOException               IOException
     * @throws ResourceNotFoundException 模板不存在
     * @throws ParseErrorException       模板语法错误
     * @throws MethodInvocationException 反射执行异常
     */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        // 获取指令传参 传参的文字文本或者对象
        Object value = node.jjtGetChild(0).value(context);
        // 将参数进行转换进行输出
        if (value instanceof String) {
            writer.write(NamingStrategy.underlineToCamel((String) value));
        } else {
            writer.write(String.valueOf(value));
        }
        return true;
    }
}
