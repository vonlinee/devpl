package io.devpl.fxui.controller.template;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 封装Velocity
 * <a href="https://stackoverflow.com/questions/1432468/how-to-use-string-as-velocity-template">...</a>
 */
public class VelocityTemplateEngine {

    private static final Logger log = LoggerFactory.getLogger(VelocityTemplateEngine.class);

    /**
     * 直接计算字符串模板
     *
     * @param template 字符串模板
     * @param context  模板数据
     * @return 最终结果
     */
    public static String evaluate(final String template, VelocityContext context) {
        String result;
        try (Writer sw = new StringWriter(); Reader reader = new StringReader(template)) {
            if (Velocity.evaluate(context, sw, "[Velocity]", reader)) {
                result = sw.toString(); // 成功
            } else {
                result = template;
            }
        } catch (IOException e) {
            return template;
        }
        return result;
    }
}
