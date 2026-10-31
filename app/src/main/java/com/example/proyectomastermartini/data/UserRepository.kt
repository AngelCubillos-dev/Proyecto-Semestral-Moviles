package com.example.proyectomastermartini.data

import com.example.proyectomastermartini.util.PasswordHasher
import kotlinx.coroutines.flow.Flow

sealed interface AuthResult {
    data class Success(val userId: Long) : AuthResult
    data object EmailAlreadyRegistered : AuthResult
    data object InvalidCredentials : AuthResult
}

class UserRepository(
    private val userDao: UserDao,
    private val session: SessionManager
) {
    suspend fun register(name: String, email: String, phone: String, password: String): AuthResult {
        val normalized = email.trim().lowercase()
        if (userDao.findByEmail(normalized) != null) return AuthResult.EmailAlreadyRegistered

        val salt = PasswordHasher.newSalt()
        val id = userDao.insert(
            UserEntity(
                name = name.trim(),
                email = normalized,
                phone = phone.trim(),
                passwordHash = PasswordHasher.hash(password, salt),
                salt = salt
            )
        )
        session.startSession(id)
        return AuthResult.Success(id)
    }

    suspend fun login(email: String, password: String): AuthResult {
        val user = userDao.findByEmail(email.trim().lowercase()) ?: return AuthResult.InvalidCredentials
        return if (PasswordHasher.hash(password, user.salt) == user.passwordHash) {
            session.startSession(user.id)
            AuthResult.Success(user.id)
        } else {
            AuthResult.InvalidCredentials
        }
    }

    fun logout() = session.endSession()

    fun observeUser(id: Long): Flow<UserEntity?> = userDao.observeById(id)

    suspend fun getUser(id: Long): UserEntity? = userDao.findById(id)

    suspend fun updateUser(user: UserEntity) = userDao.update(user)
}
