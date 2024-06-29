plugins {
    id("io.devpl.java-conventions")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.openjfx:javafx-plugin:0.1.0")
    }
}

apply(plugin = "org.openjfx.javafxplugin")

dependencies {

    api("io.github.palexdev:materialfx:11.17.0")
    api("com.github.hervegirod:fxsvgimage:1.1")
    api("commons-cli:commons-cli:1.7.0")

    api("org.fxmisc.richtext:richtextfx:0.11.2")
    api("org.jetbrains:annotations:24.0.1")
    api("com.google.googlejavaformat:google-java-format:1.19.2")
    api("org.mybatis:mybatis:3.5.15")
    api("com.github.pagehelper:pagehelper:5.3.2")
    api("com.dlsc.formsfx:formsfx-core:11.6.0")
    api("mysql:mysql-connector-java:8.0.33")
    api(Libs.gson)
    api("org.apache.poi:poi:4.1.2")
    api("org.apache.poi:poi-ooxml:4.1.2")
    api("cn.afterturn:easypoi-base:4.4.0")
    api("cn.afterturn:easypoi-annotation:4.4.0")
    api(Libs.dom4j)
    api("com.sun.jna:jna:3.0.9")
    api("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    api("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
    api("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.3.1")
    api("javax.annotation:jsr250-api:1.0")
    api("jakarta.inject:jakarta.inject-api:2.0.1")
    api("com.jcraft:jsch:0.1.54")
    api("commons-io:commons-io:2.7")
    api(Libs.snakeyaml)
    api(Libs.springJdbc)
    api("commons-dbutils:commons-dbutils:1.7")
    api("org.antlr:ST4:4.3.4")
    api("com.squareup:javapoet:1.13.0")
    api("org.mybatis.generator:mybatis-generator-core:1.4.2")
    api(Libs.mybatisPlusAnnotation)
    api(Libs.mybatisPlusCore)
    api(Libs.mybatisPlusExtension)

    implementation(project(":devpl-sdk-internal"))
    implementation(project(":devpl-commons"))
    implementation(project(":devpl-codegen"))
    implementation(project(":devpl-codegen"))

    annotationProcessor(Libs.lombok)
}

description = "devpl-fxui"

java {
    withJavadocJar()

    sourceSets {
        main {

            resources {
                include("**/*.fxml")
                include("**/*.xml")
                include("**/*.properties")

                include("**/*.css")
                include("**/*.html")
                include("**/*.js")

                include("codemirror/*")
            }
        }
    }
}

javafx {
    version = "17.0.1"

    modules("javafx.controls", "javafx.graphics", "javafx.fxml", "javafx.base", "javafx.web", "javafx.swing")
}

val mainClassName = "io.devpl.fxui.Launcher"
