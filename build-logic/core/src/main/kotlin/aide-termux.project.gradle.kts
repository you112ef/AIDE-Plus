import gradleExt.Versions
import gradleExt.copyDirectory

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
            val newManifestFile =
                currentProject.resolve("AndroidManifest.xml")
            // 复制和替换 AndroidManifest
            if (newManifestFile.exists()) {
                newManifestFile.delete()
            }
            if (manifestFile.exists()) {
                manifestFile.copyTo(newManifestFile)
                // 替换掉原本的包名防止合并的时候出现异常
                val content = newManifestFile.readText()
                val pattern = "package=\".*?\"".toRegex()
                val updatedContent = content.replace(pattern, "")
                newManifestFile.writeText(updatedContent)
                manifest.srcFile(newManifestFile)
            }
            val javaSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/java")
            if (javaSrcDir.exists()) {
                java.setSrcDirs(listOf(javaSrcDir))
            }
            val resSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/res")
            val newResDir = currentProject.resolve("res")
            if (newResDir.exists()) {
                newResDir.deleteRecursively()
            }
            if (resSrcDir.exists()) {
                // 复制res到新目录
                copyDirectory(
                    resSrcDir,
                    newResDir,
                    { _ -> }
                )
                res.setSrcDirs(listOf(newResDir))
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

