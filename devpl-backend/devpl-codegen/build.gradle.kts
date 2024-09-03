plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api(Libs.SLF4J_API)
    api(Libs.LOGBACK_CLASSIC)
    api(Libs.FASTJSON2)
    api(Libs.JFINAL_ENJOY)
    api(Libs.FREEMARKER)
    api(Libs.SWAGGER_ANNOTATIONS)
    api(Libs.VELOCITY)
    api(Libs.BEETL)
    api(Libs.LOMBOK)
    api(Libs.JETBRAINS_ANNOTATION)
    api(Libs.JAVA_PARSER_CORE)
    api(Libs.DRUID)
    api(Libs.ANTLR_ST4)
    api(Libs.APACHE_COMMONS_IO)
    api(Libs.JSQL_PARSER)
    api(Libs.APACHE_COMMONS_LANG3)
    api(Libs.MYBATIS_GENERATOR)
    api(Libs.ANT)
    api(Libs.JOOQ)

    api(project(":devpl-sdk-internal"))
    api(project(":apache-ddlutils"))

    testImplementation(Libs.H2)
    testImplementation(Libs.MYSQL_CONNECTOR_JAVA)
    testImplementation("com.formdev:flatlaf:1.0")
    testImplementation("com.formdev:flatlaf-intellij-themes:1.0")
    testImplementation(Libs.apacheCommonsDbcp2)
    testImplementation(Libs.apacheCommonsPool2)
    testImplementation(Libs.dom4j)
    testImplementation(Libs.JUPITER)

    annotationProcessor(Libs.LOMBOK)
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
