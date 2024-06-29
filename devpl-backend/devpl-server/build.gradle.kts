import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.devpl.java-conventions")
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

dependencies {
    api("com.github.f4b6a3:ulid-creator:5.2.3")
    api("javax.json:javax.json-api:1.1.4")
    api("jakarta.annotation:jakarta.annotation-api:2.1.1")
    api("org.springframework.boot:spring-boot-starter-validation:3.2.2")
    api("org.springframework.boot:spring-boot-starter-aop:3.2.2")
    api("org.springframework.boot:spring-boot-starter-jdbc:3.2.2")
    api("org.springframework.boot:spring-boot-configuration-processor:3.2.2")
    api("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.5")
    api("com.mysql:mysql-connector-j:8.0.33")
    api("org.freemarker:freemarker:2.3.31")
    api("org.reactivestreams:reactive-streams:1.0.4")
    api("com.github.wnameless:json-flattener:0.7.1")
    api(Libs.fastJson)
    api(Libs.druid)
    api(Libs.gson)
    api(Libs.guava)
    api(project(":devpl-codegen"))
    api("org.apache.commons:commons-text:1.10.0")
    api("commons-dbutils:commons-dbutils:1.7")
    api("com.github.javaparser:javaparser-core:3.25.8")
    api("com.github.javaparser:javaparser-symbol-solver-core:3.25.8")
    api("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    api("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    api("com.fasterxml.jackson.core:jackson-core:2.16.1")
    api("com.tencentcloudapi:tencentcloud-sdk-java:4.0.11")
    api("org.antlr:ST4:4.3.4")
    api("javax.xml.bind:jaxb-api:2.3.1")
    api("com.google.googlejavaformat:google-java-format:1.19.2")
    api(Libs.velocity)
    api(project(":devpl-sdk-internal"))
    api("com.baomidou:mybatis-plus-annotation:3.5.5")
    api("org.slf4j:slf4j-api:2.0.7")
    api("org.jetbrains:annotations:24.0.1")
    api("com.github.jsqlparser:jsqlparser:4.6")
    api("org.jsoup:jsoup:1.17.1")
    api(project(":devpl-commons"))
    api("com.github.wnameless.json:json-flattener:0.16.6")
    api("com.github.pagehelper:pagehelper-spring-boot-starter:1.4.6")
    api("commons-beanutils:commons-beanutils:1.9.4")
    api(Libs.groovy)
    api(Libs.dataFaker)

    // SpringBoot
    implementation(Libs.springBootStarterWeb)
    implementation(Libs.springBootStarterJdbc)
    implementation(Libs.springBootStarterWeb)
    testImplementation(Libs.springBootStarterTest)

    // Jackson's extensions for Kotlin for working with JSON
    implementation(Libs.kotlinJackson)

    // Kotlin's reflection library, required for working with Spring
    implementation(Libs.kotlinReflect)
    runtimeOnly(Libs.h2)

    runtimeOnly(Libs.springBootPropertiesMigrator)
    testImplementation(Libs.objenesis)
    testImplementation(Libs.jupiter)
}

description = "devpl-server"

java {
    withJavadocJar()
}

tasks.withType<KotlinCompile> {

    // Kotlin compiler options
    kotlinOptions {

        // `-Xjsr305=strict` enables the strict mode for JSR-305 annotations
        freeCompilerArgs = listOf("-Xjsr305=strict")

        // This option specifies the target version of the generated JVM bytecode
        jvmTarget = "17"
    }
}

