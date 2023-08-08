package com.anxops.bkn.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Loading(text: String? = null, color: Color = MaterialTheme.colors.primaryVariant, contentColor: Color = MaterialTheme.colors.onPrimary) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = contentColor,
            strokeWidth = 5.dp,
            modifier = Modifier
                .size(50.dp)
        )
        if (text != null) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(top = 16.dp),
                color = contentColor
            )
        }
    }
}



@Composable
fun Message(text: String? = null, color: Color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.6f), contentColor: Color = MaterialTheme.colors.onPrimary) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(top = 16.dp),
                color = contentColor
            )
        }
    }
}

