
allprojects {
    //noinspection GrDeprecatedAPIUsage
    @Suppress("DEPRECATION")
    buildDir =
        file("${rootProject.rootDir.path}/build/${project.path.substring(1).replace(':', '-')}")
}

