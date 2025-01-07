package gradleExt

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/5
 */


fun Project.launchApp(
    adbExecutablePath: String,
    packageName: String,
    activityName: String
) {
    tasks.register("launchApp") {
        doLast {
            // 执行adb命令
            exec {
                commandLine(
                    adbExecutablePath,
                    "shell",
                    "am",
                    "start",
                    "-n",
                    "$packageName/$activityName"
                )
            }
        }
    }
}

fun Project.configureBaseAppExtension() {
    extensions.findByType(BaseAppModuleExtension::class)?.run {

        compileSdk = Versions.compileSdk
        ndkVersion = Versions.ndkVersion

        defaultConfig{
            minSdk = Versions.minSdk
            targetSdk = Versions.targetSdk
            versionCode = Versions.versionCode
            versionName = Versions.versionName
        }

        signingConfigs {
            create("release") {
                keyAlias = "AIDE+"
                keyPassword = "123789456"
                storePassword = "123789456"
                storeFile = file("release.jks")
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
            create("debug1") {
                keyAlias = "androiddebug"
                keyPassword = "123789456"
                storePassword = "123789456"
                storeFile = file("debug.jks")
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
        }
        buildTypes {
            release {
                isMinifyEnabled = false
                isShrinkResources = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                signingConfig = signingConfigs.getByName("debug1")
            }
            debug {
                isMinifyEnabled = false
                isShrinkResources = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                signingConfig = signingConfigs.getByName("debug1")
            }
        }

        compileOptions {
            sourceCompatibility = Versions.JavaVersion
            targetCompatibility = Versions.JavaVersion
        }

        buildFeatures {
            buildConfig = true
            viewBinding = true
            renderScript = false
        }

        lint {
            baseline = file("lint-baseline.xml")
            abortOnError = false
            checkReleaseBuilds = false
        }

        packaging {
            resources {
                /// 重复替换
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
    }
}

@Suppress("DEPRECATION")
fun Project.configureBaseExtension() {
    extensions.findByType(BaseExtension::class)?.run {

        compileSdkVersion(Versions.compileSdk)
        buildToolsVersion = Versions.buildToolsVersion
        ndkVersion = Versions.ndkVersion

        defaultConfig {
            minSdk = Versions.minSdk
            targetSdk = Versions.targetSdk
            versionCode = Versions.versionCode
            versionName = Versions.versionName
        }

        compileOptions {
            sourceCompatibility = Versions.JavaVersion
            targetCompatibility = Versions.JavaVersion
        }

        lintOptions {
            baseline(file("lint-baseline.xml"))
            isAbortOnError = false
            isCheckReleaseBuilds = false
        }

        buildFeatures.apply {
            viewBinding = true
            dataBinding.enable = true
            buildConfig = true
        }


        packagingOptions {
            resources {
                /// 重复替换
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
    }
}


fun Project.configureKotlinExtension() {
    extensions.findByType(KotlinAndroidProjectExtension::class)?.run {
        jvmToolchain(21)
    }
}
