package com.example.proyectomastermartini.domain

import kotlin.text.iterator

/**
 * Reglas de validación centralizadas y desacopladas de la UI.
 * Cada función devuelve el mensaje de error, o null si el valor es válido.
 * Son funciones puras, por lo que se pueden probar con tests unitarios.
 */
object Validators {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val NAME_REGEX = Regex("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ' ]+$")
    private val PHONE_REGEX = Regex("^(\\+?56)?9\\d{8}$")

    fun name(value: String): String? {
        val v = value.trim()
        return when {
            v.isEmpty() -> "Ingresa tu nombre."
            v.length < 3 -> "El nombre debe tener al menos 3 letras."
            !NAME_REGEX.matches(v) -> "El nombre solo puede contener letras."
            else -> null
        }
    }

    fun email(value: String): String? {
        val v = value.trim()
        return when {
            v.isEmpty() -> "Ingresa tu correo."
            !EMAIL_REGEX.matches(v) -> "Ingresa un correo válido, por ejemplo nombre@correo.cl."
            else -> null
        }
    }

    /** Celular chileno: 9 dígitos que parten con 9 (admite prefijo +56). */
    fun phone(value: String): String? {
        val v = value.replace(" ", "")
        return when {
            v.isEmpty() -> "Ingresa tu teléfono."
            !PHONE_REGEX.matches(v) -> "Ingresa un celular válido, ej: 912345678."
            else -> null
        }
    }

    fun password(value: String): String? = when {
        value.isEmpty() -> "Ingresa una contraseña."
        value.length < 8 -> "Debe tener al menos 8 caracteres."
        value.none { it.isLetter() } -> "Debe incluir al menos una letra."
        value.none { it.isDigit() } -> "Debe incluir al menos un número."
        else -> null
    }

    fun loginPassword(value: String): String? =
        if (value.isEmpty()) "Ingresa tu contraseña." else null

    fun confirmPassword(password: String, confirm: String): String? = when {
        confirm.isEmpty() -> "Confirma tu contraseña."
        confirm != password -> "Las contraseñas no coinciden."
        else -> null
    }

    fun acceptTerms(accepted: Boolean): String? =
        if (accepted) null else "Debes aceptar los términos para continuar."

    /** RUT de empresa (opcional). Si se ingresa, se valida con módulo 11. */
    fun rutOptional(value: String): String? {
        val clean = value.replace(".", "").replace("-", "").trim().uppercase()
        if (clean.isEmpty()) return null
        if (clean.length < 8 || clean.length > 9) return "El RUT debe tener 8 o 9 caracteres, ej: 76.123.456-0."
        val body = clean.dropLast(1)
        val dv = clean.last()
        if (!body.all { it.isDigit() }) return "El RUT solo debe contener números y un dígito verificador."
        return if (calculateDv(body) == dv) null else "El RUT ingresado no es válido."
    }

    fun calculateDv(body: String): Char {
        var sum = 0
        var factor = 2
        for (digit in body.reversed()) {
            sum += digit.digitToInt() * factor
            factor = if (factor == 7) 2 else factor + 1
        }
        return when (val result = 11 - (sum % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> result.digitToChar()
        }
    }

    fun addressOptional(value: String): String? {
        val v = value.trim()
        return if (v.isNotEmpty() && v.length < 5) "La dirección debe tener al menos 5 caracteres." else null
    }

    fun required(value: String, message: String): String? =
        if (value.isBlank()) message else null

    fun interests(selected: Collection<String>): String? =
        if (selected.isEmpty()) "Selecciona al menos un interés." else null

    fun subject(value: String): String? {
        val v = value.trim()
        return when {
            v.isEmpty() -> "Ingresa un asunto."
            v.length < 5 -> "El asunto debe tener al menos 5 caracteres."
            v.length > 60 -> "El asunto no puede superar los 60 caracteres."
            else -> null
        }
    }

    const val MESSAGE_MAX = 300

    fun message(value: String): String? {
        val v = value.trim()
        return when {
            v.isEmpty() -> "Escribe tu consulta."
            v.length < 20 -> "Cuéntanos un poco más (mínimo 20 caracteres)."
            v.length > MESSAGE_MAX -> "El mensaje no puede superar los $MESSAGE_MAX caracteres."
            else -> null
        }
    }
}
