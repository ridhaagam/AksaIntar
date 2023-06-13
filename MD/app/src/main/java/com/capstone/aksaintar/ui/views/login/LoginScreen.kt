package com.capstone.aksaintar.ui.views.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.aksaintar.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

private const val TAG = "LoginScreen"


@Composable
fun LoginScreen(
    onSignIn: (GoogleSignInAccount?) -> Unit,
    navigateToHomeScreen: (String) -> Unit,
    startGoogleSignIn: () -> Unit
) {

    val warna = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    val context = LocalContext.current
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                onSignIn(account)
                navigateToHomeScreen(account?.displayName ?: "")
            } catch (e: ApiException) {
                Log.e(TAG, "signInResult:failed code=${e.statusCode}")
                onSignIn(null)
            }
        } else {
            onSignIn(null)
        }
    }
    val logoResId = if (isSystemInDarkTheme()) {
        R.drawable.logodm
    } else {
        R.drawable.logonb
    }
    Column(
        modifier = Modifier
            .semantics { contentDescription = "Login Screen" },
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,

        ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = "Logo Aksa Intar",
            modifier = Modifier
                .height(416.dp)
                .width(416.dp)
        )
        Spacer(modifier = Modifier.height(100.dp))
        OutlinedButton(
            border = BorderStroke(2.dp, colors.primary),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20),
//           make button bacground transparent
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent,
            ),
            onClick = {
                startGoogleSignIn()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            },
            modifier = Modifier
                .width(338.dp)
                .height(56.dp)

        ) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Masuk dengan Google", fontWeight = FontWeight.Bold, color = warna)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = { navigateToHomeScreen("Tamu") },

            ) {

            Text(text = "Masuk sebagai tamu", fontWeight = FontWeight.Bold, color = warna)
        }
    }
}


