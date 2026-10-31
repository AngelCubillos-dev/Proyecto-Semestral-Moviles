package com.example.proyectomastermartini.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Usuario registrado. Todos los datos del MVP son ficticios o sintéticos. */
@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val passwordHash: String,
    val salt: String,
    val businessType: String = "",
    val businessSize: String = "",
    val companyRut: String = "",
    val address: String = "",
    /** Intereses separados por coma (ej: "Chocolatería,Galletas"). */
    val interests: String = "",
    val photoPath: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val interestList: List<String>
        get() = interests.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    val isProfileComplete: Boolean
        get() = businessType.isNotBlank() && businessSize.isNotBlank() && interestList.isNotEmpty()
}

/** Contenido marcado como favorito por un usuario. */
@Entity(tableName = "favorites", primaryKeys = ["userId", "contentId"])
data class FavoriteEntity(
    val userId: Long,
    val contentId: String,
    val addedAt: Long = System.currentTimeMillis()
)

/** Consulta enviada desde el formulario de contacto (queda guardada localmente). */
@Entity(tableName = "contact_messages")
data class ContactMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val name: String,
    val email: String,
    val phone: String,
    val topic: String,
    val subject: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)
