import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.jvm")
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

description = "Twirp service generator PBandK plugin for use in Kotlin Multiplatform Mobile projects."

dependencies {
    compileOnly(libs.pbandk.runtime)
    compileOnly(libs.pbandk.protoc.gen)

    // Running the test requires `pbandk.gen.ServiceGenerator`, but the library
    // itself only needs it for compile-time. See README.md#Usage for details.
    testImplementation(libs.pbandk.protoc.gen)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}

// This module has no Java sources, so a `java { }` compatibility block would be a no-op; the
// Kotlin `jvmTarget` below is what emits the Java 8 bytecode for the published protoc-plugin jar.
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
            ),
        )
    }
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())

        configureTwirpKmpPOM(project.description!!)
    }
}
