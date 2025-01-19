plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.github.zeroaicy.aide.resource"

    val currentProject = project.layout.projectDirectory.asFile

    sourceSets{
        val main by getting
        main.apply {
            res.setSrcDirs(listOf(currentProject.resolve("res")))
            manifest.srcFile(currentProject.resolve("AndroidManifest.xml"))
        }
    }

}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}