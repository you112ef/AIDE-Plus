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
            val appFlavor = rootDir.resolve("Submodule/AIDE/AIDE-Plus/app_flavor/build.gradle")
            val versionTxt = rootDir.resolve("version.txt")
            versionTxt.writeText(Versions.version(appFlavor))


            minSdk = Versions.minSdk
            targetSdk = Versions.targetSdk
            versionCode = Versions.versionCode(appFlavor).toInt()
            versionName = Versions.versionName(appFlavor)
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
            }
            debug {
                isMinifyEnabled = false
                isShrinkResources = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
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
            val baselineFile = project.layout.projectDirectory.asFile.resolve("lint-baseline.xml")
            baseline = baselineFile
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
        }

        compileOptions {
            sourceCompatibility = Versions.JavaVersion
            targetCompatibility = Versions.JavaVersion
        }

        lintOptions {
            val baseline = project.layout.projectDirectory.asFile.resolve("lint-baseline.xml")
            baseline(baseline)
            isAbortOnError = false
            isCheckReleaseBuilds = false
        }

        buildFeatures.apply {
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
