open module mybatis.plus.generator {
    exports io.devpl.codegen.core;
    exports io.devpl.codegen.db.query;
    exports io.devpl.codegen.db.querys;
    exports io.devpl.codegen.jdbc;
    exports io.devpl.codegen.jdbc.meta;
    exports io.devpl.codegen.db.dialect.mysql;
    exports io.devpl.codegen.template.velocity;
    exports io.devpl.codegen.template.beetl;
    exports io.devpl.codegen.template.freemarker;
    exports io.devpl.codegen.template.enjoy;
    exports io.devpl.codegen.template;
    exports io.devpl.codegen.db;
    exports io.devpl.codegen.strategy;
    exports io.devpl.codegen.config;

    requires com.baomidou.mybatis.plus.core;
    requires com.baomidou.mybatis.plus.annotation;
    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires beetl;
    requires enjoy;
    requires velocity.engine.core;
    requires freemarker;
    requires java.sql;
    requires org.mybatis;
    requires lombok;
    requires org.hibernate.orm.core;
    requires com.google.common;
    requires devpl.sdk.internal;
}
