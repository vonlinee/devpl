package io.devpl.codegen.db.query;

import io.devpl.codegen.core.Context;

/**
 * 数据库查询抽象类
 *
 * @author nieqiurong
 * @since 3.5.3
 */
public abstract class AbstractDatabaseIntrospector implements DatabaseIntrospector {

    protected Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}