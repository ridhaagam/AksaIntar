package com.capstone.aksaintar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.aksaintar.ui.navigation.Screen
import com.capstone.aksaintar.ui.views.color.ColorScreen
import com.capstone.aksaintar.ui.views.contributor.UploadScreen
import com.capstone.aksaintar.ui.views.detection.ImagePicker
import com.capstone.aksaintar.ui.views.home.HomeScreen
import com.capstone.aksaintar.ui.views.login.LoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.StateFlow


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
            email.value = account?.displayName
                ?: "Tamu"
            navController.navigate(Screen.Home.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        } else {
            email.value = "Tamu"
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = false
                }
                restoreState = false
                launchSingleTop = true
            }
        }
    }

    Scaffold(
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
                        }
                    },
                    navToUploadScreen = {

                        navController.navigate(Screen.Upload.route) {
                        }
                    },
                    navToColorScreen = {

                        navController.navigate(Screen.Color.route) {
                        }
                    },
                    signOut = {
                        signOut(context)
                    }

                )
            }
            composable(Screen.Detection.route) {
                ImagePicker(navController = navController)
            }
            composable(Screen.Upload.route) {
                UploadScreen(navController = navController)
            }
            composable(Screen.Color.route) {
                ColorScreen(navController = navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    onSignIn = {},
                    navigateToHomeScreen = { email ->
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

fun signOut(context: Context) {
    val activity = context as Activity

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    googleSignInClient.signOut().addOnCompleteListener {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            val intent = Intent(activity, activity::class.java)
            activity.finish()
            activity.startActivity(intent)
        }
    }
}



