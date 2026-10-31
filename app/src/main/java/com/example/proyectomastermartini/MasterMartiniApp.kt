package com.example.proyectomastermartini

import android.app.Application
import com.example.proyectomastermartini.data.AppDatabase
import com.example.proyectomastermartini.data.ContactRepository
import com.example.proyectomastermartini.data.FavoriteRepository
import com.example.proyectomastermartini.data.UserRepository
import com.example.proyectomastermartini.data.SessionManager

/** Contenedor simple de dependencias (evita agregar Hilt en un MVP). */
class MasterMartiniApp : Application() {
    val session by lazy { SessionManager(this) }
    private val database by lazy { AppDatabase.get(this) }
    val userRepository by lazy { UserRepository(database.userDao(), session) }
    val favoriteRepository by lazy { FavoriteRepository(database.favoriteDao()) }
    val contactRepository by lazy { ContactRepository(database.contactDao()) }
}
