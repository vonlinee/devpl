plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api(Libs.MYBATIS)
    api(Libs.SNAKE_YAML)
    api(Libs.jsoup)
    api(Libs.GSON)

    api(Libs.SPRING_WEB)
    api(Libs.jacksonDatabind)
    api(Libs.jacksonAnnotations)
    api(Libs.jacksonCore)
    api(Libs.dom4j) {
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
