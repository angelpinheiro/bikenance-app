package com.anxops.bkn.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
fun Loading(text: String? = null, color: Color = MaterialTheme.colors.primaryVariant) {
    Box(
        Modifier
            .fillMaxSize()
            .background(color)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.secondary,
            strokeWidth = 5.dp,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center),
        )
        if (text != null) {
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                color = MaterialTheme.colors.secondary
            )
        }
    }
}
