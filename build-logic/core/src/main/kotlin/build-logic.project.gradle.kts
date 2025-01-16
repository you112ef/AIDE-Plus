import gradleExt.configureBaseAppExtension
import gradleExt.configureBaseExtension
import gradleExt.configureKotlinExtension
import gradleExt.filterResources
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


subprojects {
    plugins.withId("com.android.application") {
        configureBaseAppExtension()
        filterResources()
    }
    plugins.withId("com.android.library") {
        configureBaseExtension()
    }
    plugins.withId("org.jetbrains.kotlin.android") {
        configureKotlinExtension()
    }
    /*plugins.withId("java-library"){
        tasks.withType<JavaCompile> {
            options.compilerArgs.add("-Xlint:none")
        }
    }*/

    tasks.withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:none")
    }

    tasks.withType<KotlinCompile> {
        @Suppress("DEPRECATION")
        kotlinOptions {
            jvmTarget = "21"
        }
    }

}


allprojects {
    //noinspection GrDeprecatedAPIUsage
    @Suppress("DEPRECATION")
    buildDir =
        file("${rootProject.rootDir.path}/build/${project.path.substring(1).replace(':', '-')}")





}
