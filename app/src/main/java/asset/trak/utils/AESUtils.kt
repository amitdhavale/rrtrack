package asset.trak.utils

import okhttp3.internal.and
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


object AESUtils {

    private val keyValue = byteArrayOf('c'.code.toByte(),
        'o'.code.toByte(),
        'd'.code.toByte(),
        'i'.code.toByte(),
        'n'.code.toByte(),
        'g'.code.toByte(),
        'a'.code.toByte(),
        'f'.code.toByte(),
        'f'.code.toByte(),
        'a'.code.toByte(),
        'i'.code.toByte(),
        'r'.code.toByte(),
        's'.code.toByte(),
        'c'.code.toByte(),
        'o'.code.toByte(),
        'm'.code.toByte())


    @Throws(Exception::class)
    fun encrypt(cleartext: String): String? {
        val rawKey = getRawKey()
        val result = encrypt(rawKey, cleartext.toByteArray())
        val encrypted = toHex(result)
        LogUtils.logD("AESUtils-->> ", "encrypted:$encrypted")
        return encrypted
    }

    @Throws(Exception::class)
    fun decrypt(encrypted: String): String? {
        val enc = toByte(encrypted)
        val result = decrypt(enc)
        val decrypted = String(result)
        LogUtils.logD("AESUtils-->> ", "decrypted:$decrypted")
        return decrypted
    }

    @Throws(Exception::class)
    private fun getRawKey(): ByteArray {
        val key: SecretKey = SecretKeySpec(keyValue, "AES")
        return key.getEncoded()
    }

    @Throws(Exception::class)
    private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val skeySpec: SecretKey = SecretKeySpec(raw, "AES")
        val cipher: Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        return cipher.doFinal(clear)
    }

    @Throws(Exception::class)
    private fun decrypt(encrypted: ByteArray): ByteArray {
        val skeySpec: SecretKey = SecretKeySpec(keyValue, "AES")
        val cipher: Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }

    fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
            16).toByte()
        return result
    }

    fun toHex(buf: ByteArray?): String? {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private const val HEX = "0123456789ABCDEF"

    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX[b.toInt() shr 4 and 0x0f]).append(HEX[b and 0x0f])
    }
}