plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api(Libs.JETBRAINS_ANNOTATION)
    api(Libs.JODA_TYPE_TOOLS)
    api(Libs.APACHE_COMMONS_IO)
    api(Libs.apacheCommonsCollections)
    api(Libs.apacheCommonsCollections4)
    api(Libs.ULID)
    api(Libs.JAVAX_JSON_API)
}

description = "devpl-sdk-internal"

java {
    withJavadocJar()
}
