package com.example.mastermartini.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mastermartini.data.local.UserEntity
import com.example.mastermartini.model.ContentCatalog
import com.example.mastermartini.model.ContentItem
import com.example.mastermartini.model.ContentType
import com.example.mastermartini.ui.components.BrandLogo
import com.example.mastermartini.ui.components.ContentCard
import com.example.mastermartini.ui.components.SectionHeader
import com.example.mastermartini.ui.components.icon
import com.example.mastermartini.ui.screens.profile.ProfileScreen
import com.example.mastermartini.ui.theme.MasterMartiniTheme

private fun ContentType.hint() = when (this) {
    ContentType.Recipe -> "Preparaciones paso a paso"
    ContentType.Video -> "Técnicas explicadas"
    ContentType.Course -> "Lecciones de especialidad"
}

/** Inicio simplificado: saludo, tres accesos grandes y unas pocas sugerencias. */
@Composable
fun HomeScreen(
    user: UserEntity?,
    favoriteIds: Set<String>,
    onOpenType: (ContentType) -> Unit,
    onOpenContent: (ContentItem) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onCompleteProfile: () -> Unit
) {
    val firstName = user?.name?.trim()?.substringBefore(" ").orEmpty().ifBlank { "Chef" }
    val suggestions = ContentCatalog.items
        .filter { item -> user == null || user.interestList.isEmpty() || user.interestList.any { it.equals(item.category, true) } }
        .ifEmpty { ContentCatalog.items }
        .take(3)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { BrandLogo(Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally).width(170.dp).height(57.dp)) }
        item {
            Column {
                Text("Hola, $firstName", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                Text("¿Qué quieres ver hoy?", style = MaterialTheme.typography.bodyLarge)
            }
        }
        items(ContentType.entries) { type ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable(role = Role.Button) { onOpenType(type) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(type.icon(), null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(34.dp))
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(type.label, style = MaterialTheme.typography.titleMedium)
                        Text(type.hint(), style = MaterialTheme.typography.bodyMedium)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
        if (user != null && !user.isProfileComplete) {
            item {
                TextButton(onClick = onCompleteProfile) {
                    Text("Completa tu perfil para recibir contenido a tu medida", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        item { SectionHeader("Para ti") }
        items(suggestions, key = { it.id }) { item ->
            ContentCard(
                item = item,
                isFavorite = item.id in favoriteIds,
                onClick = { onOpenContent(item) },
                onToggleFavorite = { onToggleFavorite(item.id) }
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(){
    MasterMartiniTheme {
        HomeScreen(
            user = null,
            favoriteIds = emptySet(),
            onOpenType = {},
            onOpenContent = {},
            onToggleFavorite = {},
            onCompleteProfile = {},
        )
    }
}
