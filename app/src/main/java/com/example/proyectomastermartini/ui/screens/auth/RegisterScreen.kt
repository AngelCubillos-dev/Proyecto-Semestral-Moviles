package com.example.mastermartini.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mastermartini.ui.components.AppTextField
import com.example.mastermartini.ui.components.AppTopBar
import com.example.mastermartini.ui.components.FieldMessage
import com.example.mastermartini.ui.components.PasswordField
import com.example.mastermartini.ui.components.PrimaryButton
import com.example.mastermartini.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(onBack: () -> Unit, viewModel: AuthViewModel = viewModel()) {
    val state = viewModel.register
    val focus = LocalFocusManager.current

    Scaffold(topBar = { AppTopBar("Crear cuenta", onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Solo necesitamos tres datos. Usa información de ejemplo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTextField(
                value = state.name, onValueChange = viewModel::onRegName,
                label = "Nombre completo", leadingIcon = Icons.Filled.Person, error = state.nameError
            )
            AppTextField(
                value = state.email, onValueChange = viewModel::onRegEmail,
                label = "Correo electrónico", leadingIcon = Icons.Filled.Email,
                error = state.emailError, keyboardType = KeyboardType.Email
            )
            PasswordField(
                value = state.password, onValueChange = viewModel::onRegPassword,
                label = "Contraseña", visible = state.passwordVisible,
                onToggleVisibility = viewModel::toggleRegPassword,
                error = state.passwordError, hint = "Mínimo 8 caracteres, con letras y números",
                imeAction = ImeAction.Done, onDone = { focus.clearFocus() }
            )

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = state.acceptTerms, onCheckedChange = viewModel::onRegTerms)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Acepto el uso de mis datos ficticios para fines académicos.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (state.termsError != null) {
                    Box(Modifier.padding(start = 12.dp)) { FieldMessage(state.termsError) }
                }
            }

            Spacer(Modifier.height(4.dp))
            PrimaryButton(
                text = "Crear cuenta",
                onClick = { focus.clearFocus(); viewModel.submitRegister() },
                loading = state.loading
            )
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Ya tengo cuenta", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
