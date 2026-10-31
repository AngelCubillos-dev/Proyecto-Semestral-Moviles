package com.example.proyectomastermartini.ui.screens.contact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomastermartini.domain.Validators
import com.example.proyectomastermartini.model.ContentCatalog
import com.example.proyectomastermartini.ui.components.AppTextField
import com.example.proyectomastermartini.ui.components.AppTopBar
import com.example.proyectomastermartini.ui.components.DropdownField
import com.example.proyectomastermartini.ui.components.PrimaryButton
import com.example.proyectomastermartini.ui.components.SectionHeader
import com.example.proyectomastermartini.ui.components.SuccessDialog
import com.example.proyectomastermartini.viewmodel.ContactViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ContactScreen(viewModel: ContactViewModel = viewModel()) {
    val state = viewModel.state
    val history by viewModel.history.collectAsState()
    val focus = LocalFocusManager.current

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Contacto")
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Escríbenos para recibir información de productos, cursos o eventos. Nuestro equipo de marketing te responderá.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTextField(state.name, viewModel::onName, "Nombre", Icons.Filled.Person, error = state.nameError)
            AppTextField(
                state.email, viewModel::onEmail, "Correo electrónico", Icons.Filled.Email,
                error = state.emailError, keyboardType = KeyboardType.Email
            )
            AppTextField(
                state.phone, viewModel::onPhone, "Celular", Icons.Filled.Phone,
                error = state.phoneError, keyboardType = KeyboardType.Phone
            )
            DropdownField(
                value = state.topic,
                onSelected = viewModel::onTopic,
                options = ContentCatalog.contactTopics,
                label = "Motivo de la consulta",
                leadingIcon = Icons.Filled.Category,
                error = state.topicError
            )
            AppTextField(state.subject, viewModel::onSubject, "Asunto", Icons.Filled.Edit, error = state.subjectError)
            AppTextField(
                value = state.message,
                onValueChange = viewModel::onMessage,
                label = "Mensaje",
                leadingIcon = Icons.Filled.ChatBubble,
                error = state.messageError,
                hint = "${state.message.trim().length}/${Validators.MESSAGE_MAX} caracteres",
                singleLine = false,
                minLines = 4,
                imeAction = ImeAction.Default
            )
            PrimaryButton(
                text = "Enviar consulta",
                onClick = { focus.clearFocus(); viewModel.send() },
                loading = state.sending,
                leadingIcon = Icons.AutoMirrored.Filled.Send
            )

            AnimatedVisibility(
                visible = history.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 12.dp)) {
                    SectionHeader("Consultas enviadas", subtitle = "Guardadas en tu dispositivo")
                    history.forEach { msg ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(msg.subject, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                                }
                                Text(
                                    "${msg.topic} · " + SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")).format(Date(msg.createdAt)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(msg.message, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.sent) {
        SuccessDialog(
            title = "Consulta enviada",
            message = "Guardamos tu mensaje. Un ejecutivo de Master Martini se pondrá en contacto contigo (simulado en este prototipo).",
            onDismiss = viewModel::dismissSent
        )
    }
}
