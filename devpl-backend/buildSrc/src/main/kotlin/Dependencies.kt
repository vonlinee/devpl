/**
 * 统一依赖版本号
 */
object Versions {
    const val JACKSON = "2.16.1"
    const val SPRING = "6.1.3"
    const val SPRING_BOOT = "3.2.2"
}

/**
 * 全局依赖版本
 */
object Libs {
    // Log
    const val SLF4J_API = "org.slf4j:slf4j-api:2.0.7"
    const val LOGBACK_CLASSIC = "ch.qos.logback:logback-classic:1.4.8"

    const val LOMBOK = "org.projectlombok:lombok:1.18.22"

    const val BEETL = "com.ibeetl:beetl:3.7.0.RELEASE"
    const val VELOCITY = "org.apache.velocity:velocity-engine-core:2.3"

    // Test
    const val JUPITER = "org.junit.jupiter:junit-jupiter:5.10.3"
    const val JUPITER_API = "org.junit.jupiter:junit-jupiter-api:5.11.0-M1"

    const val GROOVY = "org.codehaus.groovy:groovy-all:2.4.15"
    const val DATA_FAKER = "net.datafaker:datafaker:2.1.0"

    const val FASTJSON2 = "com.alibaba.fastjson2:fastjson2:2.0.29"
    const val FREEMARKER = "org.freemarker:freemarker:2.3.31"
    const val JFINAL_ENJOY = "com.jfinal:enjoy:5.0.0"
    const val DRUID = "com.alibaba:druid:1.2.8"
    const val GSON = "com.google.code.gson:gson:2.10.1"
    const val GUAVA_JRE = "com.google.guava:guava:32.1.3-jre"

    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    const val H2 = "com.h2database:h2:2.3.232"
    const val KOTLIN_JACKSON = "com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1"

    const val OBJENESIS = "org.objenesis:objenesis:3.3"

    const val MYBATIS = "org.mybatis:mybatis:3.5.15"
    const val SNAKE_YAML = "org.yaml:snakeyaml:2.2"

    // val json5java = "libs/json5-java/json5-java-2.0.0.jar"

    const val DOM4J = "org.dom4j:dom4j:2.1.3"
    const val JSOUP = "org.jsoup:jsoup:1.17.1"

    const val JETBRAINS_ANNOTATION = "org.jetbrains:annotations:24.0.1"

    // Jackson
    const val JACKSON_DATABIND = "com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON}"
    const val JACKSON_ANNOTATIONS = "com.fasterxml.jackson.core:jackson-annotations:${Versions.JACKSON}"
    const val JACKSON_CORE = "com.fasterxml.jackson.core:jackson-core:${Versions.JACKSON}"

    // MyBatis-Plus
    const val MYBATIS_PLUS_ANNOTATION = "com.baomidou:mybatis-plus-annotation:3.5.5"
    const val MYBATIS_PLUS_CORE = "com.baomidou:mybatis-plus-core:3.5.5"
    const val MYBATIS_PLUS_EXTENSION = "com.baomidou:mybatis-plus-extension:3.5.5"

    // Spring
    const val SPRING_WEB = "org.springframework:spring-web:${Versions.SPRING}"
    const val SPRING_JDBC = "org.springframework:spring-jdbc:${Versions.SPRING}"

    // SpringBoot
    const val SPRING_BOOT_STARTER_WEB = "org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_DATA_JDBC = "org.springframework.boot:spring-boot-starter-data-jdbc:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_PROPERTIES_MIGRATOR =
        "org.springframework.boot:spring-boot-properties-migrator:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_AOP = "org.springframework.boot:spring-boot-starter-aop:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_VALIDATION = "org.springframework.boot:spring-boot-starter-validation:3.2.2"
    const val SPRING_BOOT_CONFIGURATION_PROCESSOR = "org.springframework.boot:spring-boot-configuration-processor:3.2.2"
    const val TENCENT_CLOUD_SDK = "com.tencentcloudapi:tencentcloud-sdk-java:4.0.11"

    const val ANTLR_ST4 = "org.antlr:ST4:4.3.4"

    const val PAGE_HELPER = "com.github.pagehelper:pagehelper:5.3.2"
    const val PAGE_HELPER_SPRING_BOOT_STARTER = "com.github.pagehelper:pagehelper-spring-boot-starter:1.4.6"

    const val MYSQL_CONNECTOR = "com.mysql:mysql-connector-j:8.0.33"

    const val MYBATIS_PLUS_SPRING_BOOT_STATER3 = "com.baomidou:mybatis-plus-spring-boot3-starter:3.5.5"

    const val REACTIVE_STREAMS = "org.reactivestreams:reactive-streams:1.0.4"
    const val JAKARTA_ANNOTATION_API = "jakarta.annotation:jakarta.annotation-api:2.1.1"

    // Apache Commons
    const val COMMONS_DBUTILS = "commons-dbutils:commons-dbutils:1.7"
    const val APACHE_COMMONS_TEXT = "org.apache.commons:commons-text:1.12.0"
    const val APACHE_COMMONS_LANG3 = "org.apache.commons:commons-lang3:3.14.0"
    const val APACHE_COMMONS_IO = "commons-io:commons-io:2.7"
    const val APACHE_COMMONS_COLLECTIONS = "commons-collections:commons-collections:3.2.2"
    const val APACHE_COMMONS_COLLECTIONS4 = "org.apache.commons:commons-collections4:4.4"
    const val APACHE_COMMONS_DBCP2 = "org.apache.commons:commons-dbcp2:2.11.0"
    const val APACHE_COMMONS_POOL2 = "org.apache.commons:commons-pool2:2.12.0"

    // https://mvnrepository.com/artifact/commons-cli/commons-cli
    const val APACHE_COMMONS_CLI = "commons-cli:commons-cli:1.9.0"

    const val SWAGGER_ANNOTATIONS = "io.swagger:swagger-annotations:1.6.2"

    const val ANT = "org.apache.ant:ant:1.9.16"

    // Apache POI
    const val POI = "org.apache.poi:poi:4.1.2"
    const val POI_OOXML = "org.apache.poi:poi-ooxml:4.1.2"

    // Easypoi
    const val EASY_POI_BASE = "cn.afterturn:easypoi-base:4.4.0"
    const val EASY_POI_ANNOTATION = "cn.afterturn:easypoi-annotation:4.4.0"

    const val JAVA_PARSER_CORE = "com.github.javaparser:javaparser-core:3.25.8"
    const val JAVA_PARSER_SYMBOL_SOLVER = "com.github.javaparser:javaparser-symbol-solver-core:3.25.8"
    const val JAVAX_JSON_API = "javax.json:javax.json-api:1.1.4"
    const val JODA_TYPE_TOOLS = "net.jodah:typetools:0.6.3"
    const val ULID = "com.github.f4b6a3:ulid-creator:5.2.3"
    const val JAXB = "javax.xml.bind:jaxb-api:2.3.1"
    const val BEANUTILS = "commons-beanutils:commons-beanutils:1.9.4"
    const val JSON_FLATTENER = "com.github.wnameless.json:json-flattener:0.16.6"
    const val GOOGLE_JAVA_FORMAT = "com.google.googlejavaformat:google-java-format:1.19.2"
    const val JSQL_PARSER = "com.github.jsqlparser:jsqlparser:4.6"

    const val MYSQL_CONNECTOR_JAVA = "mysql:mysql-connector-java:8.0.33"
    const val JAVAPOET = "com.squareup:javapoet:1.13.0"
    const val MYBATIS_GENERATOR = "org.mybatis.generator:mybatis-generator-core:1.4.2"

    const val JOOQ = "org.jooq:jooq:3.19.7"

    const val JSR250 = "javax.annotation:jsr250-api:1.0"
    const val JNA = "com.sun.jna:jna:3.0.9"

    const val JAKARTA_INJECT_API = "jakarta.inject:jakarta.inject-api:2.0.1"
    const val JSCH = "com.jcraft:jsch:0.1.54"

    // ORM框架Ktorm
    const val KTORM_CORE = "org.ktorm:ktorm-core:3.5.0"
    const val KTORM_JACKSON = "org.ktorm:ktorm-jackson:3.5.0"
    const val KTORM_MYSQL = "org.ktorm:ktorm-support-mysql:3.5.0"
}

