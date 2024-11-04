plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts
    // in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

tasks.wrapper {
    gradleVersion = "8.4"
}

subprojects {

    tasks.compileJava {
        options.compilerArgs.add("'-Xlint:-deprecation'")
        options.compilerArgs.add("'-Xlint:-unchecked'")
    }

    tasks.javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}
