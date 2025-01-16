plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies{
    api(libs.com.google.googlejavaformat.google.java.format)
    // https://mvnrepository.com/artifact/com.facebook/ktfmt
    api(libs.com.facebook.ktfmt)

}
