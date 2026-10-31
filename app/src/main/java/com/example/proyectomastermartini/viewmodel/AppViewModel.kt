package com.example.proyectomastermartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomastermartini.MasterMartiniApp
import com.example.proyectomastermartini.data.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/** Estado global: sesión activa, usuario actual y preferencia de accesibilidad. */
@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MasterMartiniApp

    val userId: StateFlow<Long?> = app.session.userId
    val largeText: StateFlow<Boolean> = app.session.largeText

    val currentUser: StateFlow<UserEntity?> = userId
        .flatMapLatest { id -> if (id == null) flowOf(null) else app.userRepository.observeUser(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun logout() = app.userRepository.logout()

    fun setLargeText(enabled: Boolean) = app.session.setLargeText(enabled)
}
