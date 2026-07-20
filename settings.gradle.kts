pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    // Auto-provisions a matching JDK for the Kotlin toolchain (jvmToolchain(24)) when one
    // isn't installed locally, so the toolchain resolves without a manual JDK install.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "twirp-kmp"

include(":generator")
project(":generator").name = "twirp-kmp-generator"

include(":runtime")
project(":runtime").name = "twirp-kmp-runtime"
