plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.github.zeroaicy.aide.aaptcompiler"

    defaultConfig {
        applicationId = "io.github.zeroaicy.aide.aaptcompiler"
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
    packaging {
        resources {
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
            //excludes += "kotlin/*/*.kotlin_builtins"
            excludes += "*/*/*.kotlin_builtins"
            excludes += "*/*.kotlin_builtins"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {


    api(project(":Submodule:Compiletion:Xml:aaptcompiler"))

    api(libs.bundles.google.androidx)
    api(files("libs/MTDataFilesProvider.jar"))



    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}

configurations.all {
    //exclude("com.google.guava","listenablefuture")
    //exclude("com.google.guava","guava")


    exclude("net.java.dev.jna", "jna")
    exclude("net.java.dev.jna", "jna-platform")
}

