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

    implementation(libs.io.github.lsposed.hiddenapibypass)
    // noinspection UseTomlInstead,GradleDependency
    implementation("androidx.window:window:1.4.0-alpha05")
    //noinspection GradleDependency,UseTomlInstead
    implementation("commons-io:commons-io:2.5")
    //noinspection UseTomlInstead
    implementation("com.termux:termux-am-library:v2.0.0")

    api(project(":Submodule:Termux:terminal-view"))

    val providerLibrary = project.layout.projectDirectory.asFile.resolve("base-api.jar")
    if (providerLibrary.exists()) {
        compileOnly(files(providerLibrary))
        //println("Termux 的 provider 库路径: $providerLibrary")
    }

}
