package com.capstone.aksaintar.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detection : Screen("detection")
    object Upload : Screen("upload")
    object Color : Screen("color")
    object Login : Screen("login")
}