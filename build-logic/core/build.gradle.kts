plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}


dependencies {
    implementation("org.bouncycastle:bcprov-jdk18on:1.76")
    implementation("org.bouncycastle:bcutil-jdk18on:1.76")

    //api(libs.org.bouncycastle.bcprov.jdk18on)
    implementation(libs.com.android.tools.build.gradle)
    implementation("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:${libs.versions.kotlinGradlePlugin.get()}")
    gradleApi()
}