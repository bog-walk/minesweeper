plugins {
    kotlin("multiplatform") apply false
    id("org.jetbrains.compose") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
}

group = "dev.bogwalk"
version = "1.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}