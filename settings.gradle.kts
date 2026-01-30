pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.+"
}

stonecutter {
    create(rootProject) {
        versions("1.21.1")
        vcsVersion = "1.21.1"
    }
}

rootProject.name = "Vinyls"