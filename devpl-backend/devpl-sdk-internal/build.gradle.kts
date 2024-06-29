plugins {
    id("io.devpl.java-conventions")
}

dependencies {
    api("org.jetbrains:annotations:24.0.1")
    api("net.jodah:typetools:0.6.3")
    api("commons-io:commons-io:2.15.1")
    api("commons-collections:commons-collections:3.2.2")
    api("org.apache.commons:commons-collections4:4.4")
    api("com.github.f4b6a3:ulid-creator:5.2.3")
    api("javax.json:javax.json-api:1.1.4")
}

description = "devpl-sdk-internal"

java {
    withJavadocJar()
}
