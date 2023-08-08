package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InfoPage(onContinue: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = "Bike Setup",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h1
        )

        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = "We need some essential information about your bike and riding habits.",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = "This will help us to provide you with the best maintenance recommendations.",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        OutlinedButton(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            onClick = { onContinue() },
            modifier = Modifier.padding(top = 25.dp)

        ) {
            Text(text = "Let's go!", Modifier.padding(2.dp))
        }
    }
}
