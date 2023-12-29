open module devpl.codegen {
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
    exports io.devpl.codegen.type;
    exports io.devpl.codegen;
    exports io.devpl.codegen.template.model;
    exports io.devpl.codegen.parser;
    exports io.devpl.codegen.parser.java;
    exports io.devpl.codegen.lang;

    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires beetl;
    requires enjoy;
    requires velocity.engine.core;
    requires freemarker;
    requires java.sql;
    requires lombok;
    requires devpl.sdk.internal;
    requires org.mybatis.generator;
    requires com.github.javaparser.core;
    requires druid;
    requires jsqlparser;
}
