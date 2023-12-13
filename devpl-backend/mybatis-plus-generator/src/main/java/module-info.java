open module mybatis.plus.generator {
    exports com.baomidou.mybatisplus.generator.config.builder;
    exports com.baomidou.mybatisplus.generator.config;
    exports com.baomidou.mybatisplus.generator.config.rules;
    exports com.baomidou.mybatisplus.generator.engine;
    exports com.baomidou.mybatisplus.generator;
    exports com.baomidou.mybatisplus.generator.config.po;
    exports com.baomidou.mybatisplus.generator.codegen;
    exports com.baomidou.mybatisplus.generator.query;
    exports com.baomidou.mybatisplus.generator.type;
    exports com.baomidou.mybatisplus.generator.config.querys;
    exports com.baomidou.mybatisplus.generator.jdbc;
    exports com.baomidou.mybatisplus.generator.jdbc.meta;
    exports com.baomidou.mybatisplus.generator.jdbc.dialect.mysql;
    exports com.baomidou.mybatisplus.generator.fill;
    exports com.baomidou.mybatisplus.generator.engine.velocity;
    exports com.baomidou.mybatisplus.generator.engine.beetl;
    exports com.baomidou.mybatisplus.generator.engine.freemarker;
    exports com.baomidou.mybatisplus.generator.engine.enjoy;

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
