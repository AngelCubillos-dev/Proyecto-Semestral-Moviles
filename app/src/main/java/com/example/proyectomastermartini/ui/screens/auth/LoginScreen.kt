package com.example.mastermartini.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mastermartini.ui.components.AppTextField
import com.example.mastermartini.ui.components.BrandLogo
import com.example.mastermartini.ui.components.PasswordField
import com.example.mastermartini.ui.components.PrimaryButton
import com.example.mastermartini.viewmodel.AuthViewModel

/** Ingreso simplificado: logo, dos campos y un botón. Entra con un suave fundido. */
@Composable
fun LoginScreen(onGoToRegister: () -> Unit, viewModel: AuthViewModel = viewModel()) {
    val state = viewModel.login
    val focus = LocalFocusManager.current

    val fade = remember { Animatable(0f) }
    LaunchedEffect(Unit) { fade.animateTo(1f, tween(500)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 28.dp, vertical = 48.dp)
            .graphicsLayer { alpha = fade.value },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        BrandLogo(Modifier.width(260.dp).height(87.dp))
        Spacer(Modifier.height(20.dp))
        Text("Ingresa a tu cuenta", style = MaterialTheme.typography.titleLarge)

        state.authError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        AppTextField(
            value = state.email,
            onValueChange = viewModel::onLoginEmail,
            label = "Correo",
            leadingIcon = Icons.Filled.Email,
            error = state.emailError,
            keyboardType = KeyboardType.Email
        )
        PasswordField(
            value = state.password,
            onValueChange = viewModel::onLoginPassword,
            label = "Contraseña",
            visible = state.passwordVisible,
            onToggleVisibility = viewModel::toggleLoginPassword,
            error = state.passwordError,
            imeAction = ImeAction.Done,
            onDone = { focus.clearFocus(); viewModel.submitLogin() }
        )
        PrimaryButton(
            text = "Entrar",
            onClick = { focus.clearFocus(); viewModel.submitLogin() },
            loading = state.loading
        )
        TextButton(onClick = onGoToRegister) {
            Text("Crear cuenta nueva", style = MaterialTheme.typography.labelLarge)
        }
    }
}
