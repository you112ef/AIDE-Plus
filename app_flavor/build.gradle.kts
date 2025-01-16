@file:Suppress("DEPRECATION")

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
    namespace = "io.github.zeroaicy.aide1"

    defaultConfig {
        applicationId = "io.github.zeroaicy.aide"
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


    flavorDimensions.add("api")
    productFlavors {
        create("default") {
            dimension = "api"
            versionNameSuffix = ""
            isDefault = true
            signingConfig = signingConfigs.getByName("debug1")
        }

        create("termux") {
            dimension = "api"
            versionNameSuffix = "-termux"
            applicationId = "io.github.zeroaicy.aide2"
            dependencies {
                api(project(":Submodule:Termux:app"))
                api(project(":Submodule:Termux:terminal-emulator"))
                api(project(":Submodule:Termux:terminal-view"))
                api(project(":Submodule:Termux:termux-shared"))
            }
            signingConfig = signingConfigs.getByName("debug1")
        }
    }

    androidResources {

        val publicXmlFile =
            project.rootProject.file("${project(":Submodule:AIDE:appAideBase").projectDir.path}/res/values/public.xml")
        val publicTxtFile =
            project.rootProject.file("${layout.buildDirectory.asFile.get().path}/public.txt")

        if (publicXmlFile.exists()) {
            // 创建父目录并确保 publicTxtFile 存在
            publicTxtFile.parentFile?.mkdirs()
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
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }



    applicationVariants.all {
        val sdkDirectory = android.sdkDirectory
        val buildToolsVersion = android.buildToolsVersion
        zipalignPath =
            "$sdkDirectory/build-tools/$buildToolsVersion/zipalign${if (isWindows) ".exe" else ""}"


        outputs.all {
            allVariants[name.replace("-", "")] = outputFile
        }
    }

}

configurations.all {
    exclude("org.jetbrains","annotations")
    exclude("com.google.guava","guava")
    exclude("com.google.guava","listenablefuture")
    exclude("org.jetbrains.kotlin","kotlin-compiler-embeddable")
    exclude("net.java.dev.jna", "jna")
    exclude("net.java.dev.jna", "jna-platform")
    exclude("javax.inject","javax.inject")
    //exclude("org.jetbrains.kotlin","kotlin-reflect")
    //exclude("org.jetbrains.intellij.deps","trove4j")
    //exclude("org.jetbrains.kotlinx","kotlinx-coroutines-core-jvm")
    //exclude("org.jetbrains.kotlin","kotlin-stdlib-jdk8")
    //exclude("org.jetbrains.kotlin","kotlin-script-runtime")
    //exclude("io.github.itsaky","nb-javac-android")
    resolutionStrategy {
        // 对冲突的依赖直接使用最新版本
        //force("")
        //failOnVersionConflict()
    }
}



dependencies {

    api(project(":Submodule:AIDE:app_rewrite"))
    api(project(":Submodule:AIDE:appAideBase"))


    // androidx&material和其他杂七杂八的框架
    api(libs.bundles.google.androidx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    coreLibraryDesugaring(libs.android.tools.desugar.jdk.libs)


    val resourcesJar = project.rootDir.resolve("AIDELibrary/resources.jar")
    api(files(resourcesJar))


}

afterEvaluate {
    tasks.register("makeApk") {
        doLast {
            allVariants.forEach { (_, apkFile) ->
                println("APK 文件路径: ${apkFile.absolutePath}")
                if (apkFile.exists()) {
                    makeApk(
                        aideLibraryDir = rootDir.resolve("AIDELibrary"),
                        zipalignFile = File(zipalignPath),
                        apkFile = apkFile,
                        storeFile = file("debug.jks"),
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