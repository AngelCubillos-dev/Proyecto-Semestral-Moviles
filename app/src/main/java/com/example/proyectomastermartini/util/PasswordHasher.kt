package com.example.mastermartini.util

import java.security.MessageDigest
import java.security.SecureRandom

/** Hash SHA-256 con sal aleatoria: las contraseñas nunca se guardan en texto plano. */
object PasswordHasher {
    fun newSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.toHex()
    }

    fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest((salt + password).toByteArray(Charsets.UTF_8)).toHex()
    }

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
}
