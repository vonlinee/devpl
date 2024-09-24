plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api(Libs.MYBATIS)
    api(Libs.SNAKE_YAML)
    api(Libs.JSOUP)
    api(Libs.GSON)

    api(Libs.SPRING_WEB)
    api(Libs.JACKSON_DATABIND)
    api(Libs.JACKSON_ANNOTATIONS)
    api(Libs.JACKSON_CORE)
    api(Libs.DOM4J) {
        exclude("pull-parser", "pull-parser")
    }

    api(Libs.LOMBOK)
    annotationProcessor(Libs.LOMBOK)

    api(project(":devpl-codegen"))

//    implementation(fileTree("lib") {
//        exclude("*-source.jar", "*-javadoc.jar")
//
//        include("")
//    })

    api(files("${rootProject.projectDir}/libs/json5-java-2.0.0.jar"))
}

description = "devpl-commons"

java {
    withJavadocJar()
}
