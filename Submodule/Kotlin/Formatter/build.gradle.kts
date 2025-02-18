plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    api(libs.com.google.googlejavaformat.google.java.format){
        exclude(group = "com.google.guava", module = "guava")
    }
    // https://mvnrepository.com/artifact/com.facebook/ktfmt
//    api(libs.com.facebook.ktfmt) {
//        exclude(group = "net.java.dev.jna", module = "jna")
//        exclude(group = "javax.inject", module = "javax.inject")
//        exclude(group = "org.jetbrains", module = "annotations")
//        exclude(group = "com.google.guava", module = "guava")
//        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
//    }


    api(files("libs/ktfmt-0.54.jar"))

}
