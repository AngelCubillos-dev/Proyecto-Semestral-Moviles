package com.example.proyectomastermartini.data

import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val dao: FavoriteDao) {
    fun observeIds(userId: Long): Flow<List<String>> = dao.observeIds(userId)

    suspend fun toggle(userId: Long, contentId: String, currentlyFavorite: Boolean) {
        if (currentlyFavorite) dao.delete(userId, contentId)
        else dao.insert(FavoriteEntity(userId = userId, contentId = contentId))
    }
}
