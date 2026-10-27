package com.example.mastermartini.ui.screens.explore

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.mastermartini.model.ContentItem
import com.example.mastermartini.model.ContentType
import com.example.mastermartini.ui.components.AppTopBar
import com.example.mastermartini.ui.components.ContentCard
import com.example.mastermartini.ui.components.EmptyState
import com.example.mastermartini.ui.theme.MasterMartiniTheme

@Composable
fun ExploreScreen(
    query: String,
    filter: ContentType?,
    results: List<ContentItem>,
    favoriteIds: Set<String>,
    onQueryChange: (String) -> Unit,
    onFilterChange: (ContentType?) -> Unit,
    onClear: () -> Unit,
    onOpenContent: (ContentItem) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val focus = LocalFocusManager.current
    Column(Modifier.fillMaxSize()) {
        AppTopBar("Explorar")
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            placeholder = { Text("Buscar por nombre o categoría") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Filled.Close, contentDescription = "Borrar búsqueda")
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focus.clearFocus() }),
            textStyle = MaterialTheme.typography.bodyLarge
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filter == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("Todo", style = MaterialTheme.typography.bodyMedium) }
                )
            }
            items(ContentType.entries.toList()) { type ->
                FilterChip(
                    selected = filter == type,
                    onClick = { onFilterChange(if (filter == type) null else type) },
                    label = { Text(type.label, style = MaterialTheme.typography.bodyMedium) }
                )
            }
        }
        Text(
            "${results.size} resultado(s)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        AnimatedContent(targetState = results.isEmpty(), label = "exploreContent", modifier = Modifier.weight(1f)) { empty ->
            if (empty) {
                EmptyState(
                    icon = Icons.Filled.SearchOff,
                    title = "Sin resultados",
                    message = "No encontramos contenido con esa búsqueda. Prueba con otra palabra o quita los filtros.",
                    action = { androidx.compose.material3.TextButton(onClick = onClear) { Text("Limpiar búsqueda", style = MaterialTheme.typography.labelLarge) } }
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(results, key = { it.id }) { item ->
                        ContentCard(
                            item = item,
                            isFavorite = item.id in favoriteIds,
                            onClick = { onOpenContent(item) },
                            onToggleFavorite = { onToggleFavorite(item.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExploreScreenPreview() {
    MasterMartiniTheme {
        ExploreScreen(
            query = "",
            filter = null,
            results = emptyList(),
            favoriteIds = emptySet(),
            onQueryChange = {},
            onFilterChange = {},
            onClear = {},
            onOpenContent = {},
            onToggleFavorite = {}
        )
    }
}
