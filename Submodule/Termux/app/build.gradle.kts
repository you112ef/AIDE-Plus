
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("aide-termux.project")
}

dependencies {
    api(project(":Submodule:Termux:terminal-emulator"))
    api(project(":Submodule:Termux:terminal-view"))
    api(project(":Submodule:Termux:termux-shared"))

}