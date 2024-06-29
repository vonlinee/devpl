plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api("org.slf4j:slf4j-api:2.0.7")
    api("ch.qos.logback:logback-classic:1.4.8")
    api("com.alibaba.fastjson2:fastjson2:2.0.29")
    api("com.jfinal:enjoy:5.0.0")
    api("org.freemarker:freemarker:2.3.31")
    api("io.swagger:swagger-annotations:1.6.2")
    api(Libs.velocity)
    api(Libs.beetl)
    api("org.projectlombok:lombok:1.18.28")
    api("org.jetbrains:annotations:24.0.1")
    api(project(":devpl-sdk-internal"))
    api("com.github.javaparser:javaparser-core:3.25.8")
    api("com.alibaba:druid:1.2.8")
    api("org.antlr:ST4:4.3.4")
    api("commons-io:commons-io:2.16.0")
    api("com.github.jsqlparser:jsqlparser:4.6")
    api("org.apache.commons:commons-lang3:3.14.0")
    api("org.mybatis.generator:mybatis-generator-core:1.4.2")
    api("org.apache.ant:ant:1.9.16")
    api("org.jooq:jooq:3.19.7")
    api(project(":apache-ddlutils"))
    testImplementation("com.h2database:h2:2.1.214")
    testImplementation("mysql:mysql-connector-java:8.0.33")
    testImplementation("com.formdev:flatlaf:1.0")
    testImplementation("com.formdev:flatlaf-intellij-themes:1.0")
    testImplementation("org.apache.commons:commons-dbcp2:2.11.0")
    testImplementation("org.apache.commons:commons-pool2:2.12.0")
    testImplementation("org.dom4j:dom4j:2.1.4")
    testImplementation(Libs.jupiter)

    annotationProcessor(Libs.lombok)
}

description = "devpl-codegen"

java {
    withJavadocJar()

    sourceSets {
        main {

            resources {
                include("**/*.properties")
            }
        }
    }
}

/**
 * 资源处理
 */
tasks.processResources {

    from("src/main/java") {

        // 将src/main/java中的任意一级的目录中的以.properties结尾的文件打包到pack中
        include("**/*.properties")
    }

    from("src/main/resources") {
        include("*")
    }
}
