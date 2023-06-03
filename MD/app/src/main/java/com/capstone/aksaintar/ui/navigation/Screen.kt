package com.capstone.aksaintar.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Profile : Screen("profile")
}