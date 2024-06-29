plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api(Libs.mybatis)
    api(Libs.snakeyaml)
    api(Libs.jsoup)
    api(Libs.gson)

    api(Libs.springWeb)
    api(Libs.jacksonDatabind)
    api(Libs.jacksonAnnotations)
    api(Libs.jacksonCore)
    api(Libs.dom4j)

    api(Libs.lombok)
    annotationProcessor(Libs.lombok)

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
