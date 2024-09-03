import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.devpl.java-conventions")
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

dependencies {
    implementation(Libs.ULID)
    implementation(Libs.JAVAX_JSON_API)
    implementation(Libs.jakartaAnnotationApi)

    implementation(Libs.mybatisPlusBootStarter3)
    implementation(Libs.mysqlConnector)
    implementation(Libs.FREEMARKER)
    implementation(Libs.reactiveStreams)
    implementation(Libs.jsonFlattener)
    implementation(Libs.FASTJSON2)
    implementation(Libs.DRUID)
    implementation(Libs.GSON)
    implementation(Libs.GUAVA_JRE)
    implementation(Libs.apacheCommonsText)
    implementation(Libs.dbutils)
    implementation(Libs.JAVA_PARSER_CORE)
    implementation(Libs.JAVA_PARSER_SYMBOL_SOLVER)
    implementation(Libs.jacksonDatabind)
    implementation(Libs.jacksonAnnotations)
    implementation(Libs.jacksonCore)
    implementation(Libs.tencentcloudSdk)
    implementation(Libs.ANTLR_ST4)
    implementation(Libs.JAXB)
    implementation(Libs.googleJavaFormat)
    implementation(Libs.VELOCITY)
    implementation(Libs.mybatisPlusAnnotation)
    implementation(Libs.SLF4J_API)
    implementation(Libs.JETBRAINS_ANNOTATION)
    implementation(Libs.JSQL_PARSER)
    implementation(Libs.jsoup)
    implementation(Libs.JSON_FLATTENER)
    implementation(Libs.pageHelperSpringBootStarter)
    implementation(Libs.BEANUTILS)
    implementation(Libs.GROOVY)
    implementation(Libs.DATA_FAKER)
    // SpringBoot
    implementation(Libs.springBootStarterWeb)
    implementation(Libs.springBootStarterJdbc)
    implementation(Libs.springBootStarterValidation)
    implementation(Libs.springBootStarterAop)
    implementation(Libs.springBootConfigurationProcessor)
    // Jackson's extensions for Kotlin for working with JSON
    implementation(Libs.KOTLIN_JACKSON)
    // Kotlin's reflection library, required for working with Spring
    implementation(Libs.KOTLIN_REFLECT)

    implementation(project(":devpl-sdk-internal"))
    implementation(project(":devpl-commons"))
    implementation(project(":devpl-codegen"))

    implementation(Libs.APACHE_COMMONS_CLI)

    runtimeOnly(Libs.H2)
    runtimeOnly(Libs.springBootPropertiesMigrator)

    testImplementation(Libs.springBootStarterTest)
    testImplementation(Libs.objenesis)
    testImplementation(Libs.JUPITER)

    annotationProcessor(Libs.LOMBOK)
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

