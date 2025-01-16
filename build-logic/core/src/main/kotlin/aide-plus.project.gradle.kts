import gradleExt.Versions
import gradleExt.copyDirectory

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val namespaceHashMap = mutableMapOf(
    "app_rewrite" to "com.aide.ui.rewrite",
    "appAideBase" to "com.aide.ui.base"
)

val replaceXmlText = mutableMapOf(
    "<attr name=\"fontProviderFetchStrategy\">\n" +
            "        <enum name=\"async\" value=\"1\" />\n" +
            "        <enum name=\"blocking\" value=\"0\" />\n" +
            "    </attr>" to "",
    "<attr name=\"fontProviderFetchTimeout\" format=\"integer\">\n" +
            "        <enum name=\"forever\" value=\"-1\" />\n" +
            "    </attr>" to "",
    "<attr name=\"fontStyle\">\n" +
            "        <enum name=\"italic\" value=\"1\" />\n" +
            "        <enum name=\"normal\" value=\"0\" />\n" +
            "    </attr>" to "",
    "<color name=\"accent_material_light\">#ff29b6f6</color>" to "",
    "<color name=\"primary_dark_material_light\">#ff29b6f6</color>" to "",
    "<color name=\"primary_material_light\">#ffffffff</color>" to "",
    "<color name=\"primary_dark_material2\">#ff03a9f4</color>\n" +
            "    <color name=\"primary_dark_material_light2\">#ff29b6f6</color>\n" +
            "\t<color name=\"accent_material2\">#ff03a9f4</color>\n" +
            "    <color name=\"accent_material_light2\">#ff29b6f6</color>\n" +
            "\t<color name=\"primary_material2\">#ff222222</color>\n" +
            "    <color name=\"primary_material_light2\">#ffffffff</color>" to "<color name=\"accent_material\">#ff03a9f4</color>\n" +
            "    <color name=\"accent_material_light\">#ff29b6f6</color>\n" +
            "    <color name=\"popup_background\">#ff111111</color>\n" +
            "    <color name=\"popup_background_light\">#ffffffff</color>\n" +
            "    <color name=\"popup_border\">#ffbbbbbb</color>\n" +
            "    <color name=\"popup_border_light\">#ffbbbbbb</color>\n" +
            "    <color name=\"primary_dark_material\">#ff191919</color>\n" +
            "    <color name=\"primary_dark_material_light\">#ffe0e0e0</color>\n" +
            "    <color name=\"primary_material\">#ff222222</color>\n" +
            "    <color name=\"primary_material_light\">#ffffffff</color>\n" +
            "    <color name=\"primary_text_default_material_dark\">#ffffffff</color>\n"
)

val replaceTxt = mutableListOf(
    """ <attr name="fontStyle">
        <enum name="italic" value="1" />
        <enum name="normal" value="0" />
    </attr>""",
    """<attr name="fontProviderFetchStrategy">
        <enum name="async" value="1" />
        <enum name="blocking" value="0" />
    </attr>""",
    """<attr name="fontProviderFetchTimeout" format="integer">
        <enum name="forever" value="-1" />
    </attr>"""
)

android {

    val currentProject = project.layout.projectDirectory.asFile
    val currentProjectName = currentProject.name
    val rootProjectDir = File(currentProject.parentFile, "AIDE-Plus")
    namespace = namespaceHashMap[currentProjectName]
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
    }

    sourceSets {
        val main by getting
        main.apply {
            try {
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
                    if (currentProject.absolutePath.endsWith("appAideBase")) {
                        val valueAttrXml = rootDir.resolve("Submodule/AIDE/attrs.xml")
                        currentProject.resolve("res/values/attrs.xml").apply {
                            valueAttrXml.copyTo(this,true)
                        }
                    } else if (currentProject.absolutePath.endsWith("app_rewrite")) {
                        val valueColorXml = rootDir.resolve("Submodule/AIDE/colors.xml")
                        currentProject.resolve("res/values-v21/colors.xml").apply {
                            valueColorXml.copyTo(this,true)
                        }
                        currentProject.resolve("res/values-v21/colors_ui.xml")
                            .apply { if (exists()) delete() }
                    }
                    res.setSrcDirs(listOf(newResDir))
                }

                val jniLibsSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/jniLibs")
                if (jniLibsSrcDir.exists()) {
                    jniLibs.setSrcDirs(listOf(jniLibsSrcDir))
                }

                val assetsSrcDir = rootProjectDir.resolve("${currentProjectName}/src/main/assets")
                if (assetsSrcDir.exists()) {
                    assets.setSrcDirs(listOf(assetsSrcDir))
                }

                val resourcesSrcDir =
                    rootProjectDir.resolve("${currentProjectName}/src/main/resources")
                if (resourcesSrcDir.exists()) {
                    resources.setSrcDirs(listOf(resourcesSrcDir))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
