import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.compose") version "1.2.0"
}

group = "dev.bogwalk"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    testImplementation(kotlin("test"))
    testImplementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "minesweeper"
            packageVersion = "1.0.0"
        }
    }
}