
allprojects {
    //noinspection GrDeprecatedAPIUsage
    @Suppress("DEPRECATION")
    buildDir =
        file("${rootDir.path}/build/${project.path.substring(1).replace(':', '-')}")
}

