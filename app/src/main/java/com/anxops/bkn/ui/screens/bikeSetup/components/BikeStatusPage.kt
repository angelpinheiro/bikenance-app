package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupTitle
import com.anxops.bkn.ui.screens.bikeSetup.SetupDetails
import kotlin.math.floor


@Composable
fun BikeStatusPage(
    details: SetupDetails,
    onContinue: () -> Unit = {},
    onLastMaintenanceUpdate: (ComponentCategory, Float) -> Unit = { _, _ -> },
) {

    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scrollState)

    ) {

        BikeSetupTitle(text = "Current bike status")

        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = "When was the last maintenance of",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        Column(
            Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )             
        ) {
            details.lastMaintenances.toList().forEachIndexed { index, (category, months) ->

                val displayValue =
                    when (months) {
                        in 0f..1f -> "Recently"
                        12f -> "One year ago or more"
                        else -> {
                            "${floor(months).toInt()} months ago"
                        }
                    }

                Column(
                    Modifier
                        .background(MaterialTheme.colors.primaryVariant)
                        .padding(bottom = if(index < details.lastMaintenances.size - 1) 5.dp else 0.dp)
                        .background(MaterialTheme.colors.primary)
                        .padding(10.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${category.name}",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            text = "$displayValue",
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colors.primary)
                                .padding(3.dp)

                        )
                    }

                    PeriodSelector(value = months, onUpdate = {
                        onLastMaintenanceUpdate(category, it)
                    })
                }


            }
        }

        OutlinedButton(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            onClick = { onContinue() },
            modifier = Modifier.padding(top = 25.dp)

        ) {
            Text(text = "Continue", Modifier.padding(2.dp))
        }
    }
}


@Composable
fun PeriodSelector(value: Float, onUpdate: (Float) -> Unit = {}) {

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Slider(
            value = value,
            valueRange = 0f..12f,
            steps = 11,
            onValueChange = { onUpdate(it) },
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colors.surface,
                thumbColor = MaterialTheme.colors.surface,
                inactiveTrackColor = MaterialTheme.colors.primaryVariant,
                inactiveTickColor = MaterialTheme.colors.surface.copy(alpha = 0.5f),
                activeTickColor = MaterialTheme.colors.surface
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Recently", color = MaterialTheme.colors.surface, fontSize = 10.sp)
            Text(text = "1 year +", color = MaterialTheme.colors.surface, fontSize = 10.sp)
        }

    }


}