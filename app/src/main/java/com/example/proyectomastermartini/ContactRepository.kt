package com.example.mastermartini.data.repository

import com.example.mastermartini.data.local.ContactDao
import com.example.mastermartini.data.local.ContactMessageEntity
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val dao: ContactDao) {
    fun observeByUser(userId: Long): Flow<List<ContactMessageEntity>> = dao.observeByUser(userId)
    suspend fun send(message: ContactMessageEntity): Long = dao.insert(message)
}
