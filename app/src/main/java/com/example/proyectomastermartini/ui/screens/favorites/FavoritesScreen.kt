package com.example.mastermartini.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mastermartini.model.ContentItem
import com.example.mastermartini.ui.components.AppTopBar
import com.example.mastermartini.ui.components.ContentCard
import com.example.mastermartini.ui.components.EmptyState
import com.example.mastermartini.ui.screens.profile.ProfileScreen
import com.example.mastermartini.ui.theme.MasterMartiniTheme

@Composable
fun FavoritesScreen(
    favorites: List<ContentItem>,
    onOpenContent: (ContentItem) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onGoExplore: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar("Mis favoritos")
        if (favorites.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.FavoriteBorder,
                title = "Aún no tienes favoritos",
                message = "Toca el corazón en una receta, video o curso para guardarlo aquí.",
                action = { TextButton(onClick = onGoExplore) { Text("Explorar contenido") } }
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites, key = { it.id }) { item ->
                    ContentCard(
                        item = item,
                        isFavorite = true,
                        onClick = { onOpenContent(item) },
                        onToggleFavorite = { onToggleFavorite(item.id) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoritesScreenEmptyPreview(){
    MasterMartiniTheme {
        FavoritesScreen(
            favorites = emptyList(),
            onOpenContent = {},
            onToggleFavorite = {},
            onGoExplore = {},
        )
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoritesScreenWithDataPreview(){
    MasterMartiniTheme {
        FavoritesScreen(
            favorites = listOf(),
            onOpenContent = {},
            onToggleFavorite = {},
            onGoExplore = {},
        )
    }
}



