package com.example.proyectomastermartini.model

import  androidx.annotation.DrawableRes
data class Recipe(
    val id: String,
    val category: String,
    val title : String,
    val description: String,
    val duration: String,
    val yield: String,
    @DrawableRes val imagesRes: Int,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val relatedVideo: ExploreContent? =null

)

data class Ingredient(
    val name: String,
    val amount: String
)
