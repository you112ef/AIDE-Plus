plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.github.zeroaicy.aide.test.kotlinc"

    defaultConfig {
        applicationId = "io.github.zeroaicy.aide.test.kotlinc"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }

    packaging {

    }
}

dependencies {



    api(project(":Submodule:Kotlin:Compiler"))



    api(project(":Submodule:Build-deps:Jaxp-Xml"))
    api(project(":Submodule:Build-deps:Jaxp-Kotlin"))






    api(libs.bundles.google.androidx)
    api(files("libs/MTDataFilesProvider.jar"))
//    api(files("libs/jaxp.jar"))


    api(libs.io.github.lsposed.hiddenapibypass)




    coreLibraryDesugaring(libs.android.tools.desugar.jdk.libs)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}


configurations.all {
    exclude(group = "net.java.dev.jna", module = "jna-platform")
    exclude(group = "net.java.dev.jna", module = "jna")
    exclude(group = "javax.inject", module = "javax.inject")
//    exclude("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
//    exclude("org.jetbrains.intellij.deps:trove4j:1.0.20200330")
//    exclude("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
//    exclude("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.0")
//    exclude("org.jetbrains.kotlin:kotlin-script-runtime:2.1.0")

}