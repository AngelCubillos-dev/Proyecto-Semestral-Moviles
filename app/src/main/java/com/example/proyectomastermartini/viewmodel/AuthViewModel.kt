package com.example.mastermartini.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermartini.MasterMartiniApp
import com.example.mastermartini.data.repository.AuthResult
import com.example.mastermartini.domain.Validators
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val authError: String? = null,
    val loading: Boolean = false
)

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val acceptTerms: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val termsError: String? = null,
    val loading: Boolean = false
)

/**
 * Lógica de login y registro. La UI solo observa [login] y [register] y envía eventos;
 * al iniciar sesión, SessionManager emite el nuevo userId y la navegación reacciona.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as MasterMartiniApp).userRepository

    var login by mutableStateOf(LoginUiState())
        private set
    var register by mutableStateOf(RegisterUiState())
        private set

    // ---------- Login ----------
    fun onLoginEmail(v: String) { login = login.copy(email = v, emailError = null, authError = null) }
    fun onLoginPassword(v: String) { login = login.copy(password = v, passwordError = null, authError = null) }
    fun toggleLoginPassword() { login = login.copy(passwordVisible = !login.passwordVisible) }

    fun submitLogin() {
        val emailError = Validators.email(login.email)
        val passError = Validators.loginPassword(login.password)
        login = login.copy(emailError = emailError, passwordError = passError, authError = null)
        if (emailError != null || passError != null) return

        login = login.copy(loading = true)
        viewModelScope.launch {
            delay(350)
            val result = repository.login(login.email, login.password)
            login = if (result is AuthResult.Success) login.copy(loading = false)
            else login.copy(loading = false, authError = "Correo o contraseña incorrectos.")
        }
    }

    // ---------- Registro ----------
    fun onRegName(v: String) { register = register.copy(name = v, nameError = null) }
    fun onRegEmail(v: String) { register = register.copy(email = v, emailError = null) }
    fun onRegPassword(v: String) { register = register.copy(password = v, passwordError = null) }
    fun toggleRegPassword() { register = register.copy(passwordVisible = !register.passwordVisible) }
    fun onRegTerms(v: Boolean) { register = register.copy(acceptTerms = v, termsError = null) }

    fun submitRegister() {
        val s = register
        val updated = s.copy(
            nameError = Validators.name(s.name),
            emailError = Validators.email(s.email),
            passwordError = Validators.password(s.password),
            termsError = Validators.acceptTerms(s.acceptTerms)
        )
        register = updated
        val hasErrors = listOf(
            updated.nameError, updated.emailError, updated.passwordError, updated.termsError
        ).any { it != null }
        if (hasErrors) return

        register = register.copy(loading = true)
        viewModelScope.launch {
            delay(350)
            val result = repository.register(s.name, s.email, "", s.password)
            register = if (result is AuthResult.EmailAlreadyRegistered)
                register.copy(loading = false, emailError = "Este correo ya está registrado.")
            else register.copy(loading = false)
        }
    }
}
