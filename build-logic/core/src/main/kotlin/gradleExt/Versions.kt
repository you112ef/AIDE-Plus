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
    private val versionPattern = """versionName\s*"([^"]+)"""".toRegex()
    private val versionCodePattern = """versionCode\s*(\d+)""".toRegex()


    val version: (File) -> String = {
        versionPattern.find(it.readText())?.groupValues?.get(1) ?: "2.3.3.3-alpha05"
    }
    val versionCode: (File) -> String = {
        versionCodePattern.find(it.readText())?.groupValues?.get(1) ?: "2008210017"
    }
    val versionName: (File) -> String = {
        "${version(it)}-${getCommitHash()}"
    }


    const val buildToolsVersion = "35.0.0"
    const val compileSdk = 35
    const val ndkVersion = "27.2.12479018"
    const val minSdk = 26
    const val targetSdk = 34

    val JavaVersion = org.gradle.api.JavaVersion.VERSION_21


    /**
     * 获取 git 提交的短的 hash 值
     * */
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

    /**
     * 获取 git 提交的 hash 值
     * */
    val gitCommitHash: String by lazy {
        @Suppress("DEPRECATION")
        Runtime.getRuntime()
            .exec("git rev-parse HEAD")
            .inputStream
            .bufferedReader()
            .use { it.readText().trim() }
    }
}

