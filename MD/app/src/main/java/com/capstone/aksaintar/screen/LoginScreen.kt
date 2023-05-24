package com.capstone.aksaintar.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.aksaintar.R
import com.capstone.aksaintar.SecondActivity
import com.capstone.aksaintar.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

private const val RC_SIGN_IN = 123

@Composable
fun LoginScreen(onSignIn: (GoogleSignInAccount?) -> Unit){
    val context = LocalContext.current

//    // State untuk menangani status login
//    val loggedInState = remember { mutableStateOf(false) }
//    // State untuk menyimpan email pengguna yang login
//    val userEmailState = remember { mutableStateOf("") }
//    // Fungsi untuk menangani tindakan login
//    val signInWithGoogle: (GoogleSignInAccount) -> Unit = { account ->
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//
//        val googleSignInClient = GoogleSignIn.getClient(context, gso)
//        val signInIntent = googleSignInClient.signInIntent
//        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
//
//        // Set state login dan email pengguna yang login
//        loggedInState.value = true
//        userEmailState.value = account.email ?: ""
//
//        // Navigasi ke SecondActivity setelah berhasil login
//        val intent = Intent(context, SecondActivity::class.java)
//        intent.putExtra("email", account.email)
//        context.startActivity(intent)
//    }
    Box(contentAlignment = Alignment.TopCenter) {
        Image(
            painter = painterResource(id = R.drawable.ic_image), contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .width(250.dp)
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Button(
                onClick = {val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    val signInIntent = googleSignInClient.signInIntent
                    (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)},
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Continue with Google", color = Teal200, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Guest", color = Teal200, fontSize = 16.sp)
                }
            }

        }
    }
}



@Composable
fun LoginScreenPreview() {
    AksaIntarTheme {
        Surface(color = MaterialTheme.colors.background) {
            LoginScreen{}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginScreenPreview()
}
