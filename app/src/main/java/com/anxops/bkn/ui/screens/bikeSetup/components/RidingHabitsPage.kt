package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.AthleteStats
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupDescription
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupDivider
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupTitle
import com.anxops.bkn.ui.screens.bikeSetup.SetupDetails
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.FadeInFadeOutAnimatedVisibility
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun RidingHabitsPage(stats: AthleteStats, details: SetupDetails, onContinue: () -> Unit = {}) {
    val showDialog = remember {
        mutableStateOf(false)
    }

    val (estimation, avgActivity) = stats.monthlyEstimationBasedOnYTD()
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BikeSetupTitle(text = "Rinding habits")
            BikeSetupDescription(
                text = "According to your Strava data, your average monthly riding distance is: ",
                TextAlign.Center
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 20.dp),
                    text = "${formatDistanceAsKm(estimation.distance.toInt())}",
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h1
                )

                IconButton(onClick = { showDialog.value = !showDialog.value }) {
                    BknIcon(
                        CommunityMaterial.Icon2.cmd_information,
                        MaterialTheme.colors.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            BikeSetupDescription(
                text = "We will use this estimation to provide you with better maintenance recommendations.",
                TextAlign.Center
            )

            BikeSetupDivider(20.dp)

            OutlinedButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                onClick = { onContinue() },
                modifier = Modifier.padding(top = 25.dp)

            ) {
                Text(text = "Agree and finish!", Modifier.padding(2.dp))
            }
        }

        FadeInFadeOutAnimatedVisibility(visible = showDialog.value) {
            Card(
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 10.dp
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 26.dp).padding(bottom = 26.dp, top = 16.dp)
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = "Mileage calculation",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h2
                        )

                        IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = { showDialog.value = !showDialog.value }) {
                            BknIcon(
                                CommunityMaterial.Icon.cmd_close,
                                MaterialTheme.colors.onPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "Your average monthly riding distance is ${
                            formatDistanceAsKm(
                                estimation.distance.toInt()
                            )
                        }. This calculation is based on data from this year up until today. Results report an average ride of ${
                            formatDistanceAsKm(
                                avgActivity.distance.toInt()
                            )
                        } (${avgActivity.hours()} hours) and ${avgActivity.count.toInt()} rides per month.",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Light
                    )

                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = "As a reference, your las month stats are ${
                            formatDistanceAsKm(
                                stats.recentRideTotals.distance.toInt()
                            )
                        } (${
                            stats.recentRideTotals.hours().toInt()
                        } hours) in ${stats.recentRideTotals.count.toInt()} rides.",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}
