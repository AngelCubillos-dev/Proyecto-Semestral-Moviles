package com.example.mastermartini.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermartini.MasterMartiniApp
import com.example.mastermartini.data.local.UserEntity
import com.example.mastermartini.domain.Validators
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ProfileUiState(
    val loaded: Boolean = false,
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val companyRut: String = "",
    val address: String = "",
    val businessType: String = "",
    val businessSize: String = "",
    val interests: Set<String> = emptySet(),
    val photoPath: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val nameError: String? = null,
    val phoneError: String? = null,
    val rutError: String? = null,
    val addressError: String? = null,
    val typeError: String? = null,
    val sizeError: String? = null,
    val interestsError: String? = null,
    val saving: Boolean = false,
    val saved: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MasterMartiniApp
    private val repository = app.userRepository
    private var original: UserEntity? = null

    var state by mutableStateOf(ProfileUiState())
        private set

    init {
        val id = app.session.userId.value
        if (id != null) {
            viewModelScope.launch {
                repository.getUser(id)?.let { user ->
                    original = user
                    state = ProfileUiState(
                        loaded = true,
                        email = user.email,
                        name = user.name,
                        phone = user.phone,
                        companyRut = user.companyRut,
                        address = user.address,
                        businessType = user.businessType,
                        businessSize = user.businessSize,
                        interests = user.interestList.toSet(),
                        photoPath = user.photoPath,
                        latitude = user.latitude,
                        longitude = user.longitude
                    )
                }
            }
        }
    }

    fun onName(v: String) { state = state.copy(name = v, nameError = null, saved = false) }
    fun onPhone(v: String) { state = state.copy(phone = v.filter { it.isDigit() || it == '+' }, phoneError = null, saved = false) }
    fun onRut(v: String) { state = state.copy(companyRut = v, rutError = null, saved = false) }
    fun onAddress(v: String) { state = state.copy(address = v, addressError = null, saved = false) }
    fun onBusinessType(v: String) { state = state.copy(businessType = v, typeError = null, saved = false) }
    fun onBusinessSize(v: String) { state = state.copy(businessSize = v, sizeError = null, saved = false) }

    fun toggleInterest(interest: String) {
        val next = if (interest in state.interests) state.interests - interest else state.interests + interest
        state = state.copy(interests = next, interestsError = null, saved = false)
    }

    /** Recurso nativo: cámara / galería. Se guarda de inmediato en la base local. */
    fun onPhotoChanged(path: String) {
        state = state.copy(photoPath = path)
        persist { it.copy(photoPath = path) }
    }

    /** Recurso nativo: ubicación del negocio. */
    fun onLocationChanged(lat: Double, lon: Double) {
        state = state.copy(latitude = lat, longitude = lon)
        persist { it.copy(latitude = lat, longitude = lon) }
    }

    fun save() {
        val s = state
        val updated = s.copy(
            nameError = Validators.name(s.name),
            phoneError = Validators.phone(s.phone),
            rutError = Validators.rutOptional(s.companyRut),
            addressError = Validators.addressOptional(s.address),
            typeError = Validators.required(s.businessType, "Selecciona el tipo de negocio."),
            sizeError = Validators.required(s.businessSize, "Selecciona el tamaño aproximado."),
            interestsError = Validators.interests(s.interests),
            saved = false
        )
        state = updated
        val hasErrors = listOf(
            updated.nameError, updated.phoneError, updated.rutError, updated.addressError,
            updated.typeError, updated.sizeError, updated.interestsError
        ).any { it != null }
        if (hasErrors) return

        state = state.copy(saving = true)
        viewModelScope.launch {
            delay(300)
            original?.let { user ->
                val newUser = user.copy(
                    name = s.name.trim(),
                    phone = s.phone.trim(),
                    companyRut = s.companyRut.trim(),
                    address = s.address.trim(),
                    businessType = s.businessType,
                    businessSize = s.businessSize,
                    interests = s.interests.joinToString(",")
                )
                repository.updateUser(newUser)
                original = newUser
            }
            state = state.copy(saving = false, saved = true)
        }
    }

    private fun persist(transform: (UserEntity) -> UserEntity) {
        val user = original ?: return
        val newUser = transform(user)
        original = newUser
        viewModelScope.launch { repository.updateUser(newUser) }
    }
}
