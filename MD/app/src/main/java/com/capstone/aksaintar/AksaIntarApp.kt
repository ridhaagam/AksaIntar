package com.capstone.aksaintar

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.capstone.aksaintar.ui.views.color.ColorScreen
import com.capstone.aksaintar.ui.views.contributor.UploadScreen
import com.capstone.aksaintar.ui.views.detection.ImagePicker
import com.capstone.aksaintar.ui.views.home.HomeScreen
import com.capstone.aksaintar.ui.views.login.LoginScreen
import com.capstone.aksaintar.ui.views.profile.ProfileScreen
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.StateFlow
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AksaIntarApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startGoogleSignIn: () -> Unit,
    googleAccount: StateFlow<GoogleSignInAccount?>,
    context: Context
) {
    val account by googleAccount.collectAsState(initial = null)
    val signedInAccount by rememberSaveable { mutableStateOf<GoogleSignInAccount?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val email = rememberSaveable { mutableStateOf<String?>(null) }


    BackHandler(enabled = currentRoute != Screen.Login.route) {
        navController.popBackStack()
    }
    LaunchedEffect(account) {
        if (account != null) {
            email.value = account?.displayName ?: "Guest" // Update nilai email saat pengguna sign in dengan akun Google
            navController.navigate(Screen.Home.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        } else {
            email.value = "Guest" // Set nilai email sebagai "Guest" saat pengguna sign out
        }
    }

    Scaffold(
//        bottomBar = {
//            if (currentRoute != Screen.Detection.route) {
//                BottomBar(navController)
//            }
//        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    email = email,

                    navToCameraScreen = {
                        println(account?.displayName)
                        navController.navigate(Screen.Detection.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
                        }
                    },
                    navToUploadScreen = {

                        navController.navigate(Screen.Upload.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
                        }
                    },
                    navToColorScreen = {

                        navController.navigate(Screen.Color.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
                        }
                    },
                    signOut = {
                        signOut(context,navController)
                        // Clear data login dari OAuth dan lakukan sign out
//                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                            .requestEmail()
//                            .build()
//                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
//                        googleSignInClient.signOut().addOnCompleteListener {
//                            googleSignInClient.revokeAccess().addOnCompleteListener {
//                                navController.navigate(Screen.Login.route) {
//                                    popUpTo(Screen.Login.route) {
//                                        saveState = false
//                                    }
//                                    launchSingleTop = true
//                                }
//                            }
//                        }
                    }

                )
            }
            composable(Screen.Detection.route) {
                ImagePicker(navController = navController)
            }
            composable(Screen.Upload.route){
                UploadScreen(navController = navController)
            }
            composable(Screen.Color.route){
                ColorScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Login.route){
                LoginScreen(
                    onSignIn = {},
                    navigateToHomeScreen = { email -> // <-- fungsi navigateToHomeScreen yang diteruskan ke LoginScreen
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

fun signOut(context: Context, navController: NavHostController) {
    // Clear data login dari OAuth dan lakukan sign out
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    googleSignInClient.signOut().addOnCompleteListener {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Login.route) {
                    saveState = false
                }

                launchSingleTop = true
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

//@Composable
//@Preview
//private fun BottomBarPreview() {
//    BottomBar(navController = rememberNavController())
//}