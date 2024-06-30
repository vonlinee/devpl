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

    val lombok = "org.projectlombok:lombok:1.18.22"
    val jupiter = "org.junit.jupiter:junit-jupiter:5.10.3"
    val beetl = "com.ibeetl:beetl:3.7.0.RELEASE"
    val velocity = "org.apache.velocity:velocity-engine-core:2.3"

    val groovy = "org.codehaus.groovy:groovy-all:2.4.15"
    val dataFaker = "net.datafaker:datafaker:2.1.0"

    val fastJson = "com.alibaba.fastjson2:fastjson2:2.0.29"
    val freemarker = "org.freemarker:freemarker:2.3.31"
    val druid = "com.alibaba:druid:1.2.8"
    val gson = "com.google.code.gson:gson:2.10.1"
    val guava = "com.google.guava:guava:32.1.3-jre"

    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"
    val h2 = "com.h2database:h2"
    val kotlinJackson = "com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1"

    val objenesis = "org.objenesis:objenesis:3.3"

    val mybatis = "org.mybatis:mybatis:3.5.15"
    val snakeyaml = "org.yaml:snakeyaml:2.2"

    val json5java = "libs/json5-java/json5-java-2.0.0.jar"

    val dom4j = "org.dom4j:dom4j:2.1.3"
    val jsoup = "org.jsoup:jsoup:1.17.1"

    val jetbrainsAnnotation = "org.jetbrains:annotations:24.0.1"

    // Jackson
    val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON}"
    val jacksonAnnotations = "com.fasterxml.jackson.core:jackson-annotations:${Versions.JACKSON}"
    val jacksonCore = "com.fasterxml.jackson.core:jackson-core:${Versions.JACKSON}"

    // MyBatis-Plus
    val mybatisPlusAnnotation = "com.baomidou:mybatis-plus-annotation:3.5.5"
    val mybatisPlusCore = "com.baomidou:mybatis-plus-core:3.5.5"
    val mybatisPlusExtension = "com.baomidou:mybatis-plus-extension:3.5.5"

    // Spring
    val springWeb = "org.springframework:spring-web:${Versions.SPRING}"
    val springJdbc = "org.springframework:spring-jdbc:${Versions.SPRING}"

    // SpringBoot
    val springBootStarterWeb = "org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}"
    val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT}"
    val springBootStarterJdbc = "org.springframework.boot:spring-boot-starter-data-jdbc:${Versions.SPRING_BOOT}"
    val springBootPropertiesMigrator =
        "org.springframework.boot:spring-boot-properties-migrator:${Versions.SPRING_BOOT}"
    val springBootStarterAop = "org.springframework.boot:spring-boot-starter-aop:${Versions.SPRING_BOOT}"
    val springBootStarterValidation = "org.springframework.boot:spring-boot-starter-validation:3.2.2"
    val springBootConfigurationProcessor = "org.springframework.boot:spring-boot-configuration-processor:3.2.2"
    val tencentcloudSdk = "com.tencentcloudapi:tencentcloud-sdk-java:4.0.11"

    val st4 = "org.antlr:ST4:4.3.4"

    val pagehelper = "com.github.pagehelper:pagehelper:5.3.2"
    val pageHelperSpringBootStarter = "com.github.pagehelper:pagehelper-spring-boot-starter:1.4.6"

    val slf4jApi = "org.slf4j:slf4j-api:2.0.7"

    val mysqlConnector = "com.mysql:mysql-connector-j:8.0.33"

    val mybatisPlusBootStarter3 = "com.baomidou:mybatis-plus-spring-boot3-starter:3.5.5"

    val jsonFlattener = "com.github.wnameless:json-flattener:0.7.1"
    val reactiveStreams = "org.reactivestreams:reactive-streams:1.0.4"
    val jakartaAnnotationApi = "jakarta.annotation:jakarta.annotation-api:2.1.1"

    // Apache Commons
    val dbutils = "commons-dbutils:commons-dbutils:1.7"
    val apacheCommonsText = "org.apache.commons:commons-text:1.10.0"
    val apacheCommonsCli = "commons-cli:commons-cli:1.7.0"
    val apacheCommonsIO = "commons-io:commons-io:2.7"

    // Apache POI
    val poi = "org.apache.poi:poi:4.1.2"
    val poiOoxml = "org.apache.poi:poi-ooxml:4.1.2"

    val javaparserCore = "com.github.javaparser:javaparser-core:3.25.8"
    val javaparserSymbolSolver = "com.github.javaparser:javaparser-symbol-solver-core:3.25.8"

    val jsonApi = "javax.json:javax.json-api:1.1.4"

    val ulid = "com.github.f4b6a3:ulid-creator:5.2.3"
    val jaxb = "javax.xml.bind:jaxb-api:2.3.1"
    val beanutils = "commons-beanutils:commons-beanutils:1.9.4"
    val jsonFlattener2 = "com.github.wnameless.json:json-flattener:0.16.6"
    val googleJavaFormat = "com.google.googlejavaformat:google-java-format:1.19.2"
    val jsqlparser = "com.github.jsqlparser:jsqlparser:4.6"

    val mysqlConnectorJava = "mysql:mysql-connector-java:8.0.33"
    val javapoet = "com.squareup:javapoet:1.13.0"
    val mybatisGenerator = "org.mybatis.generator:mybatis-generator-core:1.4.2"
}

