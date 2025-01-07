
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("aide-termux.project")
}


dependencies {
    // androidx&material和其他杂七杂八的框架
    api(libs.bundles.google.androidx)
    implementation(libs.com.google.guava)
    implementation(libs.bundles.noties.markwon)
    api(project(":Submodule:Termux:terminal-emulator"))
}