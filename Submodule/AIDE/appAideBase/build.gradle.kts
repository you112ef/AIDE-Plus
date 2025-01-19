plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("aide-plus.project")
}


dependencies {


    api(libs.bundles.rikkax.shizuku)

    api(libs.androidx.browser)

    api(libs.bundles.google.androidx)

    api(libs.androidx.legacy.support.v4)

    api(libs.androidx.multidex)



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