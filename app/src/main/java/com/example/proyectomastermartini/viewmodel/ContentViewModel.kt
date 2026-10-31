package com.example.proyectomastermartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomastermartini.MasterMartiniApp
import com.example.proyectomastermartini.model.ContentCatalog
import com.example.proyectomastermartini.model.ContentItem
import com.example.proyectomastermartini.model.ContentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Búsqueda, filtros y favoritos (persistidos en Room). Compartido entre pantallas. */
@OptIn(ExperimentalCoroutinesApi::class)
class ContentViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MasterMartiniApp
    private val favoriteRepository = app.favoriteRepository

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _filter = MutableStateFlow<ContentType?>(null)
    val filter: StateFlow<ContentType?> = _filter.asStateFlow()

    val favoriteIds: StateFlow<Set<String>> = app.session.userId
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else favoriteRepository.observeIds(id) }
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    val favorites: StateFlow<List<ContentItem>> = favoriteIds
        .map { ids -> ids.mapNotNull { ContentCatalog.find(it) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val results: StateFlow<List<ContentItem>> = combine(_query, _filter) { q, f ->
        val text = q.trim()
        ContentCatalog.items.filter { item ->
            (f == null || item.type == f) &&
                (text.isEmpty() ||
                    item.title.contains(text, ignoreCase = true) ||
                    item.category.contains(text, ignoreCase = true) ||
                    item.description.contains(text, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ContentCatalog.items)

    fun onQueryChange(value: String) { _query.value = value }
    fun onFilterChange(value: ContentType?) { _filter.value = value }
    fun clearSearch() { _query.value = ""; _filter.value = null }

    fun toggleFavorite(contentId: String) {
        val userId = app.session.userId.value ?: return
        val isFavorite = contentId in favoriteIds.value
        viewModelScope.launch { favoriteRepository.toggle(userId, contentId, isFavorite) }
    }
}
