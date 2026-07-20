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

// The published protoc-plugin jar targets Java 8 bytecode (see #15). The Kotlin `jvmTarget` sets
// the bytecode level; `java.targetCompatibility` must match it -- even though this module has no
// Java sources -- or Kotlin's JVM-target validation fails (compileJava defaults to the build JDK,
// which then disagrees with compileKotlin).
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

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
