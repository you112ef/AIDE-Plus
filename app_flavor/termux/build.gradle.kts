@file:Suppress("DEPRECATION")

import gradleExt.Versions
import gradleExt.makeApk
import org.jetbrains.kotlin.cli.common.CompilerSystemProperties

plugins {
    id("app-flavor.project")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.baseLineprofile)
    //alias(libs.plugins.googleDagge)
}

val allVariants = mutableMapOf<String, File>()
var zipalignPath = ""



android {
    namespace = "io.github.zeroaicy.aide2"

    defaultConfig {
        applicationId = "io.github.zeroaicy.aide2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }


    sourceSets {
        val main by getting
        val aideLibraryDir = project.rootDir.resolve("AIDELibrary")

        main.apply {
            val jniLibsSrcDir = aideLibraryDir.resolve("jniLibs")
            if (jniLibsSrcDir.exists()) {
                jniLibs.setSrcDirs(listOf(jniLibsSrcDir))
            }
            val assetsSrcDir = aideLibraryDir.resolve("assets")
            if (assetsSrcDir.exists()) {
                assets.setSrcDirs(listOf(assetsSrcDir))
            }
        }

    }

    packaging {
        resources {
            pickFirsts += "*/*/*/*/*/*/*/*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*/*/*/*//*/*"
            pickFirsts += "*/*/*/*/*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*/*"
            pickFirsts += "*/*/*/*/*"
            pickFirsts += "*/*/*/*"
            pickFirsts += "*/*/*"
            pickFirsts += "*/*"
            pickFirsts += "*"
        }
        jniLibs {
            useLegacyPackaging = true
            pickFirsts += "/lib/*/*"
        }
    }

    androidResources {

        val publicXmlFile =
            rootDir.resolve("Submodule/AIDE/AIDE-Plus/appAideBase/src/main/res/values/public.xml")
        val publicTxtFile = rootDir.resolve("ids-default.txt")

        if (publicXmlFile.exists()) {
            // 创建父目录并确保 publicTxtFile 存在
            publicTxtFile.parentFile?.mkdirs()
            publicTxtFile.delete()
            publicTxtFile.createNewFile()

            // 解析 public.xml 文件并将内容写入 public.txt
            val nodes = javax.xml.parsers.DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(publicXmlFile)
                .documentElement
                .getElementsByTagName("public")

            for (i in 0 until nodes.length) {
                val node = nodes.item(i)
                val type = node.attributes.getNamedItem("type").nodeValue
                val name = node.attributes.getNamedItem("name").nodeValue
                val id = node.attributes.getNamedItem("id").nodeValue
                publicTxtFile.appendText("${android.defaultConfig.applicationId}:$type/$name = $id\n")
            }

            // 添加稳定 ID 参数
            additionalParameters.addAll(mutableListOf("--stable-ids", publicTxtFile.path))
        } else {
            println("public.xml不存在")
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    android.applicationVariants.all {
        val sdkDirectory = android.sdkDirectory
        val buildToolsVersion = android.buildToolsVersion
        zipalignPath =
            "$sdkDirectory/build-tools/$buildToolsVersion/zipalign${if (isWindows) ".exe" else ""}"

        outputs.all {
            allVariants[name.replace("-", "")] = outputFile
        }
    }
}



dependencies {

    // termux
    api(project(":Submodule:Termux:app"))
    api(project(":Submodule:Termux:terminal-emulator"))
    api(project(":Submodule:Termux:terminal-view"))
    api(project(":Submodule:Termux:termux-shared"))

    api(project(":Submodule:AIDE:app_rewrite"))
    api(project(":Submodule:AIDE:appAideBase"))

    api(project(":Submodule:Resource"))

    // androidx&material和其他杂七杂八的框架
    api(libs.bundles.google.androidx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    coreLibraryDesugaring(libs.android.tools.desugar.jdk.libs)


    val resourcesJar = project.rootDir.resolve("AIDELibrary/resources.jar")
    api(files(resourcesJar))


}

configurations.all {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
    exclude(group = "net.java.dev.jna", module = "jna-platform")
    exclude(group = "net.java.dev.jna", module = "jna")
    exclude(group = "javax.inject", module = "javax.inject")
    exclude(group = "org.jetbrains", module = "annotations")
    exclude(group = "com.google.guava", module = "guava")
    exclude(group = "com.google.guava", module = "listenablefuture")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
}




afterEvaluate {
    tasks.register("makeApk") {
        doLast {
            allVariants.forEach { (_, apkFile) ->
                if (apkFile.exists()) {
                    println("APK 文件路径: ${apkFile.absolutePath}")
                    val appFlavor =
                        rootDir.resolve("Submodule/AIDE/AIDE-Plus/app_flavor/build.gradle")
                    makeApk(
                        aideLibraryDir = rootDir.resolve("AIDELibrary"),
                        zipalignFile = File(zipalignPath),
                        apkFile = apkFile,
                        apkOutputFile = File(
                            apkFile.parentFile,
                            "AIDE-Plus-${Versions.versionName(appFlavor)}.apk"
                        ),
                        storeFile = file("../debug.jks"),
                        storePassword = "123789456",
                        keyAlias = "androiddebug",
                        keyPassword = "123789456",
                        enableV1Signing = true,
                        enableV2Signing = true,
                        enableV3Signing = true,
                    )
                }

            }
        }
    }


    tasks.configureEach {
        if (name.startsWith("package")) {
            finalizedBy("makeApk")
        }
    }
}

val isWindows: Boolean
    get() = CompilerSystemProperties.OS_NAME.value!!.lowercase().startsWith("windows")
