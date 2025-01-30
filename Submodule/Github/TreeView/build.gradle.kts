plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.github.dingyi222666.view.treeview"

    sourceSets {
        val main by getting
        main.apply {
            kotlin.setSrcDirs(listOf("kotlin"))
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