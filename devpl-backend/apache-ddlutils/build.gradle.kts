plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api(Libs.SLF4J_API)
    api(Libs.JETBRAINS_ANNOTATION)
    api(Libs.LOGBACK_CLASSIC)
    testImplementation(Libs.JUPITER_API)
    testImplementation(Libs.dom4j)
    testImplementation(Libs.apacheCommonsDbcp2)
    testImplementation(Libs.MYSQL_CONNECTOR_JAVA)
}

group = "org.example"
description = "apache-ddlutils"

java {
    withJavadocJar()
}
