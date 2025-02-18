package gradleExt

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyStore
import java.security.Security

class JksKeyStore : KeyStore {
    constructor() : super(JKS(), bouncyCastleProvider, "jks")

    private constructor(type: String) : super(JKS(), bouncyCastleProvider, type)

    companion object {
        private val bouncyCastleProvider: BouncyCastleProvider = BouncyCastleProvider()
        private var inited = false

        init {
            initBouncyCastleProvider()
            inited = true
        }

        private fun initBouncyCastleProvider() {
            if (inited) return
            println("替换bouncycastle版本")
            // 先移除，后添加
            Security.removeProvider(bouncyCastleProvider.name)

            Security.addProvider(bouncyCastleProvider)
        }

        val jksKeyStore: JksKeyStore
            get() = JksKeyStore()

        val bksKeyStore: JksKeyStore
            get() = JksKeyStore("bks")
    }
}

