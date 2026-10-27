package com.example.mastermartini.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val FAVORITES = "favorites"
    const val CONTACT = "contact"
    const val PROFILE = "profile"

    const val ARG_ID = "id"
    const val RECIPE = "recipe/{$ARG_ID}"
    const val COURSE = "course/{$ARG_ID}"
    const val VIDEO = "video/{$ARG_ID}"

    fun recipe(id: String) = "recipe/$id"
    fun course(id: String) = "course/$id"
    fun video(id: String) = "video/$id"

    val authRoutes = setOf(LOGIN, REGISTER)
    val mainRoutes = setOf(HOME, EXPLORE, FAVORITES, CONTACT, PROFILE)
}
