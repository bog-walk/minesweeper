pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        val kotlinVersion = extra["kotlinVersion"] as String
        val composeVersion = extra["composeVersion"] as String

        kotlin("multiplatform").version(kotlinVersion)
        id("org.jetbrains.compose").version(composeVersion)
        id("org.jetbrains.kotlin.plugin.compose").version(kotlinVersion)
    }
}

rootProject.name = "minesweeper"
include(":common")
include(":desktop")
