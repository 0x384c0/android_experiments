package com.example.corenetwork

import android.annotation.SuppressLint
import com.example.corenetwork.utils.byteArrayOfInts
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoHelper(private val key:ByteArray, private val useJavaEncoder:Boolean = false){
    fun encryptWithAES(text:String):String{
        val blockSize = 16
        val textBytesUTF8 = text.toByteArray(Charsets.UTF_8)

        val randomSecureRandom = SecureRandom()
        val iv = ByteArray(blockSize)


        randomSecureRandom.nextBytes(iv)
        val ivParams = IvParameterSpec(iv)
        val keyParams = SecretKeySpec(key,"AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, keyParams, ivParams)

        val encrypted = cipher.doFinal(textBytesUTF8)
        val ivAndEncrypted = encrypted + iv
        return encodeToBase64String(ivAndEncrypted)
    }

    @SuppressLint("NewApi")
    private fun encodeToBase64String(bytes: ByteArray):String{
        return if (useJavaEncoder){
            java.util.Base64.getEncoder().encodeToString(bytes)
        } else {
            android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
        }
    }


    companion object {
        private val AES_KEY = byteArrayOfInts(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        private val instance = CryptoHelper(AES_KEY)
        fun getInstance(): CryptoHelper {
            return instance
        }
    }
}