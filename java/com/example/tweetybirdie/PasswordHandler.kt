//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie

import java.security.MessageDigest

class PasswordHandler {

    companion object {

        fun hashPassword(password: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val encodedHash = digest.digest(password.toByteArray())
            return bytesToHex(encodedHash)
        }

        private fun bytesToHex(bytes: ByteArray): String {
            val hexChars = "0123456789ABCDEF"
            val hexString = StringBuilder(bytes.size * 2)
            for (byte in bytes) {
                val value = byte.toInt() and 0xFF
                hexString.append(hexChars[value shr 4])
                hexString.append(hexChars[value and 0x0F])
            }
            return hexString.toString()
        }
    }
}