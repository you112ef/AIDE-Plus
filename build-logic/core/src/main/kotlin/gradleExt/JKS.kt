package gradleExt

import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.DigestInputStream
import java.security.DigestOutputStream
import java.security.Key
import java.security.KeyFactory
import java.security.KeyStoreException
import java.security.KeyStoreSpi
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.UnrecoverableKeyException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Date
import java.util.Enumeration
import java.util.Locale
import java.util.Vector
import javax.crypto.EncryptedPrivateKeyInfo
import javax.crypto.spec.SecretKeySpec

/* JKS.java -- implementation of the "JKS" key store.
Copyright (C) 2003  Casey Marshall <rsdio@metastatic.org>

Permission to use, copy, modify, distribute, and sell this software and
its documentation for any purpose is hereby granted without fee,
provided that the above copyright notice appear in all copies and that
both that copyright notice and this permission notice appear in
supporting documentation.  No representations are made about the
suitability of this software for any purpose.  It is provided "as is"
without express or implied warranty.

This program was derived by reverse-engineering Sun's own
implementation, using only the public API that is available in the 1.4.1
JDK.  Hence nothing in this program is, or is derived from, anything
copyrighted by Sun Microsystems.  While the "Binary Evaluation License
Agreement" that the JDK is licensed under contains blanket statements
that forbid reverse-engineering (among other things), it is my position
that US copyright law does not and cannot forbid reverse-engineering of
software to produce a compatible implementation.  There are, in fact,
numerous clauses in copyright law that specifically allow
reverse-engineering, and therefore I believe it is outside of Sun's
power to enforce restrictions on reverse-engineering of their software,
and it is irresponsible for them to claim they can.  */

/**
 * This is an implementation of Sun's proprietary key store
 * algorithm, called "JKS" for "Java Key Store". This implementation was
 * created entirely through reverse-engineering.
 *
 *
 * The format of JKS files is, from the start of the file:
 *
 *
 *  1. Magic bytes. This is a four-byte integer, in big-endian byte
 * order, equal to `0xFEEDFEED`.
 *  1. The version number (probably), as a four-byte integer (all
 * multibyte integral types are in big-endian byte order). The current
 * version number (in modern distributions of the JDK) is 2.
 *  1. The number of entrires in this keystore, as a four-byte
 * integer. Call this value *n*
 *  1. Then, *n* times:
 *
 *  1. The entry type, a four-byte int. The value 1 denotes a private
 * key entry, and 2 denotes a trusted certificate.
 *  1. The entry's alias, formatted as strings such as those written
 * by [DataOutput.writeUTF(String)](http://java.sun.com/j2se/1.4.1/docs/api/java/io/DataOutput.html#writeUTF(java.lang.String)).
 *  1. An eight-byte integer, representing the entry's creation date,
 * in milliseconds since the epoch.
 *
 *
 * Then, if the entry is a private key entry:
 *
 *  1. The size of the encoded key as a four-byte int, then that
 * number of bytes. The encoded key is the DER encoded bytes of the
 * [EncryptedPrivateKeyInfo](http://java.sun.com/j2se/1.4.1/docs/api/javax/crypto/EncryptedPrivateKeyInfo.html) structure (the
 * encryption algorithm is discussed later).
 *  1. A four-byte integer, followed by that many encoded
 * certificates, encoded as described in the trusted certificates
 * section.
 *
 *
 *
 * Otherwise, the entry is a trusted certificate, which is encoded
 * as the name of the encoding algorithm (e.g. X.509), encoded the same
 * way as alias names. Then, a four-byte integer representing the size
 * of the encoded certificate, then that many bytes representing the
 * encoded certificate (e.g. the DER bytes in the case of X.509).
 *
 *
 *
 *  1. Then, the signature.
 *
 *
 *
 *
 *
 *
 * (See [this file](genkey.java) for some idea of how I
 * was able to figure out these algorithms)
 *
 *
 * Decrypting the key works as follows:
 *
 *
 *  1. The key length is the length of the ciphertext minus 40. The
 * encrypted key, `ekey`, is the middle bytes of the
 * ciphertext.
 *  1. Take the first 20 bytes of the encrypted key as a seed value,
 * `K[0]`.
 *  1. Compute `K[1] ... K[n]`, where
 * `|K[i]| = 20`, `n = ceil(|ekey| / 20)`, and
 * `K[i] = SHA-1(UTF-16BE(password) + K[i-1])`.
 *  1. `key = ekey ^ (K[1] + ... + K[n])`.
 *  1. The last 20 bytes are the checksum, computed as `H =
 * SHA-1(UTF-16BE(password) + key)`. If this value does not match
 * the last 20 bytes of the ciphertext, output `FAIL`. Otherwise,
 * output `key`.
 *
 *
 *
 * The signature is defined as `SHA-1(UTF-16BE(password) +
 * US_ASCII("Mighty Aphrodite") + encoded_keystore)` (yup, Sun
 * engineers are just that clever).
 *
 *
 * (Above, SHA-1 denotes the secure hash algorithm, UTF-16BE the
 * big-endian byte representation of a UTF-16 string, and US_ASCII the
 * ASCII byte representation of the string.)
 *
 *
 * The source code of this class should be available in the file [JKS.java](http://metastatic.org/source/JKS.java).
 *
 * @author Casey Marshall (rsdio@metastatic.org)
 *
 * Changes by Ken Ellinwood:
 * ** Fixed a NullPointerException in engineLoad(). This method must return gracefully if the keystore input stream is null.
 * ** engineGetCertificateEntry() was updated to return the first cert in the chain for private key entries.
 * ** Lowercase the alias names, otherwise keytool chokes on the file created by this code.
 */
class JKS : KeyStoreSpi() {
    private val aliases = Vector<String>()
    private val trustedCerts =
        HashMap<String, Certificate>()
    private val privateKeys = HashMap<String, ByteArray>()
    private val certChains =
        HashMap<String, Array<Certificate>>()
    private val dates = HashMap<String, Date>()

    // Instance methods.
    // ------------------------------------------------------------------------
    @Throws(NoSuchAlgorithmException::class, UnrecoverableKeyException::class)
    override fun engineGetKey(aliass: String, password: CharArray): Key? {
        var alias = aliass
        alias = alias.lowercase(Locale.getDefault())

        if (!privateKeys.containsKey(alias)) return null
        val key = decryptKey(
            privateKeys[alias]!!,
            charsToBytes(password)
        )
        val chain = engineGetCertificateChain(alias)
        if (chain.isNotEmpty()) {
            try {
                // Private and public keys MUST have the same algorithm.
                val fact = KeyFactory.getInstance(
                    chain[0].publicKey.algorithm
                )
                return fact.generatePrivate(PKCS8EncodedKeySpec(key))
            } catch (x: InvalidKeySpecException) {
                throw UnrecoverableKeyException(x.message)
            }
        } else return SecretKeySpec(key, alias)
    }

    override fun engineGetCertificateChain(alias: String): Array<Certificate> {
        return certChains[alias.lowercase(Locale.getDefault())] as Array<Certificate>
    }

    override fun engineGetCertificate(aliass: String): Certificate {
        var alias = aliass
        alias = alias.lowercase(Locale.getDefault())
        if (engineIsKeyEntry(alias)) {
            val certChain = certChains[alias]
            if (!certChain.isNullOrEmpty()) return certChain[0]
        }
        return trustedCerts[alias] as Certificate
    }

    override fun engineGetCreationDate(alias: String): Date {
        return dates[alias.lowercase(Locale.getDefault())] as Date
    }

    // XXX implement writing methods.
    @Throws(KeyStoreException::class)
    override fun engineSetKeyEntry(
        aliass: String,
        key: Key,
        passwd: CharArray,
        certChain: Array<Certificate>?
    ) {
        var alias = aliass
        alias = alias.lowercase(Locale.getDefault())
        if (trustedCerts.containsKey(alias)) throw KeyStoreException("\"$alias is a trusted certificate entry")
        privateKeys[alias] =
            encryptKey(key, charsToBytes(passwd))
        if (certChain != null) certChains[alias] = certChain
        else certChains[alias] = emptyArray()
        if (!aliases.contains(alias)) {
            dates[alias] = Date()
            aliases.add(alias)
        }
    }

    @Throws(KeyStoreException::class)
    override fun engineSetKeyEntry(
        aliass: String,
        encodedKey: ByteArray,
        certChain: Array<Certificate>?
    ) {
        var alias = aliass
        alias = alias.lowercase(Locale.getDefault())
        if (trustedCerts.containsKey(alias)) throw KeyStoreException("\"$alias\" is a trusted certificate entry")
        try {
            EncryptedPrivateKeyInfo(encodedKey)
        } catch (ioe: IOException) {
            throw KeyStoreException("encoded key is not an EncryptedPrivateKeyInfo")
        }
        privateKeys[alias] = encodedKey
        if (certChain != null) certChains[alias] = certChain
        else certChains[alias] = emptyArray()
        if (!aliases.contains(alias)) {
            dates[alias] = Date()
            aliases.add(alias)
        }
    }

    @Throws(KeyStoreException::class)
    override fun engineSetCertificateEntry(aliass: String, cert: Certificate) {
        var alias = aliass
        alias = alias.lowercase(Locale.getDefault())
        if (privateKeys.containsKey(alias)) throw KeyStoreException("\"$alias\" is a private key entry")
        trustedCerts[alias] = cert
        if (!aliases.contains(alias)) {
            dates[alias] = Date()
            aliases.add(alias)
        }
    }

    @Throws(KeyStoreException::class)
    override fun engineDeleteEntry(alias: String) {
        aliases.remove(alias.lowercase(Locale.getDefault()))
    }

    override fun engineAliases(): Enumeration<String>? {
        return aliases.elements()
    }

    override fun engineContainsAlias(alias: String): Boolean {
        return aliases.contains(alias.lowercase(Locale.getDefault()))
    }

    override fun engineSize(): Int {
        return aliases.size
    }

    override fun engineIsKeyEntry(alias: String): Boolean {
        return privateKeys.containsKey(alias.lowercase(Locale.getDefault()))
    }

    override fun engineIsCertificateEntry(alias: String): Boolean {
        return trustedCerts.containsKey(alias.lowercase(Locale.getDefault()))
    }

    override fun engineGetCertificateAlias(cert: Certificate): String? {
        val keys: Iterator<*> = trustedCerts.keys.iterator()
        while (keys.hasNext()) {
            val alias = keys.next() as String
            if (cert == trustedCerts[alias]) return alias
        }
        return null
    }

    @Throws(IOException::class, NoSuchAlgorithmException::class, CertificateException::class)
    override fun engineStore(out: OutputStream, passwd: CharArray) {
        val md = MessageDigest.getInstance("SHA1")
        md.update(charsToBytes(passwd))
        md.update("Mighty Aphrodite".toByteArray(charset("UTF-8")))
        val dout = DataOutputStream(DigestOutputStream(out, md))
        dout.writeInt(MAGIC)
        dout.writeInt(2)
        dout.writeInt(aliases.size)
        val e: Enumeration<*> = aliases.elements()
        while (e.hasMoreElements()) {
            val alias = e.nextElement() as String
            if (trustedCerts.containsKey(alias)) {
                dout.writeInt(TRUSTED_CERT)
                dout.writeUTF(alias)
                dout.writeLong((dates[alias] as Date).time)
                writeCert(
                    dout,
                    trustedCerts[alias]!!
                )
            } else {
                dout.writeInt(PRIVATE_KEY)
                dout.writeUTF(alias)
                dout.writeLong((dates[alias] as Date).time)
                val key = privateKeys[alias]
                dout.writeInt(key!!.size)
                dout.write(key)
                val chain = certChains[alias] as Array<Certificate>
                dout.writeInt(chain.size)
                for (i in chain.indices) writeCert(dout, chain[i])
            }
        }
        val digest = md.digest()
        dout.write(digest)
    }

    @Throws(IOException::class, NoSuchAlgorithmException::class, CertificateException::class)
    override fun engineLoad(input: InputStream?, passwd: CharArray?) {
        val md = MessageDigest.getInstance("SHA")
        passwd?.let { md.update(charsToBytes(it)) }
        md.update("Mighty Aphrodite".toByteArray(Charsets.UTF_8)) // HAR HAR

        aliases.clear()
        trustedCerts.clear()
        privateKeys.clear()
        certChains.clear()
        dates.clear()

        if (input == null) return

        DataInputStream(DigestInputStream(input, md)).use { din ->
            if (din.readInt() != MAGIC) {
                throw IOException("not a JavaKeyStore")
            }
            din.readInt() // version no.
            val n = din.readInt()
            aliases.ensureCapacity(n)

            if (n < 0) {
                throw IOException("negative entry count")
            }

            for (i in 0 until n) {
                val type = din.readInt()
                val alias = din.readUTF()
                aliases.add(alias)
                dates[alias] = Date(din.readLong())

                when (type) {
                    PRIVATE_KEY -> {
                        val len = din.readInt()
                        val encoded = ByteArray(len)
                        din.readFully(encoded)
                        privateKeys[alias] = encoded

                        val count = din.readInt()
                        val chain = Array(count) { readCert(din) }
                        certChains[alias] = chain
                    }
                    TRUSTED_CERT -> {
                        trustedCerts[alias] = readCert(din)
                    }
                    else -> throw IOException("malformed key store")
                }
            }

            val hash = ByteArray(20)
            din.readFully(hash)
            if (passwd != null && MessageDigest.isEqual(hash, md.digest())) {
                throw IOException("signature not verified")
            }
        }
    }


    companion object {
        // Constants and fields.
        // ------------------------------------------------------------------------
        /** Ah, Sun. So goddamned clever with those magic bytes.  */
        private const val MAGIC = -0x1120113

        private const val PRIVATE_KEY = 1
        private const val TRUSTED_CERT = 2

        // Own methods.
        // ------------------------------------------------------------------------
        @Throws(
            IOException::class,
            CertificateException::class,
            NoSuchAlgorithmException::class
        )
        private fun readCert(`in`: DataInputStream): Certificate {
            val type = `in`.readUTF()
            val len = `in`.readInt()
            val encoded = ByteArray(len)
            `in`.read(encoded)
            val factory = CertificateFactory.getInstance(type)
            return factory.generateCertificate(ByteArrayInputStream(encoded))
        }

        @Throws(IOException::class, CertificateException::class)
        private fun writeCert(dout: DataOutputStream, cert: Certificate) {
            dout.writeUTF(cert.type)
            val b = cert.encoded
            dout.writeInt(b.size)
            dout.write(b)
        }

        @Throws(UnrecoverableKeyException::class)
        private fun decryptKey(encryptedPKI: ByteArray, passwd: ByteArray): ByteArray {
            try {
                val epki =
                    EncryptedPrivateKeyInfo(encryptedPKI)
                val encr = epki.encryptedData
                val keystream = ByteArray(20)
                System.arraycopy(encr, 0, keystream, 0, 20)
                val check = ByteArray(20)
                System.arraycopy(encr, encr.size - 20, check, 0, 20)
                val key = ByteArray(encr.size - 40)
                val sha = MessageDigest.getInstance("SHA1")
                var count = 0
                while (count < key.size) {
                    sha.reset()
                    sha.update(passwd)
                    sha.update(keystream)
                    sha.digest(keystream, 0, keystream.size)
                    var i = 0
                    while (i < keystream.size && count < key.size) {
                        key[count] = (keystream[i].toInt() xor encr[count + 20].toInt()).toByte()
                        count++
                        i++
                    }
                }
                sha.reset()
                sha.update(passwd)
                sha.update(key)
                if (!MessageDigest.isEqual(
                        check,
                        sha.digest()
                    )
                ) throw UnrecoverableKeyException("checksum mismatch")
                return key
            } catch (x: Exception) {
                throw UnrecoverableKeyException(x.message)
            }
        }

        @Throws(KeyStoreException::class)
        private fun encryptKey(key: Key, passwd: ByteArray): ByteArray {
            try {
                val sha = MessageDigest.getInstance("SHA1")
                //val rand = SecureRandom.getInstance("SHA1PRNG")
                val k = key.encoded
                val encrypted = ByteArray(k.size + 40)
                val keystream = SecureRandom.getSeed(20)
                System.arraycopy(keystream, 0, encrypted, 0, 20)
                var count = 0
                while (count < k.size) {
                    sha.reset()
                    sha.update(passwd)
                    sha.update(keystream)
                    sha.digest(keystream, 0, keystream.size)
                    var i = 0
                    while (i < keystream.size && count < k.size) {
                        encrypted[count + 20] = (keystream[i].toInt() xor k[count].toInt()).toByte()
                        count++
                        i++
                    }
                }
                sha.reset()
                sha.update(passwd)
                sha.update(k)
                sha.digest(encrypted, encrypted.size - 20, 20)
                // 1.3.6.1.4.1.42.2.17.1.1 is Sun's private OID for this
                // encryption algorithm.
                return EncryptedPrivateKeyInfo(
                    "1.3.6.1.4.1.42.2.17.1.1",
                    encrypted
                ).encoded
            } catch (x: Exception) {
                throw KeyStoreException(x.message)
            }
        }

        private fun charsToBytes(passwd: CharArray): ByteArray {
            val buf = ByteArray(passwd.size * 2)
            var i = 0
            var j = 0
            while (i < passwd.size) {
                buf[j++] = (passwd[i].code ushr 8).toByte()
                buf[j++] = passwd[i].code.toByte()
                i++
            }
            return buf
        }
    }
}

