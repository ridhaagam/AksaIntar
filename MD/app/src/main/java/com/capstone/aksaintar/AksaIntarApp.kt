package com.capstone.aksaintar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.aksaintar.ui.navigation.NavigationItem
import com.capstone.aksaintar.ui.navigation.Screen
import com.capstone.aksaintar.ui.views.detection.ImagePicker
import com.capstone.aksaintar.ui.views.home.HomeScreen
import com.capstone.aksaintar.ui.views.login.LoginScreen
import com.capstone.aksaintar.ui.views.profile.ProfileScreen
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.StateFlow


@Composable
fun AksaIntarApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startGoogleSignIn: () -> Unit,
    googleAccount: StateFlow<GoogleSignInAccount?>
) {
    val account by googleAccount.collectAsState(initial = null)
    val signedInAccount by rememberSaveable { mutableStateOf<GoogleSignInAccount?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LaunchedEffect(account) {
        if (account != null) {
            navController.navigate(Screen.Home.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detection.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    email = account?.displayName,

                    navToCameraScreen = {
                        println(account?.displayName)
                        navController.navigate(Screen.Detection.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Detection.route) {
                ImagePicker()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Login.route){
                LoginScreen(
                    onSignIn = {},
                    navigateToHomeScreen = { // <-- fungsi navigateToHomeScreen yang diteruskan ke LoginScreen
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    startGoogleSignIn = startGoogleSignIn
                )
            }

        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home,
                contentDescription = stringResource(R.string.menu_home)
            ),


            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile,
                contentDescription = stringResource(R.string.menu_profile)
            ),
        )
        BottomNavigation {
            navigationItems.map { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription
                        )
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun BottomBarPreview() {
    BottomBar(navController = rememberNavController())
}