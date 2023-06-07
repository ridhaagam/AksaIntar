package com.capstone.aksaintar.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detection : Screen("detection")
    object Profile : Screen("profile")

    object Login : Screen("login")
}