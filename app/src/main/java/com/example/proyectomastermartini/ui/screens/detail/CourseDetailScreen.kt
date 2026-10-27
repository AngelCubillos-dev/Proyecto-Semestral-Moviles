package com.example.mastermartini.ui.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mastermartini.model.ContentCatalog
import com.example.mastermartini.model.ContentItem
import com.example.mastermartini.ui.components.AppTopBar
import com.example.mastermartini.ui.components.EmptyState
import com.example.mastermartini.ui.components.FavoriteButton
import com.example.mastermartini.ui.components.SectionHeader
import com.example.mastermartini.ui.components.TypeBadge

@Composable
fun CourseDetailScreen(
    course: ContentItem?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenLesson: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AppTopBar("Curso", onBack)
        if (course == null) {
            EmptyState(Icons.Filled.School, "Curso no encontrado", "El contenido que buscas ya no está disponible.")
            return@Column
        }
        val lessons = ContentCatalog.lessonsOf(course)
        var expandedId by rememberSaveable { mutableStateOf<String?>(null) }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
            Image(
                painter = painterResource(course.imageRes),
                contentDescription = course.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        TypeBadge(course.type)
                        Text(course.title, style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "${course.level} · ${course.meta}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    FavoriteButton(isFavorite, onToggleFavorite)
                }
                Text(course.description, style = MaterialTheme.typography.bodyLarge)
                SectionHeader("Lecciones", subtitle = "Toca una lección para ver el detalle")

                lessons.forEachIndexed { index, lesson ->
                    val expanded = expandedId == lesson.id
                    val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "chevron")
                    Card(
                        modifier = Modifier.fillMaxWidth().animateContentSize(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.clickable { expandedId = if (expanded) null else lesson.id }.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${index + 1}.", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                                    Text(lesson.meta, style = MaterialTheme.typography.bodySmall)
                                }
                                Icon(Icons.Filled.ExpandMore, contentDescription = if (expanded) "Contraer" else "Expandir", modifier = Modifier.rotate(rotation))
                            }
                            AnimatedVisibility(visible = expanded) {
                                Column(Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(lesson.description, style = MaterialTheme.typography.bodyMedium)
                                    androidx.compose.material3.Button(
                                        onClick = { onOpenLesson(lesson.id) },
                                        modifier = Modifier.fillMaxWidth().height(52.dp)
                                    ) {
                                        Icon(Icons.Filled.PlayCircle, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Ver lección", style = MaterialTheme.typography.labelLarge)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
