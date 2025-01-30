plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.richpath"

    sourceSets {
        val main by getting
        main.apply {
            java.setSrcDirs(listOf("java"))
            res.setSrcDirs(listOf("res"))

        }
    }
}

dependencies {


    api(libs.bundles.google.androidx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}