import gradleExt.Versions

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val namespaceHashMap = mutableMapOf(
    "app_rewrite" to "com.aide.ui.rewrite",
    "appAideBase" to "com.aide.ui.base"
)

android {

    val currentProject = project.layout.projectDirectory.asFile
    val currentProjectName = currentProject.name
    val rootProjectDir = File(currentProject.parentFile, "AIDE-Plus")
    println("AIDE-Plus 的项目路径: $rootProjectDir")
    namespace = namespaceHashMap[currentProjectName]
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
    }

    sourceSets {
        val main by getting
        main.apply {
            val manifestFile =
                rootProjectDir.resolve("${currentProjectName}/src/main/AndroidManifest.xml")
            if (manifestFile.exists()) {
                val content = manifestFile.readText()
                val pattern = "package=\".*?\"".toRegex()
                val updatedContent = content.replace(pattern, "")
                manifestFile.writeText(updatedContent)
                manifest.srcFile(manifestFile)
            }
            val javaSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/java")
            if (javaSrcDir.exists()) {
                java.setSrcDirs(listOf(javaSrcDir))
            }
            val resSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/res")
            if (resSrcDir.exists()) {
                res.setSrcDirs(listOf(resSrcDir))
            }
            val jniLibsSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/jniLibs")
            if (jniLibsSrcDir.exists()) {
                jniLibs.setSrcDirs(listOf(jniLibsSrcDir))
            }
            val assetsSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/assets")
            if (assetsSrcDir.exists()) {
                assets.setSrcDirs(listOf(assetsSrcDir))
            }
        }
    }

}
