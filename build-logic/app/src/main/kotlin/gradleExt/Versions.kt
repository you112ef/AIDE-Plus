package gradleExt

import java.io.File

/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/5
 */

@SuppressWarnings("unused")
object Versions {
    // Project versions
    private const val version = "2.3.2.9"
    const val versionCode = 2008210017

    val versionName by lazy {
        "$version-${getCommitHash()}"
    }


    const val buildToolsVersion = "35.0.0"
    const val compileSdk = 35
    const val ndkVersion = "27.2.12479018"
    const val minSdk = 24
    const val targetSdk = 28

    val JavaVersion = org.gradle.api.JavaVersion.VERSION_21


    private fun getCommitHash(): String {
        return ProcessBuilder("git", "rev-parse", "--short", "HEAD")
            .directory(File("."))
            .redirectErrorStream(true)
            .start()
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
    }

    val gitCommitHash: String by lazy {
        @Suppress("DEPRECATION")
        Runtime.getRuntime()
            .exec("git rev-parse HEAD")
            .inputStream
            .bufferedReader()
            .use { it.readText().trim() }
    }
}

