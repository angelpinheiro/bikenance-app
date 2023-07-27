package com.anxops.bkn.ui.screens.rides.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.theme.strava
import com.anxops.bkn.util.formatAsRelativeTime
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import java.util.concurrent.TimeUnit

data class RideAndBike(val ride: BikeRide, val bike: Bike?)

@Composable
fun BikeRideItem(
    item: RideAndBike,
    bikes: List<Bike>,
    onBikeConfirm: (Bike, BikeRide) -> Unit = { _, _ -> },
    onClick: () -> Unit = {},
    onClickOpenOnStrava: () -> Unit = {}
) {

    Card(backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 10.dp)) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.Top
            ) {

                BknIcon(
                    CommunityMaterial.Icon.cmd_bike_fast,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 6.dp)
                        .size(26.dp)

                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                ) {

                    Row(
                        Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically

                    ) {

                        Text(
                            text = item.ride.name ?: "",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h2,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )


                    }

                    Row(
                        Modifier.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        item.ride.distance?.let {
                            BknIcon(
                                CommunityMaterial.Icon3.cmd_map_marker,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${formatDistanceAsKm(it)}",
                                color = MaterialTheme.colors.onBackground,
                                style = MaterialTheme.typography.h4,
                                modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                            )
                        }

                        item.ride.totalElevationGain?.let {
                            BknIcon(
                                CommunityMaterial.Icon2.cmd_image_filter_hdr,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${it}m",
                                color = MaterialTheme.colors.onBackground,
                                style = MaterialTheme.typography.h4,
                                modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                            )
                        }

                        item.ride.movingTime?.let {
                            BknIcon(
                                CommunityMaterial.Icon.cmd_clock_fast,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${formatDuration(it)}",
                                color = MaterialTheme.colors.onBackground,
                                style = MaterialTheme.typography.h4,
                                modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                            )
                        }
                    }

                    Text(
                        text = item.ride.dateTime?.formatAsRelativeTime(showDay = true) ?: "",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h4,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )


                    Text(text = "View on Strava",
                        color = MaterialTheme.colors.strava,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .clickable { onClickOpenOnStrava() })


                }

            }

            if (!item.ride.bikeConfirmed && bikes.isNotEmpty()) {
                BikeConfirmationView(item.bike, bikes, onBikeConfirm = {
                    onBikeConfirm(it, item.ride)
                })
            }

        }
    }
}

@Composable
private fun BikeConfirmationView(
    selectedBike: Bike?, bikes: List<Bike>, onBikeConfirm: (Bike) -> Unit
) {

    val chooserBike = remember(selectedBike) {
        mutableStateOf(selectedBike)
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {


        BikesDropDown(selected = chooserBike.value,
            bikes = bikes,
            modifier = Modifier.weight(2f),
            onBikeChange = {
                chooserBike.value = it
            })

        OutlinedButton(
            enabled = chooserBike.value != null,
            onClick = {
                chooserBike.value?.let { onBikeConfirm(it) }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Confirm",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}


@Composable
fun BikesDropDown(
    modifier: Modifier = Modifier,
    selected: Bike?,
    bikes: List<Bike>,
    onBikeChange: (Bike) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        OutlinedButton(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
            shape = RoundedCornerShape(bottomStart = 5.dp, topStart = 5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = selected?.displayName() ?: "Select bike",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(end = 10.dp)
            )
            BknIcon(icon = CommunityMaterial.Icon.cmd_chevron_down, modifier = Modifier.size(16.dp))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
                .background(MaterialTheme.colors.primaryVariant),
        ) {
            bikes.forEach { bike ->
                DropdownMenuItem(content = { Text(bike.displayName()) }, onClick = {
                    onBikeChange(bike)
                    expanded = false
                }, contentPadding = PaddingValues(10.dp), modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


fun formatDuration(seconds: Int): String {

    val hours = TimeUnit.SECONDS.toHours(seconds.toLong()) % 24
    val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong()) % 60
    val secs = TimeUnit.SECONDS.toSeconds(seconds.toLong()) % 60

    return when {
        hours > 0L -> {
            "${hours}h ${minutes}min"
        }

        minutes > 0L -> {
            "${minutes}min"
        }

        else -> {
            "${secs}s"
        }
    }
}
