package com.capstone.aksaintar.ui.views.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(
    navToCameraScreen: () -> Unit
) {

    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()


    ) {
        Spacer(modifier = Modifier.height(106.dp))
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 25.dp),

            text = "Hi, Jeremy!" +
                    "\nWelcome to Aksa Intar!",

            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
            )
        )
        Text(text = "Choose feature on the button below to start", textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.Start)
                .padding(start = 25.dp)
        )
        Spacer(modifier = Modifier.height(150.dp))
        ButtonOutlined(
            text = "Object Detection",
            onClick = navToCameraScreen
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonOutlined(text = "Color Detection") {}
        Spacer(modifier = Modifier.height(20.dp))
        ButtonOutlined(text = "Contribution") {}

    }

}


@Composable
fun ButtonOutlined(text: String, onClick: () -> Unit) {
    OutlinedButton(
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        ),
        onClick = onClick,
        modifier = Modifier
            .width(338.dp)
            .height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen(
        navToCameraScreen = {}
    )
}
