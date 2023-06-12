package com.capstone.aksaintar.ui.views.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


@Composable
fun HomeScreen(
    email: MutableState<String?>,
    navToCameraScreen: () -> Unit,
    navToUploadScreen: () -> Unit,
    navToColorScreen: () -> Unit,
    signOut: () -> Unit
) {
    val isGuest = rememberSaveable { mutableStateOf(email.value == "Guest") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(106.dp))
        Text(
            modifier = Modifier.padding(start = 25.dp),
            text = "Hi, ${email.value ?: ""}\nWelcome to Aksa Intar!",
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
            )
        )
        Text(
            text = "Choose feature on the button below to start",
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 10.dp, start = 25.dp)
        )
        Spacer(modifier = Modifier.height(150.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ButtonOutlined(
                    text = "Object Detection",
                    onClick = navToCameraScreen
                )
            }

            item {
                ButtonOutlined(
                    text = "Color Detection",
                    onClick = navToColorScreen
                )
            }
            item {
                ButtonOutlined(
                    text = "Contribution",
                    onClick = navToUploadScreen,
                    enabled = !isGuest.value
                )
            }
            item {
                ButtonOutlined(
                    text = "Sign Out",
                    onClick = signOut,
                    enabled = !isGuest.value
                )
            }
        }
    }
}

@Composable
fun ButtonOutlined(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    OutlinedButton(
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .semantics { testTag = "outlinedButton-$text" },
        enabled = enabled
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center, // Teks di tengah tombol
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        )
    }
}


