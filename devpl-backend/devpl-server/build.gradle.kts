import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.devpl.java-conventions")
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

dependencies {
    implementation(Libs.ulid)
    implementation(Libs.jsonApi)
    implementation(Libs.jakartaAnnotationApi)

    implementation(Libs.mybatisPlusBootStarter3)
    implementation(Libs.mysqlConnector)
    implementation(Libs.freemarker)
    implementation(Libs.reactiveStreams)
    implementation(Libs.jsonFlattener)
    implementation(Libs.fastJson)
    implementation(Libs.druid)
    implementation(Libs.gson)
    implementation(Libs.guava)
    implementation(Libs.apacheCommonsText)
    implementation(Libs.dbutils)
    implementation(Libs.javaparserCore)
    implementation(Libs.javaparserSymbolSolver)
    implementation(Libs.jacksonDatabind)
    implementation(Libs.jacksonAnnotations)
    implementation(Libs.jacksonCore)
    implementation(Libs.tencentcloudSdk)
    implementation(Libs.st4)
    implementation(Libs.jaxb)
    implementation(Libs.googleJavaFormat)
    implementation(Libs.velocity)
    implementation(Libs.mybatisPlusAnnotation)
    implementation(Libs.slf4jApi)
    implementation(Libs.jetbrainsAnnotation)
    implementation(Libs.jsqlparser)
    implementation(Libs.jsoup)
    implementation(Libs.jsonFlattener2)
    implementation(Libs.pageHelperSpringBootStarter)
    implementation(Libs.beanutils)
    implementation(Libs.groovy)
    implementation(Libs.dataFaker)
    // SpringBoot
    implementation(Libs.springBootStarterWeb)
    implementation(Libs.springBootStarterJdbc)
    implementation(Libs.springBootStarterValidation)
    implementation(Libs.springBootStarterAop)
    implementation(Libs.springBootConfigurationProcessor)
    // Jackson's extensions for Kotlin for working with JSON
    implementation(Libs.kotlinJackson)
    // Kotlin's reflection library, required for working with Spring
    implementation(Libs.kotlinReflect)

    implementation(project(":devpl-sdk-internal"))
    implementation(project(":devpl-commons"))
    implementation(project(":devpl-codegen"))

    runtimeOnly(Libs.h2)
    runtimeOnly(Libs.springBootPropertiesMigrator)

    testImplementation(Libs.springBootStarterTest)
    testImplementation(Libs.objenesis)
    testImplementation(Libs.jupiter)

    annotationProcessor(Libs.lombok)
}

description = "devpl-server"

java {
    withJavadocJar()
}

tasks.withType<KotlinCompile> {

    /**
     * Kotlin compiler options
     * https://spring.io/guides/tutorials/spring-boot-kotlin
     */
    kotlinOptions {

        // `-Xjsr305=strict` enables the strict mode for JSR-305 annotations
        freeCompilerArgs = listOf("-Xjsr305=strict")

        // This option specifies the target version of the generated JVM bytecode
        jvmTarget = "17"
    }
}

configurations {
    /**
     * 全局移除 commons-logging 依赖
     */
    configureEach {
        exclude("commons-logging", "commons-logging")
    }
}

