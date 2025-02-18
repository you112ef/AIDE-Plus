plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}


android {
    namespace = "org.jetbrains.kotlin"
}


dependencies {

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-compiler
    //implementation(libs.org.jetbrains.kotlin.compiler)

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-compiler-embeddable
    //implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.1.10")

    // Kotlin编译器必须的依赖
    // https://mvnrepository.com/artifact/org.jetbrains.intellij.deps/trove4j
    implementation(libs.org.jetbrains.intellij.deps.trove4j)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    //noinspection GradleDependency,UseTomlInstead
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-script-runtime
    implementation(libs.org.jetbrains.kotlin.script.runtime)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm
    implementation(libs.org.jetbrains.kotlinx.coroutines.core.jvm)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib
    implementation(libs.org.jetbrains.kotlin.stdlib)






    implementation(libs.io.github.lsposed.hiddenapibypass)
    implementation(libs.io.github.itsaky.nb.javac.android)
    implementation(libs.org.jetbrains.annotations)
    implementation(libs.org.jdom)


/*
    api(files("libs/apiguardian-api-1.1.2.jar"))
    api(files("libs/kotlin-compiler-embeddable-2.1.10.jar"))
    api(files("libs/kotlin-daemon-embeddable-2.1.10.jar"))
    api(files("libs/opentest4j-1.3.0.jar"))*/

    //api(files("libs/kotlin-compiler-2.1.10.jar"))


    api(files("libs/kotlin-compiler-2.1.10.jar"))

    api(project(":Submodule:JDK:Jaxp-Kotlin"))

    compileOnly(project(":Submodule:Kotlin:Core"))

}