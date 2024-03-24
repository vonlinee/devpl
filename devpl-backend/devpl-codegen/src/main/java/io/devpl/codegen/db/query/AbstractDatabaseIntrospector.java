package io.devpl.codegen.db.query;

import io.devpl.codegen.core.ContextImpl;

/**
 * 数据库查询抽象类
 */
public abstract class AbstractDatabaseIntrospector implements DatabaseIntrospector {

    protected ContextImpl context;

    @Override
    public void setContext(ContextImpl context) {
        this.context = context;
    }
}
