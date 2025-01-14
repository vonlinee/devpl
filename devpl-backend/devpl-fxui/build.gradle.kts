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
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0")
    implementation("org.fxmisc.richtext:richtextfx:0.11.2")

    implementation(Libs.APACHE_COMMONS_CLI)
    implementation(Libs.JETBRAINS_ANNOTATION)
    implementation(Libs.GOOGLE_JAVA_FORMAT)
    implementation(Libs.MYBATIS)
    implementation(Libs.PAGE_HELPER)
    implementation(Libs.MYSQL_CONNECTOR_JAVA)
    implementation(Libs.GSON)
    implementation(Libs.POI)
    implementation(Libs.POI_OOXML)
    implementation(Libs.EASY_POI_BASE)
    implementation(Libs.EASY_POI_ANNOTATION)
    implementation(Libs.DOM4J)
    implementation(Libs.JNA)
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.3.1")
    implementation(Libs.JSR250)
    implementation(Libs.JAKARTA_INJECT_API)
    implementation(Libs.JSCH)
    implementation(Libs.APACHE_COMMONS_IO)
    implementation(Libs.SNAKE_YAML)
    implementation(Libs.SPRING_JDBC)
    implementation(Libs.COMMONS_DBUTILS)
    implementation(Libs.ANTLR_ST4)
    implementation(Libs.JAVAPOET)
    implementation(Libs.MYBATIS_GENERATOR)
    implementation(Libs.MYBATIS_PLUS_ANNOTATION)
    implementation(Libs.MYBATIS_PLUS_CORE)
    implementation(Libs.MYBATIS_PLUS_EXTENSION)

    implementation(project(":devpl-sdk-internal"))
    implementation(project(":devpl-commons"))
    implementation(project(":devpl-codegen"))
    implementation(project(":devpl-codegen"))

    annotationProcessor(Libs.LOMBOK)
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
