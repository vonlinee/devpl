open module devpl.codegen {
    exports io.devpl.codegen.generator;
    exports io.devpl.codegen.db.query;
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
    exports io.devpl.codegen.generator.config;
    exports io.devpl.codegen.type;
    exports io.devpl.codegen;
    exports io.devpl.codegen.template.model;
    exports io.devpl.codegen.parser;
    exports io.devpl.codegen.parser.java;
    exports io.devpl.codegen.lang;
    exports io.devpl.codegen.util;
    exports io.devpl.codegen.db.dialect;
    exports io.devpl.codegen.generator.config.xml;

    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires transitive java.sql;
    requires beetl;
    requires enjoy;
    requires velocity.engine.core;
    requires freemarker;
    requires lombok;
    requires devpl.sdk.internal;
    requires com.github.javaparser.core;
    requires druid;
    requires jsqlparser;
    requires java.compiler;
    requires org.mybatis.generator;
    requires java.desktop;
    requires java.annotation;
    requires ant;

    requires org.jooq;
}
