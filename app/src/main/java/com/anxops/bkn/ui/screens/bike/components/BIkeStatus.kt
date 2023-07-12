package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.ui.theme.statusWarning

@Composable
fun BikeStatus(bike: Bike) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BikeStats(bike)

        Box(modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(0.3f)
            .aspectRatio(1f)) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                PulsatingCircles(size = 120.dp, color = MaterialTheme.colors.statusWarning)
            }
        }

        Text(
            text = "Overall status: Warning",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.fillMaxWidth(0.8f),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Some components of your ${bike.displayName()} need some love and care!",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.fillMaxWidth(0.8f),
            textAlign = TextAlign.Center
        )

    }

}