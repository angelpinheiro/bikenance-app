package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupTitle
import com.anxops.bkn.ui.screens.bikeSetup.SetupDetails


@Composable
fun BikeStatusPage(
    details: SetupDetails,
    onContinue: () -> Unit = {},
    onWearLevelUpdate: (ComponentCategory, Float) -> Unit = { _, _ -> },
) {


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        BikeSetupTitle(text = "Current bike status")

        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = "Which is the wear level of your bike?",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )


        details.wearLevel.forEach { (category, percent) ->

            Text(
                modifier = Modifier.padding(top = 30.dp, bottom = 10.dp),
                text = "${category.name}: ${(percent * 100).toInt()}%",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            )

            Slider(
                value = percent, onValueChange = {
                    onWearLevelUpdate(category, it)
                }, colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colors.secondary,
                    thumbColor = MaterialTheme.colors.secondary
                )
            )


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