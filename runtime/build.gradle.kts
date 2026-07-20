import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

description = "Runtime for Twirp service generator PBandK plugin for use in Kotlin Multiplatform projects."

kotlin {
    explicitApi()
    jvmToolchain(24)

    // The Android target is configured through the Android-KMP library plugin's nested
    // `android {}` block instead of the com.android.library plugin + a top-level `android {}`
    // block + `androidTarget()`. This plugin publishes a single Android variant, so
    // `publishLibraryVariants("release")` is no longer needed.
    android {
        namespace = "com.collectiveidea.twirp"
        compileSdk = 36
        minSdk = 23

        // Opt in to JVM host unit tests so commonTest still runs on the Android/JVM host,
        // as `:twirp-kmp-runtime:testAndroidHostTest`.
        withHostTestBuilder {}
    }

    jvm()

    js {
        browser {}
        nodejs {}
    }

    // Native targets, according to https://kotlinlang.org/docs/native-target-support.html
    // Tier 1
    macosArm64()
    iosSimulatorArm64()
    iosArm64()
    // Tier 2
    linuxX64()
    linuxArm64()
    iosX64()
    // watchosSimulatorArm64()
    // watchosX64()
    // watchosArm32()
    // watchosArm64()
    tvosSimulatorArm64()
    tvosArm64()
    // Tier 3
    // androidNativeArm32()
    // androidNativeArm64()
    // androidNativeX86()
    // androidNativeX64()
    mingwX64()
    // watchosDeviceArm64()

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.framework {
                baseName = "TwirpKmp"
            }
        }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)

            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// Compile with the JDK 24 toolchain but emit Java 11 bytecode, so consumers only need a
// JDK 11+ toolchain to build against the published library. Java 8 is deprecated under AGP 9;
// 11 is the modern-conservative floor with no on-device impact (D8 desugars to minSdk).
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
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

tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}
