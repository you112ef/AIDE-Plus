import gradleExt.Versions

@Suppress("JavaPluginLanguageLevel")
plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = Versions.JavaVersion
    targetCompatibility = Versions.JavaVersion
}

sourceSets{
    val main by getting
    main.java.srcDir("java")
}

dependencies {
    compileOnlyApi(files("android-35.jar"))
}


tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:none")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}