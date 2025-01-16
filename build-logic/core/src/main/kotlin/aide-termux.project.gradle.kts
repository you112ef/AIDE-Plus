import gradleExt.Versions

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val namespaceHashMap = mutableMapOf(
    "app" to "com.termux",
    "terminal-emulator" to "com.termux.emulator",
    "terminal-view" to "com.termux.view",
    "termux-shared" to "com.termux.shared"
)

android {

    val currentProject = project.layout.projectDirectory.asFile
    val currentProjectName = currentProject.name
    val rootProjectDir = File(currentProject.parentFile, "AIDE-Termux")
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
                // 替换掉原本的包名防止合并的时候出现异常
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
            val resourcesSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/resources")
            if (resourcesSrcDir.exists()) {
                resources.setSrcDirs(listOf(resourcesSrcDir))
            }
        }
    }

}

