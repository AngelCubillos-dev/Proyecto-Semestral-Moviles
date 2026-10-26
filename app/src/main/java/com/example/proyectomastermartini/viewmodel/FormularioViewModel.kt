package com.example.proyectomastermartini.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectomastermartini.model.FormularioErrores
import com.example.proyectomastermartini.model.FormularioUIState

class FormularioViewModel : ViewModel() {

    var uiState by mutableStateOf(FormularioUIState())
        private set

    fun onNameChange(value: String) {
        uiState = uiState.copy(name = value, errors = uiState.errors.copy(name=null))
    }

    fun OnEmailChange(value: String){
        uiState = uiState.copy(email = value, errors = uiState.errors.copy(email = null))
    }

    fun  onPasswordChange(value: String){
        uiState = uiState.copy(
            password = value, errors = uiState.errors.copy(password = null,confirmPassword = null)
        )
    }

    fun onConfirmPasswordChange(value: String){
        uiState = uiState.copy(
            password = value, errors = uiState.errors.copy(confirmPassword = null)
        )
    }

    fun onPasswordVisibilityChange(visible: Boolean){
        uiState = uiState.copy(passwordVisible = visible)
    }

    fun onConfirmPasswordVisibilityChange(visible: Boolean){
        uiState = uiState.copy(confirmPasswordVisible = visible)
    }

    fun validateLogin(): Boolean{
        val errors = FormularioErrores(
            email = if (uiState.email.isValidEmail()) null else "Ingrese un correo valido",
            password =if(uiState.password.isNotBlank()) null else "Ingresa tu contraseña"
        )
        uiState = uiState.copy(errors = errors)
        return errors.email == null && errors.password == null
    }

    fun showLoginError(){
        uiState = uiState.copy(
            errors = uiState.errors.copy(password = "Email o contraseña incorrectos" )
        )
    }

    fun validateRegister(): Boolean{
        val errors = FormularioErrores(
            name = if(uiState.name.isNotBlank()) null else "Ingresa tu nombre",
            email = if (uiState.email.isValidEmail()) null else "Ingresa un email valido",
            password =if(uiState.password.isNotBlank()) null else "Ingresa tu contraseña",
            confirmPassword = when{
                uiState.confirmPassword.isBlank() -> "Confirma tu contraseña"
                uiState.confirmPassword != uiState.password -> "Las contraseñas no deben ser iguales"
                else -> null
            }
        )
        uiState = uiState.copy(errors= errors)
        return errors.name == null && errors.email == null &&
                errors.password == null && errors.confirmPassword == null
    }

    fun showEmailAlreadyRegisterError(){
        uiState = uiState.copy(
            errors= uiState.errors.copy(email="Este correo ya esta registrado")
        )
    }

    private fun String.isValidEmail(): Boolean{
        return isNotBlank() && contains ("@") && substringAfter("@").contains(".")
    }
}