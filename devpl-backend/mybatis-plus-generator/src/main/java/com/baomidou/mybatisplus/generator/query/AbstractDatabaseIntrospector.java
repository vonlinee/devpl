package com.baomidou.mybatisplus.generator.query;

import com.baomidou.mybatisplus.generator.config.builder.Context;
import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;

import java.util.List;

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
