@file:Suppress("DEPRECATION")

package gradleExt

import com.android.apksig.ApkSigner
import java.io.File
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate

/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/5
 */



fun apkSign(
    minSdkVersion:Int,
    outputApk: File,
    inputApk: File,
    storeFile: File,
    storePassword: String,
    keyAlias: String,
    keyPassword: String,
    enableV1Signing: Boolean,
    enableV2Signing: Boolean,
    enableV3Signing: Boolean,

    ){
    // 支持jks，bks
    val jks: KeyStore = JksKeyStore()
    jks.load(storeFile.inputStream(), storePassword.toCharArray())
    val privateKey = jks.getKey(keyAlias, keyPassword.toCharArray()) as PrivateKey
    val certificate = jks.getCertificateChain(keyAlias)[0] as X509Certificate
    val signerConfig = ApkSigner.SignerConfig.Builder(
        "ANDROID",
        privateKey,
        listOf(certificate)
    ).build()
    val builder = ApkSigner.Builder(listOf(signerConfig))
    builder.setCreatedBy("Android Gradle 8.8.1")
        .setMinSdkVersion(minSdkVersion)
        .setInputApk(inputApk)
        .setOutputApk(outputApk)
        .setV1SigningEnabled(enableV1Signing)
        .setV2SigningEnabled(enableV2Signing)
        .setV3SigningEnabled(enableV3Signing)
        .build()
        .sign()


}