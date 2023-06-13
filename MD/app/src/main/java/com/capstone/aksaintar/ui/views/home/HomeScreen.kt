package com.capstone.aksaintar.ui.views.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.capstone.aksaintar.R


@Composable
fun HomeScreen(
    email: MutableState<String?>,
    navToCameraScreen: () -> Unit,
    navToUploadScreen: () -> Unit,
    navToColorScreen: () -> Unit,
    signOut: () -> Unit
) {

    val isGuest = rememberSaveable { mutableStateOf(email.value == "Tamu") }
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier
        .fillMaxWidth()
        .semantics {
            contentDescription = "Halaman Utama"

        }

    ) {
        Spacer(modifier = Modifier.height(106.dp))
        Text(
            modifier = Modifier.padding(start = 25.dp),
            text = "Hai, ${email.value ?: ""}\nSelamat Datang di Aksaintar",
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
            )
        )
        Text(
            text = stringResource(R.string.guide_text),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 10.dp, start = 25.dp),
        )
        Spacer(modifier = Modifier.height(150.dp))
        LazyColumn(
            modifier = Modifier.padding(horizontal = 25.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            item {
                ButtonOutlined(
                    text = stringResource(R.string.object_button), onClick = navToCameraScreen
                )
            }

            item {
                ButtonOutlined(
                    text = stringResource(R.string.color_button), onClick = navToColorScreen
                )

            }


            if (email.value != "Tamu") {
                item {
                    ButtonOutlined(
                        text = stringResource(R.string.contribution_button), onClick = navToUploadScreen, enabled = !isGuest.value
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }

                item {
                    ButtonOutlined(
                        text = stringResource(R.string.signout_button), onClick = signOut, enabled = !isGuest.value
                    )
                }
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
            backgroundColor = Color.Transparent
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
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
        )
    }
}