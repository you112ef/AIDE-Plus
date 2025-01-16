plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("aide-plus.project")
}


dependencies {
    api(project(":Submodule:AIDE:appAideBase"))

    api(project(":Submodule:Compiletion:Xml:aaptcompiler"))
    api(project(":Submodule:Eclipse:JavaFormatter"))

    api(project(":Submodule:Kotlin:Compiler"))
    api(project(":Submodule:Kotlin:Formatter"))

    api(libs.io.github.itsaky.nb.javac.android)


    // appAideBase 项目的依赖
    api(libs.bundles.rikkax.shizuku)

    api(libs.androidx.browser)

    api(libs.bundles.google.androidx)

    api(libs.androidx.legacy.support.v4)

    api(libs.androidx.multidex)



    // app_rewrite的依赖
    compileOnly(libs.org.eclipse.jdt.ecj)
    // jgit 7.0.0.202409031743-r
    compileOnly(libs.org.eclipse.jgit)

    api(libs.bundles.google.androidx)

    // 颜色选择控件
    api(libs.net.margaritov.preference.colorpickercolorpickerpreference)

    api(libs.org.jetbrains.annotations)

    // java-formatter依赖库 后期与java-formatter合并
    api(libs.org.eclipse.platform.text)
    compileOnly(libs.org.osgi.framework)

    // 依赖解析库
    api(libs.org.apache.maven.model)
    api(libs.org.codehaus.plexus.utils)


    // BC加密库
    api(libs.org.bouncycastle.bcprov.jdk18on)
    // apk签名
    api(libs.com.android.tools.build.apksig)



    val aideLibraryDir = project.rootDir.resolve("AIDELibrary")


    aideLibraryDir.resolve("libs").listFiles()?.forEach {
        if (it.name.endsWith(".jar")) {
            api(files(it))
        }
    }

    aideLibraryDir.resolve("provider").listFiles()?.forEach {
        if (it.name.endsWith(".jar")) {
            compileOnly(files(it))
        }
    }


}
