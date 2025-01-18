plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.github.zeroaicy.aide.aaptcompiler"
}

dependencies {

    api(project(":Submodule:JDK:Jaxp-Xml"))
    // 太多错误了，直接引用jar包
    //api(files("libs/xml-completion-jaxp.jar"))

    api(libs.androidx.collection.ktx)

    api(libs.org.jetbrains.annotations)


    api(libs.com.android.tools.annotations)
    api(libs.com.android.tools.common)
    api(libs.com.android.tools.aapt2.proto)
    api(libs.com.google.protobuf.java)

    api(libs.bundles.google.androidx)
    api(libs.com.android.tools.annotations)
    api(libs.org.jetbrains.annotations)
    api(libs.com.google.guava) {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    api(libs.org.apache.bcel)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}