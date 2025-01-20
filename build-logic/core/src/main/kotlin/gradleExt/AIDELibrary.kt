package gradleExt

import org.gradle.api.Project
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/7
 */


/**
 * 扫描以下目录的jar
 * */
val nameLibrary = mutableListOf(
    "libs",
    "provider",
    "resources",
    "runtime"
)

/**
 * 用于排除指定的jar包
 * */
val excludeLibrary = mutableListOf<String>(
    //"desugar-lib.jar",
    "java-formatter.jar",
    "javax-tools-22.jar"
//    "dependencies_desugar.jar",
//    "BaseIncrement-2.3.jar",
//    "ClassReaderLibrary-1.8.7-beta.jar"
)

/**
 * 用于排除指定的文件
 * */
val excludeFile = mutableListOf(
    "META-INF/com/android/build/gradle/app-metadata.properties"
)


/**
 * 所有模块
 * */
val allModuleName = mutableListOf(
    "appAideBase",
    "app_rewrite"
)


fun Project.filterResources(

) {
    val aideLibraryDir = rootDir.resolve("AIDELibrary")
    val aidePlusDir = rootDir.resolve("Submodule/AIDE/AIDE-Plus")
    val resourcesJarFile = rootDir.resolve("AIDELibrary/resources.jar")

    try {
        val resourcesJarBytes = mutableMapOf<String, ByteArray>()

        allModuleName.forEach { moduleName ->
            val module = aidePlusDir.resolve(moduleName)
            nameLibrary.forEach {
                val sourceFolder = module.resolve(it)
                val outputDir = aideLibraryDir.resolve(it)
                if (sourceFolder.exists()) {
                    when (it) {
                        "libs" -> {
                            copyAllJar(sourceFolder, outputDir)
                            copyDex(sourceFolder, aideLibraryDir, { name, bytes ->
                                resourcesJarBytes[name] = bytes
                            })
                        }

                        "provider" -> {
                            copyAllJar(sourceFolder, outputDir)
                        }

                        "resources" -> {
                            copyResources(sourceFolder, aideLibraryDir, { name, bytes ->
                                resourcesJarBytes[name] = bytes
                            })
                        }

                        "runtime" -> {
                            copyDex(sourceFolder, aideLibraryDir, { name, bytes ->
                                resourcesJarBytes[name] = bytes
                            })
                        }
                    }
                }

            }
        }
        createNewZip(resourcesJarFile,resourcesJarBytes)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}


/**
 * 创建 ZIP 文件
 * @param outputFile ZIP文件的路径
 * @param zipAllBytes 文件内容
 */

fun createNewZip(
    outputFile: File,
    zipAllBytes: MutableMap<String, ByteArray>
) {
    outputFile.parentFile.mkdirs()
    if (outputFile.exists()) {
        outputFile.delete()
    }
    ZipOutputStream(outputFile.outputStream()).use { outputZip ->
        // 添加文件内容
        zipAllBytes.forEach { (name, data) ->
            outputZip.putNextEntry(ZipEntry(name))
            outputZip.write(data)
            outputZip.closeEntry()
        }
    }
}

/**
 * 检测zip是否包含dex
 * */
fun zipContainsDex(
    zipFile: File
): Boolean {
    var containsDex = false
    ZipFile(zipFile).use {
        it.entries().asSequence().forEach { entry ->
            if (entry.name.indexOf('/') == -1 && entry.name.endsWith(".dex")) {
                containsDex = true
                return@forEach
            }
        }
    }
    return containsDex
}


/**
 * 进行复制资源文件
 * */
fun copyResources(
    inputDir: File,
    aideLibraryDir: File,
    resource: (String, ByteArray) -> Unit,
) {
    val jniLibsDir = aideLibraryDir.resolve("jniLibs").apply { mkdirs() }
    inputDir
        .listFiles()
        ?.filter { it.name.endsWith(".jar") && it.name !in excludeLibrary && !zipContainsDex(it) }
        ?.forEach { file ->
            ZipFile(file).use { zipFile ->
                zipFile.entries().asSequence().forEach { zipEntry ->
                    zipFile.getInputStream(zipEntry).use { inputStream ->
                        val entryName = zipEntry.name
                        if (entryName.startsWith("assets/")) {
                            // 复制assets
                            val outputFile = File(aideLibraryDir, entryName)
                            outputFile.parentFile.mkdirs()
                            outputFile.outputStream().use {
                                inputStream.copyTo(it)
                            }
                        } else if (entryName.startsWith("lib/") && entryName.endsWith(".so")) {
                            // 复制so
                            val outputFile =
                                File(jniLibsDir, entryName.removePrefix("lib/"))
                            outputFile.parentFile.mkdirs()
                            outputFile.outputStream().use {
                                inputStream.copyTo(it)
                            }
                        } else {
                            // 复制其他文件
                            if (entryName !in excludeFile) {
                                resource(entryName, inputStream.readBytes())
                            }
                        }
                    }
                }
            }
        }

}

/**
 * 进行Dex复制
 * */
fun copyDex(
    inputDir: File,
    aideLibraryDir: File,
    resource: (String, ByteArray) -> Unit,
) {
    val dexLibsDir = aideLibraryDir.resolve("dexLibs").apply { mkdirs() }
    inputDir
        .listFiles()
        ?.filter { it.name.endsWith(".jar") && it.name !in excludeLibrary && zipContainsDex(it) }
        ?.forEach {
            ZipFile(it).use { zip ->
                zip.entries().asSequence().apply {
                    var dexCount = 0
                    forEach { zipEntry ->
                        zip.getInputStream(zipEntry).use { inputStream ->
                            val entryName = zipEntry.name
                            if (entryName.indexOf('/') == -1 && entryName.endsWith(
                                    ".dex"
                                )
                            ) {
                                val dexOutPath = dexLibsDir.resolve(
                                    "${it.nameWithoutExtension}${
                                        dexCount.toString().let { count ->
                                            if (count == "0") "" else count
                                        }
                                    }.dex"
                                )
                                dexOutPath.parentFile.mkdirs()
                                dexOutPath.outputStream().use {
                                    inputStream.copyTo(it)
                                }
                                dexCount++
                            } else {
                                if (entryName !in excludeFile) {
                                    resource(entryName, inputStream.readBytes())
                                }
                            }
                        }
                    }
                    dexCount = 0
                }
            }

        }
}


/**
 * 进行增量复制
 * */
fun copyAllJar(
    inputDir: File,
    outputDir: File
) {
    inputDir
        .listFiles()
        ?.filter { it.name.endsWith(".jar") && it.name !in excludeLibrary && !zipContainsDex(it) }
        ?.forEach {
            val outFile = File(outputDir, it.name)
            if (!outFile.exists() || it.lastModified() > outFile.lastModified()) {
                outFile.parentFile.mkdirs()
                Files.copy(
                    it.toPath(),
                    outFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }
}


/**
 * 替换目录内xml文件内指定内容
 * @param directory 替换目录的路径
 * @param targetString 需要替换的内容
 * */
fun processXmlFiles(directory: File, targetString: String) {
    if (!directory.exists() || !directory.isDirectory) {
        return
    }
    val xmlFiles = directory.listFiles { file -> file.extension == "xml" }
    if (xmlFiles.isNullOrEmpty()) {
        return
    }
    xmlFiles.forEach { file ->
        val content = file.readText()
        if (content.contains(targetString)) {
            val updatedContent = content.replace(targetString, "")
            file.writeText(updatedContent)
            //println("已处理文件: ${file.name}")
        }
    }
}

/**
 * 递归复制文件夹
 * @param source 源目录
 * @param target 目标目录
 * @throws IOException 当文件操作失败时抛出
 */
fun copyDirectory(
    source: File,
    target: File,
    callBack: (File) -> Unit,
) {
    if (!source.exists()) {
        throw IOException("源目录不存在: ${source.absolutePath}")
    }
    if (!target.exists()) {
        target.mkdirs() // 创建目标目录
    }
    source.listFiles()?.forEach { file ->
        val targetFile = File(target, file.name)
        if (file.isDirectory) {
            copyDirectory(file, targetFile,callBack)
        } else {
            file.copyTo(targetFile, overwrite = true)
            callBack(targetFile)
            //println("已复制文件: ${file.absolutePath} -> ${targetFile.absolutePath}")
        }
    }
}
