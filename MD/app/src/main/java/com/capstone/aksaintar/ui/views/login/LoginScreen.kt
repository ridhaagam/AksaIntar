package com.capstone.aksaintar.ui.views.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.aksaintar.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

private const val RC_SIGN_IN = 123
private const val TAG = "LoginScreen"


@Composable
fun LoginScreen(
    onSignIn: (GoogleSignInAccount?) -> Unit,
    navigateToHomeScreen: (String) -> Unit,
    startGoogleSignIn: () -> Unit
) {



    Column(
        modifier = Modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .height(416.dp)
                .width(416.dp)
        )
        Spacer(modifier = Modifier.height(100.dp))
        OutlinedButton(
            border = BorderStroke(2.dp, colors.primary),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            onClick = {
                      startGoogleSignIn()

            },
            modifier = Modifier
                .width(338.dp)
                .height(56.dp)
        ,
        ) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)

                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Sign in with Google", fontWeight = FontWeight.Bold)

            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = {  navigateToHomeScreen("Guest") }
        ) {
            Text(text = "Continue as Guest", fontWeight = FontWeight.Bold, color = Color.Black)
            
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginScreen(
        onSignIn = {},
        navigateToHomeScreen = {},
        startGoogleSignIn = {}
    )
}
