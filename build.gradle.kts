// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.devtoolsKsp) apply false
    alias(libs.plugins.baseLineprofile) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.googleDagge) apply false
    alias(libs.plugins.kotlinComposeCompiler) apply false


//    id("com.gradleup.shadow") version "9.0.0-beta8"
    id("build-logic.project")
}

buildscript {
    dependencies {
        //BlackObfuscator(https://github.com/CodingGay/BlackObfuscator-ASPlugin)
        classpath(libs.io.github.blackObfuscator.asPlugin)

    }
}


val moduleName = mutableListOf(
    "Submodule/AIDE/app_rewrite",
    "Submodule/AIDE/appAideBase",
    "Submodule/Termux/app",
    "Submodule/Termux/terminal-emulator",
    "Submodule/Termux/terminal-view",
    "Submodule/Termux/termux-shared"
)

tasks.register<Delete>("clean").configure {
    delete(rootProject.layout.buildDirectory)
    delete(rootDir.resolve("AIDELibrary"))
    moduleName.forEach {
        delete(rootDir.resolve(it).resolve("res"))
        delete(rootDir.resolve(it).resolve("AndroidManifest.xml"))
    }
}
