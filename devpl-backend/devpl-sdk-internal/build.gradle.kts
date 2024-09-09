plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api(Libs.JETBRAINS_ANNOTATION)
    api(Libs.JODA_TYPE_TOOLS)
    api(Libs.APACHE_COMMONS_IO)
    api(Libs.APACHE_COMMONS_COLLECTIONS)
    api(Libs.APACHE_COMMONS_COLLECTIONS4)
    api(Libs.ULID)
    api(Libs.JAVAX_JSON_API)

    testImplementation(Libs.JUPITER)
    testImplementation(Libs.JUPITER_API)
}

description = "devpl-sdk-internal"

java {
    withJavadocJar()
}
