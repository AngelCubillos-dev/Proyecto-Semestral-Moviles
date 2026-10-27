package com.example.mastermartini.model

import androidx.annotation.DrawableRes

enum class ContentType(val label: String, val badge: String) {
    Recipe("Recetas", "RECETA"),
    Video("Videos", "VIDEO TÉCNICO"),
    Course("Cursos", "CURSO")
}

data class Ingredient(val name: String, val amount: String)

/**
 * Contenido del catálogo (receta, video o curso).
 *
 * - [imageRes]: imagen de portada (res/drawable).
 * - [rawVideoName]: nombre del archivo de video en res/raw SIN extensión. Si el
 *   archivo no existe, la app muestra un reproductor simulado.
 * - [lessonIds]: para cursos, ids de los videos que forman las lecciones.
 */
data class ContentItem(
    val id: String,
    val type: ContentType,
    val category: String,
    val title: String,
    val description: String,
    val meta: String,
    @DrawableRes val imageRes: Int,
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<String> = emptyList(),
    val lessonIds: List<String> = emptyList(),
    val relatedVideoId: String? = null,
    val rawVideoName: String? = null,
    val level: String = ""
)
