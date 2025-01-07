plugins {
    id("app-flavor.project")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.baseLineprofile)
    //alias(libs.plugins.googleDagge)
}

android {
    namespace = "io.github.zeroaicy.aide"

    defaultConfig {
        applicationId = "io.github.zeroaicy.aide"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    api(project(":Submodule:Termux:terminal-emulator"))
    api(project(":Submodule:Termux:terminal-view"))
    api(project(":Submodule:Termux:app"))
    api(project(":Submodule:Termux:termux-shared"))


    // androidx&material和其他杂七杂八的框架
    api(libs.bundles.google.androidx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    coreLibraryDesugaring(libs.android.tools.desugar.jdk.libs)
}