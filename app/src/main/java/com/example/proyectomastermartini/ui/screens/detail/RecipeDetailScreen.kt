package com.example.mastermartini.ui.screens.detail

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.mastermartini.model.ContentCatalog
import com.example.mastermartini.model.ContentItem
import com.example.mastermartini.ui.components.AppTopBar
import com.example.mastermartini.ui.components.EmptyState
import com.example.mastermartini.ui.components.FavoriteButton
import com.example.mastermartini.ui.components.SectionHeader
import com.example.mastermartini.ui.components.TypeBadge

@Composable
fun RecipeDetailScreen(
    recipe: ContentItem?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenVideo: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar("Receta", onBack)
        if (recipe == null) {
            EmptyState(Icons.Filled.OndemandVideo, "Receta no encontrada", "El contenido que buscas ya no está disponible.")
            return@Column
        }
        val context = LocalContext.current
        // Ingredientes marcados (se conservan al rotar la pantalla)
        var checkedRaw by rememberSaveable { mutableStateOf("") }
        val checked = checkedRaw.split("|").filter { it.isNotEmpty() }.toSet()

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            Image(
                painter = painterResource(recipe.imageRes),
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        TypeBadge(recipe.type)
                        Text(recipe.title, style = MaterialTheme.typography.headlineMedium)
                        Text(recipe.meta, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    FavoriteButton(isFavorite, onToggleFavorite)
                    // Recurso del sistema: compartir con otras apps
                    IconButton(onClick = {
                        val send = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, recipe.title)
                            putExtra(Intent.EXTRA_TEXT, "Mira esta receta de Master Martini: ${recipe.title}. ${recipe.description}")
                        }
                        context.startActivity(Intent.createChooser(send, "Compartir receta"))
                    }) { Icon(Icons.Filled.Share, contentDescription = "Compartir receta") }
                }
                Text(recipe.description, style = MaterialTheme.typography.bodyLarge)
                HorizontalDivider()

                SectionHeader("Ingredientes", subtitle = "Marca lo que ya tienes")
                recipe.ingredients.forEach { ingredient ->
                    val done = ingredient.name in checked
                    val color by animateColorAsState(
                        if (done) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
                        label = "ingredientColor"
                    )
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = done,
                            onCheckedChange = {
                                val next = if (done) checked - ingredient.name else checked + ingredient.name
                                checkedRaw = next.joinToString("|")
                            }
                        )
                        Text(
                            ingredient.name,
                            modifier = Modifier.weight(1f),
                            color = color,
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = if (done) TextDecoration.LineThrough else null
                        )
                        Text(ingredient.amount, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
                HorizontalDivider()

                SectionHeader("Preparación")
                recipe.steps.forEachIndexed { index, step ->
                    Row(Modifier.animateContentSize(), verticalAlignment = Alignment.Top) {
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                            Box(Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                                Text("${index + 1}", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(step, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    }
                }

                val related = recipe.relatedVideoId?.let { ContentCatalog.find(it) }
                if (related != null) {
                    HorizontalDivider()
                    OutlinedButton(
                        onClick = { onOpenVideo(related.id) },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Icon(Icons.Filled.OndemandVideo, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Ver video técnico: ${related.title}", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
