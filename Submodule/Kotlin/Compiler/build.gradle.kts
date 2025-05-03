plugins {
    //alias(libs.plugins.android.library)
    //alias(libs.plugins.kotlin.android)

    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"


}


java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


//
//tasks {
//    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
//        exclude("META-INF/maven/org.jetbrains/*/pom.xml")
//        exclude("META-INF/maven/org.jetbrains/*/pom.properties")
//
//        exclude("com/google/common/**")
//        exclude("org/jdom/**")
//        exclude("kotlin/**")
//        exclude("org/jetbrains/annotations/Nls**")
//        exclude("android/**")
//        exclude("dalvik/**")
//        exclude("org/lsposed/hiddenapibypass/**")
//
//        dependencies {
//            exclude(dependency("org.jetbrains.kotlin:kotlin-reflect:1.6.10"))
//            exclude(dependency("org.jetbrains.intellij.deps:trove4j:1.0.20200330"))
//            exclude(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4"))
//            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.0"))
//            exclude(dependency("org.jetbrains.kotlin:kotlin-script-runtime:2.1.0"))
//            exclude(dependency("io.github.itsaky:nb-javac-android:17.0.0.3"))
//            exclude(dependency("org.jetbrains:annotations:26.0.1"))
//        }
//
//        archiveFileName.set("kotlinc.jar")
//    }
//
//    assemble {
//        dependsOn(shadowJar)
//    }
//    build {
//        dependsOn(shadowJar)
//    }
//}



dependencies {

    // Kotlin编译器必须的依赖
    // https://mvnrepository.com/artifact/org.jetbrains.intellij.deps/trove4j
    api(libs.org.jetbrains.intellij.deps.trove4j)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    api(libs.org.jetbrains.kotlin.reflect)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-script-runtime
    api(libs.org.jetbrains.kotlin.script.runtime)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm
    api(libs.org.jetbrains.kotlinx.coroutines.core.jvm)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib
    api(libs.org.jetbrains.kotlin.stdlib)


    api(libs.com.google.guava)


    api(libs.io.github.itsaky.nb.javac.android)
    api(libs.org.jetbrains.annotations)
    //implementation(libs.org.jetbrains.kotlin.compiler)



//    implementation(libs.io.github.lsposed.hiddenapibypass) /// 必须




    api(files("libs/kotlin-compiler-2.1.10.jar"))
    api(project(":Submodule:Build-deps:Jaxp-Xml"))



}