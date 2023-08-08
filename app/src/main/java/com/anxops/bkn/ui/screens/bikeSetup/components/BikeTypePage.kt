package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupDescription
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupTitle
import com.anxops.bkn.ui.screens.bikeSetup.SetupDetails

@Composable
fun BikeTypePage(
    bike: Bike,
    details: SetupDetails,
    onBikeTypeSelected: (bikeType: BikeType) -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BikeSetupTitle(text = "Bike type")
        BikeSetupDescription(text = "Which kind of bike is your '${bike.displayName()}'?")

        Column(
            Modifier.fillMaxWidth().padding(vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BikeType.getAllKnown().forEach {
                OutlinedButton(
                    onClick = {
                        onBikeTypeSelected(it)
                    },
                    modifier = Modifier.padding(4.dp).fillMaxWidth(0.8f),
                        colors = if (details.selectedBikeType == it) {
                        ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant
                        )
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = it.extendedType,
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h5

                    )
                }
            }
        }
    }
}
