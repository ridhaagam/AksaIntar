package com.capstone.aksaintar.ui.screen.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.aksaintar.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener

class HomeActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignInClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )

        setContent {
            val email = intent.getStringExtra("email") ?: ""
            HomeActivityContent(email){
                signOut()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            setContent {
                HomeActivityContent( account.email ?: ""){
                    signOut()
                }
            }
        } else {
            setContent {
                HomeActivityContent(""){
                    signOut()
                }
            }
        }
    }

    private fun signOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void?> {
                finish()
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            })
    }
}

@Composable
fun HomeActivityContent(email: String, onSignOut: () -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Email: $email")
            Button(onClick = { onSignOut }) {
                Text(text = "Sign Out")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeActivityContent() {
    HomeActivityContent("johndoe@example.com"){}
}
