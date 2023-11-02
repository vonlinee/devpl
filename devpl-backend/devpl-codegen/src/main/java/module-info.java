module devpl.codegen {
    requires org.slf4j;
    requires java.sql;
    requires com.baomidou.mybatis.plus.annotation;
    requires freemarker;
    requires com.alibaba.fastjson2;
    requires druid;
    requires lombok;
    requires jsqlparser;
    requires org.apache.commons.text;
    requires com.github.javaparser.core;
    requires org.jetbrains.annotations;
    requires velocity.engine.core;

    requires org.mybatis;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires okhttp;
    requires java.net.http;
    requires spring.core;
    requires tencentcloud.sdk.java;
    requires com.google.common;

    exports io.devpl.codegen;

    exports io.devpl.codegen.parser.sql;
    exports io.devpl.codegen.utils;

    exports io.devpl.codegen.parser.java;

    opens io.devpl.codegen to com.google.gson;
    opens io.devpl.codegen.parser.java to com.google.gson;
    exports io.devpl.codegen.ddl.service;

    requires java.desktop;
    requires devpl.sdk.internal;
}
