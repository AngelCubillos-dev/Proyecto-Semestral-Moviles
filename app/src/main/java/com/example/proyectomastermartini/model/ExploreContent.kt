package com.example.proyectomastermartini.model

import androidx.annotation.DrawableRes

data class ExploreContent(
    val eyebrow : String,
    val title:String,
    val metadata: String,
    val type: ContentFilter,
    @DrawableRes val imageRes: Int,
    val id: String = title.lowercase().replace(" ","-")


)

