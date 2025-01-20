package gradleExt

import java.io.File
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/12
 */


fun makeApk(
    aideLibraryDir: File,
    zipalignFile: File,
    apkFile: File,
    apkOutputFile: File,
    storeFile: File,
    storePassword: String,
    keyAlias: String,
    keyPassword: String,
    enableV1Signing: Boolean,
    enableV2Signing: Boolean,
    enableV3Signing: Boolean,
) {
    println("$apkFile 是否存在: ${apkFile.exists()}")
    println("$zipalignFile 是否存在: ${zipalignFile.exists()}")
    val unZipAlignSignerApkFile =
        apkFile.parentFile.resolve("${apkFile.nameWithoutExtension}-unzipaligned-unsigned.apk")

    val unSignedApkFile =
        apkFile.parentFile.resolve("${apkFile.nameWithoutExtension}-unsigned.apk")



    unSignedApkFile.delete()
    unZipAlignSignerApkFile.delete()

    apkFile.copyTo(unZipAlignSignerApkFile)


    unZipAlignSignerApkFile.apply {
        var dexCount = 0
        val fileContents = mutableMapOf<String, ByteArray>()
        ZipFile(this).use { zip ->
            zip.entries().asSequence().apply {
                forEach { zipEntry ->
                    zip.getInputStream(zipEntry).use { inputStream ->
                        if (zipEntry.name.indexOf('/') == -1 && zipEntry.name.endsWith(".dex")) {
                            dexCount++
                        }
                        fileContents[zipEntry.name] = inputStream.readBytes()
                    }
                }
            }
        }
        println("原本APK包含 $dexCount 个Dex")

        aideLibraryDir.resolve("dexLibs").listFiles()?.forEach {
            if (it.name.endsWith(".dex")) {
                if (it.name != "AIDE+_2.3.dex") {
                    dexCount++
                    val fileName = "classes${
                        dexCount.toString().let { count ->
                            if (count == "0") "" else count
                        }
                    }.dex"
                    fileContents[fileName] = it.readBytes()
                    println("已添加 ${it.name} to $fileName")
                }
            }
        }

        aideLibraryDir.resolve("dexLibs/AIDE+_2.3.dex").inputStream().use { inputStream ->
            dexCount++
            fileContents["classes$dexCount.dex"] = inputStream.readBytes()
            println("已添加 classes$dexCount.dex")
        }

        ZipOutputStream(outputStream()).use { outputZip ->
            fileContents.forEach { (name, data) ->
                outputZip.putNextEntry(ZipEntry(name).apply {
                    if (
                        name == "resources.arsc" ||
                        name.startsWith("lib/") && name.endsWith(".so") ||
                        name.startsWith("classes") && name.endsWith(".dex") && name.indexOf('/') == -1
                    ) {
                        val crc32 = CRC32()
                        crc32.update(data)
                        method = ZipEntry.STORED // 设置为不压缩模式
                        size = data.size.toLong() // 必须设置原始大小
                        compressedSize = size // 压缩大小与原始大小一致
                        crc = crc32.value // 必须设置 CRC 校验值
                    }
                })
                outputZip.write(data)
                outputZip.closeEntry()
            }
        }
        println("新文件输出路径为: $absolutePath")
    }


    //填充参数
    val args = mutableListOf(
        zipalignFile.absolutePath,
        "-p",
        "-v",
        "4",
        unZipAlignSignerApkFile.absolutePath,
        unSignedApkFile.absolutePath
    )

    Runtime.getRuntime()
        .exec(args.toTypedArray())
        .inputStream
        .bufferedReader()
        .use { it.readText().trim() }


    apkSign(
        minSdkVersion = Versions.minSdk,
        outputApk = apkOutputFile,
        inputApk = unSignedApkFile,
        storeFile,
        storePassword,
        keyAlias,
        keyPassword,
        enableV1Signing,
        enableV2Signing,
        enableV3Signing
    )

    apkOutputFile.inputStream().use { input ->
        apkFile.outputStream().use {  output->
            input.copyTo(output)
        }
    }




}