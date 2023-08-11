package com.anxops.bkn.ui.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun EmptyPlaceholder(text: String) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(100.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                )
                .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.4f))
                .padding(10.dp),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text,
                color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                style = MaterialTheme.typography.h3
            )
        }
    }
}
