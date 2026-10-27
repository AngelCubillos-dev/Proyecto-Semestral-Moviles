package com.example.mastermartini

import android.app.Application
import com.example.mastermartini.data.local.AppDatabase
import com.example.mastermartini.data.repository.ContactRepository
import com.example.mastermartini.data.repository.FavoriteRepository
import com.example.mastermartini.data.repository.UserRepository
import com.example.mastermartini.data.session.SessionManager

/** Contenedor simple de dependencias (evita agregar Hilt en un MVP). */
class MasterMartiniApp : Application() {
    val session by lazy { SessionManager(this) }
    private val database by lazy { AppDatabase.get(this) }
    val userRepository by lazy { UserRepository(database.userDao(), session) }
    val favoriteRepository by lazy { FavoriteRepository(database.favoriteDao()) }
    val contactRepository by lazy { ContactRepository(database.contactDao()) }
}
