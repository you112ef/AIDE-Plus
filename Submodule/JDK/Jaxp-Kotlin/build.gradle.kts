import gradleExt.Versions

@Suppress("JavaPluginLanguageLevel")
plugins {
    id("java-library")
}

java {
    sourceCompatibility = Versions.JavaVersion
    targetCompatibility = Versions.JavaVersion
}
