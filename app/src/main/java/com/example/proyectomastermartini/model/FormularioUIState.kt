package com.example.proyectomastermartini.model

data class FormularioUIState(
    val name: String ="",
    val email: String ="",
    val password: String ="",
    val confirmPassword: String="",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean= false,
    val errors : FormularioErrores = FormularioErrores()
)
