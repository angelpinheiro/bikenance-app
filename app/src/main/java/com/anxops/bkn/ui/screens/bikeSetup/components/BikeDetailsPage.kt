package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupDescription
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupDivider
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupTitle
import com.anxops.bkn.ui.screens.bikeSetup.SetupDetails

@Composable
fun BikeDetailsPage(
    details: SetupDetails,
    onTubelessSelectionChange: (Boolean) -> Unit = {},
    onDropperSelectionChange: (Boolean) -> Unit = {},
    onFullSuspensionSelectionChange: (Boolean) -> Unit = {},
    onCliplessPedalsSelectionChange: (Boolean) -> Unit = {},
    onContinue: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (!listOf(
                BikeType.Road,
                BikeType.Gravel,
                BikeType.Stationary
            ).contains(details.selectedBikeType)
        ) {
            BikeSetupTitle(text = "Bike details")
            BikeSetupDivider(30.dp)
            BikeSetupDescription(text = "Is this bike full suspension?")
            BooleanSelector("Yes", "No", value = details.fullSuspension, onOptionSelected = {
                onFullSuspensionSelectionChange(it)
            })
        }

        BikeSetupDivider(30.dp)
        BikeSetupDescription(text = "Does this bike have a dropper post?")
        BooleanSelector("Yes", "No", value = details.hasDropperPost, onOptionSelected = {
            onDropperSelectionChange(it)
        })

        BikeSetupDivider(30.dp)
        BikeSetupDescription(text = "Are the bike tires tubeless?")
        BooleanSelector("Yes", "No", value = details.hasTubeless, onOptionSelected = {
            onTubelessSelectionChange(it)
        })
        BikeSetupDivider(30.dp)
        BikeSetupDescription(text = "Are you using clipless pedals?")
        BooleanSelector("Yes", "No", value = details.hasCliplessPedals, onOptionSelected = {
            onCliplessPedalsSelectionChange(it)
        })
    }
}
