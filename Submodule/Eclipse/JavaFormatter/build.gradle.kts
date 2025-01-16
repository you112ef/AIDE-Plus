import gradleExt.Versions

@Suppress("JavaPluginLanguageLevel")
plugins {
    id("java-library")
}

java {
    sourceCompatibility = Versions.JavaVersion
    targetCompatibility = Versions.JavaVersion
}

dependencies {
    // kotlin库如果类冲突，取消下一行注释
    // api "org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.0"
    compileOnly(libs.org.eclipse.jdt.ecj)
    api(libs.org.eclipse.platform.text)
    compileOnly(libs.org.osgi.framework)
}


tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:none")
}