@file:Suppress("UnstableApiUsage")



enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven("https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        // maven("https://api.xposed.info/")//xposed仓库
        maven("https://maven.aliyun.com/repository/public")
        maven("https://repo1.maven.org/maven2/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://jcenter.bintray.com/")
        maven("https://dl.bintray.com/ppartisan/maven/")
        maven("https://clojars.org/repo/")
        maven("https://plugins.gradle.org/m2/")
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://repo.eclipse.org/content/groups/releases/")
        maven("https://storage.googleapis.com/r8-releases/raw")
        maven {
            url = uri("http://4thline.org/m2/")
            isAllowInsecureProtocol = true
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven("https://jitpack.io")
        mavenCentral()
        mavenLocal()
        // maven("https://api.xposed.info/")//xposed仓库
        maven("https://maven.aliyun.com/repository/public")
        maven("https://repo1.maven.org/maven2")
        maven("https://dl.bintray.com/ppartisan/maven")
        maven("https://clojars.org/repo")
        // 快照仓库
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://www.jetbrains.com/intellij-repository/releases")

    }
}

includeBuild("build-logic")


rootProject.name = "AIDE-Plus"


// 多渠道
include(
    ":app_flavor:default",
    ":app_flavor:termux"
)
// 测试包
include(
    ":Submodule:TestProject:XmlPackge"
)

// 底包
include(
    ":Submodule:AIDE:app_rewrite",
    ":Submodule:AIDE:appAideBase"
)
// 资源
include(
    ":Submodule:Resource"
)
// Github
include(
    ":Submodule:Github:Richpath",
    ":Submodule:Github:TreeView",
    ":Submodule:Github:HideApi"
)


// xml
include(
    ":Submodule:Compiletion:Xml:aaptcompiler"
)

// ecj
include(
    ":Submodule:Eclipse:JavaFormatter"
)


// jaxp
include(
    ":Submodule:JDK:Jaxp-Xml",
    ":Submodule:JDK:Jaxp-Kotlin"
)

// kotlin
include(
    ":Submodule:Kotlin:Compiler",
    ":Submodule:Kotlin:Formatter",
    ":Submodule:Kotlin:Core"
)

// termux
include(
    ":Submodule:Termux:terminal-emulator",
    ":Submodule:Termux:terminal-view",
    ":Submodule:Termux:app",
    ":Submodule:Termux:termux-shared"
)

