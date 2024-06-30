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

    implementation("io.github.palexdev:materialfx:11.17.0")
    implementation("com.github.hervegirod:fxsvgimage:1.1")
    implementation(Libs.apacheCommonsCli)

    implementation("org.fxmisc.richtext:richtextfx:0.11.2")
    implementation(Libs.jetbrainsAnnotation)
    implementation(Libs.googleJavaFormat)
    implementation(Libs.mybatis)
    implementation(Libs.pagehelper)
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0")
    implementation(Libs.mysqlConnectorJava)
    implementation(Libs.gson)
    implementation(Libs.poi)
    implementation(Libs.poiOoxml)
    implementation("cn.afterturn:easypoi-base:4.4.0")
    implementation("cn.afterturn:easypoi-annotation:4.4.0")
    implementation(Libs.dom4j)
    implementation("com.sun.jna:jna:3.0.9")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.3.1")
    implementation("javax.annotation:jsr250-api:1.0")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("com.jcraft:jsch:0.1.54")
    implementation(Libs.apacheCommonsIO)
    implementation(Libs.snakeyaml)
    implementation(Libs.springJdbc)
    implementation(Libs.dbutils)
    implementation(Libs.st4)
    implementation(Libs.javapoet)
    implementation(Libs.mybatisGenerator)
    implementation(Libs.mybatisPlusAnnotation)
    implementation(Libs.mybatisPlusCore)
    implementation(Libs.mybatisPlusExtension)

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
