plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}


android {
    namespace = "io.github.zeroaicy.aide.kotlinc"
}


java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}


dependencies {
    implementation(libs.org.jetbrains.kotlin.compiler){
        exclude("org.jetbrains","annotations")
        exclude("com.google.guava","guava")
        exclude("com.google.guava","listenablefuture")
        exclude("org.jetbrains.kotlin","kotlin-compiler-embeddable")
        exclude("net.java.dev.jna", "jna")
        exclude("net.java.dev.jna", "jna-platform")
        exclude("javax.inject","javax.inject")
    }
    implementation(libs.io.github.itsaky.nb.javac.android)
    implementation(libs.org.jetbrains.annotations)

    implementation(libs.io.github.lsposed.hiddenapibypass)
    implementation(libs.org.jetbrains.kotlin.reflect)
    implementation(libs.org.jetbrains.intellij.deps.trove4j)
    implementation(libs.org.jdom)

    api(project(":Submodule:Kotlin:Core"))

}