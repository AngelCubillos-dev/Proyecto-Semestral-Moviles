package com.example.proyectomastermartini.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomastermartini.MasterMartiniApp
import com.example.proyectomastermartini.data.ContactMessageEntity
import com.example.proyectomastermartini.domain.Validators
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ContactUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val topic: String = "",
    val subject: String = "",
    val message: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val topicError: String? = null,
    val subjectError: String? = null,
    val messageError: String? = null,
    val sending: Boolean = false,
    val sent: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MasterMartiniApp
    private val repository = app.contactRepository

    var state by mutableStateOf(ContactUiState())
        private set

    val history: StateFlow<List<ContactMessageEntity>> = app.session.userId
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else repository.observeByUser(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // Precarga los datos del usuario (ficticios) para ahorrar escritura.
        val id = app.session.userId.value
        if (id != null) {
            viewModelScope.launch {
                app.userRepository.getUser(id)?.let { u ->
                    state = state.copy(name = u.name, email = u.email, phone = u.phone)
                }
            }
        }
    }

    fun onName(v: String) { state = state.copy(name = v, nameError = null) }
    fun onEmail(v: String) { state = state.copy(email = v, emailError = null) }
    fun onPhone(v: String) { state = state.copy(phone = v.filter { it.isDigit() || it == '+' }, phoneError = null) }
    fun onTopic(v: String) { state = state.copy(topic = v, topicError = null) }
    fun onSubject(v: String) { state = state.copy(subject = v, subjectError = null) }
    fun onMessage(v: String) { state = state.copy(message = v.take(Validators.MESSAGE_MAX + 20), messageError = null) }
    fun dismissSent() { state = state.copy(sent = false) }

    fun send() {
        val s = state
        val updated = s.copy(
            nameError = Validators.name(s.name),
            emailError = Validators.email(s.email),
            phoneError = Validators.phone(s.phone),
            topicError = Validators.required(s.topic, "Selecciona el motivo de tu consulta."),
            subjectError = Validators.subject(s.subject),
            messageError = Validators.message(s.message)
        )
        state = updated
        val hasErrors = listOf(
            updated.nameError, updated.emailError, updated.phoneError,
            updated.topicError, updated.subjectError, updated.messageError
        ).any { it != null }
        if (hasErrors) return

        val userId = app.session.userId.value ?: return
        state = state.copy(sending = true)
        viewModelScope.launch {
            delay(600) // simula el envío; el MVP no se conecta a un servicio real
            repository.send(
                ContactMessageEntity(
                    userId = userId,
                    name = s.name.trim(), email = s.email.trim(), phone = s.phone.trim(),
                    topic = s.topic, subject = s.subject.trim(), message = s.message.trim()
                )
            )
            state = state.copy(sending = false, sent = true, topic = "", subject = "", message = "")
        }
    }
}
