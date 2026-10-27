package com.example.mastermartini.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mastermartini.model.ContentItem
import com.example.mastermartini.ui.components.AppTopBar
import com.example.mastermartini.ui.components.EmptyState
import com.example.mastermartini.ui.components.FavoriteButton
import com.example.mastermartini.ui.components.TypeBadge
import com.example.mastermartini.ui.components.VideoPlayer

@Composable
fun VideoDetailScreen(
    video: ContentItem?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar("Video técnico", onBack)
        if (video == null) {
            EmptyState(Icons.Filled.OndemandVideo, "Video no encontrado", "El contenido que buscas ya no está disponible.")
            return@Column
        }
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            VideoPlayer(video)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    TypeBadge(video.type)
                    Text(video.title, style = MaterialTheme.typography.headlineMedium)
                    Text(video.meta, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                FavoriteButton(isFavorite, onToggleFavorite)
            }
            Text(video.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
