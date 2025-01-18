plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}


android {
    namespace = "org.jetbrains.kotlin"
}


dependencies {
    implementation(libs.org.jetbrains.kotlin.compiler){
        isTransitive = false
    }

    implementation(libs.io.github.lsposed.hiddenapibypass)

    implementation(libs.org.jetbrains.kotlin.reflect)
    implementation(libs.io.github.itsaky.nb.javac.android)
    implementation(libs.org.jetbrains.intellij.deps.trove4j)
    implementation(libs.org.jdom)


    implementation(libs.org.jetbrains.annotations)


    api(project(":Submodule:JDK:Jaxp-Kotlin"))

    compileOnly(project(":Submodule:Kotlin:Core"))

}