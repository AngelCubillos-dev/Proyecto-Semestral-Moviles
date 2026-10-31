package com.example.proyectomastermartini.data

import kotlinx.coroutines.flow.Flow

class ContactRepository(private val dao: ContactDao) {
    fun observeByUser(userId: Long): Flow<List<ContactMessageEntity>> = dao.observeByUser(userId)
    suspend fun send(message: ContactMessageEntity): Long = dao.insert(message)
}
