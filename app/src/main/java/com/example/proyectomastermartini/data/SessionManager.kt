package com.example.proyectomastermartini.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Sesión local (SharedPreferences). Guarda solo el id del usuario activo y
 * la preferencia de accesibilidad "texto grande"; nunca contraseñas.
 */
class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _userId = MutableStateFlow(readUserId())
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    private val _largeText = MutableStateFlow(prefs.getBoolean(KEY_LARGE_TEXT, false))
    val largeText: StateFlow<Boolean> = _largeText.asStateFlow()

    fun startSession(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
        _userId.value = userId
    }

    fun endSession() {
        prefs.edit().remove(KEY_USER_ID).apply()
        _userId.value = null
    }

    fun setLargeText(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LARGE_TEXT, enabled).apply()
        _largeText.value = enabled
    }

    private fun readUserId(): Long? =
        if (prefs.contains(KEY_USER_ID)) prefs.getLong(KEY_USER_ID, -1L).takeIf { it >= 0 } else null

    private companion object {
        const val PREFS_NAME = "master_martini_session"
        const val KEY_USER_ID = "user_id"
        const val KEY_LARGE_TEXT = "large_text"
    }
}
