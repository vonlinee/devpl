package io.devpl.backend.tools.mybatis;

import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.session.Configuration;

import java.util.Map;

public class DynamicContextVisitor extends DynamicContext {

    public DynamicContextVisitor(Configuration configuration, Object parameterObject) {
        super(configuration, parameterObject);
    }

    @Override
    public Map<String, Object> getBindings() {
        final Map<String, Object> bindings = super.getBindings();
        System.out.println(bindings);
        return bindings;
    }

    @Override
    public void bind(String name, Object value) {
        System.out.println("bind => " + name + " " + value);
        super.bind(name, value);
    }

    @Override
    public void appendSql(String sql) {
        System.out.println("appendSql => " + sql);
        super.appendSql(sql);
    }

    @Override
    public String getSql() {
        return super.getSql();
    }

    @Override
    public int getUniqueNumber() {
        return super.getUniqueNumber();
    }
}
