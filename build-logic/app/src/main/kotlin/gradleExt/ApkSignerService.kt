@file:Suppress("DEPRECATION")

package gradleExt

import com.android.apksig.ApkSigner
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.SigningConfig
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



fun BaseAppModuleExtension.apkSign(
    outputApk: File,
    inputApk: File,
    signingConfigs: SigningConfig,
    ){
    // 支持jks，bks
    val jks: KeyStore = JksKeyStore()
    jks.load(signingConfigs.storeFile?.inputStream(), signingConfigs.storePassword?.toCharArray())
    val privateKey = jks.getKey(signingConfigs.keyAlias, signingConfigs.keyPassword?.toCharArray()) as PrivateKey
    val certificate = jks.getCertificateChain(signingConfigs.keyAlias)[0] as X509Certificate
    val signerConfig = ApkSigner.SignerConfig.Builder(
        "ANDROID",
        privateKey,
        listOf(certificate)
    ).build()
    val builder = ApkSigner.Builder(listOf(signerConfig))
    builder.setCreatedBy("Android Gradle 8.7.3")
        .setMinSdkVersion(defaultConfig.minSdk!!)
        .setInputApk(inputApk)
        .setOutputApk(outputApk)
        .setV1SigningEnabled(signingConfigs.enableV1Signing!!)
        .setV2SigningEnabled(signingConfigs.enableV2Signing!!)
        .setV3SigningEnabled(signingConfigs.enableV3Signing!!)
        .build()
        .sign()


}