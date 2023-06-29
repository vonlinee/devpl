package io.devpl.generator.mybatis;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

public class MyXmlStatemtnBuilder extends XMLStatementBuilder {

    // 覆盖父类的final属性
    private XNode context;

    public MyXmlStatemtnBuilder(Configuration configuration, XNode context) {
        // mapper文件路径
        this(configuration, new MapperBuilderAssistant(configuration, null), context, null);
    }

    MyXmlStatemtnBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context, String databaseId) {
        super(configuration, builderAssistant, context, databaseId);
    }

    @Override
    protected <T> Class<? extends T> resolveClass(String alias) {
        Class<? extends T> clazz;
        try {
            clazz = super.resolveClass(alias);
        } catch (Exception exception) {

        }
        clazz = (Class<? extends T>) Integer.class;
        return clazz;
    }
}
