package com.anxops.bkn.ui.screens.rides.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    val showBikeConfirm = true;//!item.ride.bikeConfirmed && bikes.isNotEmpty()

    Column {
        Card(backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxWidth().padding(
                    start = 10.dp, end = 10.dp, top = 10.dp, bottom = if (!showBikeConfirm) {
                        10.dp
                    } else {
                        0.dp
                    }
                ).clickable { onClick() },
            shape = if (!showBikeConfirm) {
                RoundedCornerShape(8.dp)
            } else {
                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            }) {


            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {

                Row(
                    Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically

                ) {

//                BknIcon(
//                    CommunityMaterial.Icon.cmd_bike_fast,
//                    color = MaterialTheme.colors.onPrimary,
//                    modifier = Modifier
//                        .padding(end = 10.dp)
//                        .size(16.dp)
//                )

                    Text(
                        text = item.ride.name ?: "",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.weight(1f).padding(end = 10.dp, bottom = 2.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                }

                Text(
                    text = item.ride.dateTime?.formatAsRelativeTime(showDay = true) ?: "",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 2.dp),
                )

                Row(
                    Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    item.ride.distance?.let {
                        BknIcon(
                            CommunityMaterial.Icon3.cmd_map_marker, modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${formatDistanceAsKm(it)}",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                        )
                    }

                    item.ride.totalElevationGain?.let {
                        BknIcon(
                            CommunityMaterial.Icon2.cmd_image_filter_hdr,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${it}m",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                        )
                    }

                    item.ride.movingTime?.let {
                        BknIcon(
                            CommunityMaterial.Icon.cmd_clock_fast, modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${formatDuration(it)}",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                        )
                    }
                }




                Text(text = "View on Strava",
                    color = MaterialTheme.colors.strava,
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 6.dp).clickable { onClickOpenOnStrava() })


            }

        }

        if (showBikeConfirm) {
            BikeConfirmationView(item.bike, bikes, onBikeConfirm = {
                onBikeConfirm(it, item.ride)
            })
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
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(MaterialTheme.colors.primaryVariant),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BikesDropDown(selected = chooserBike.value,
            bikes = bikes,
            modifier = Modifier.weight(2f),
            onBikeChange = {
                chooserBike.value = it
            })

        Surface(color = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .weight(1f)
                .height(45.dp)
                .padding(0.dp)
                .clickable {
                    chooserBike.value?.let { onBikeConfirm(it) }
                }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
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

        Surface(color = MaterialTheme.colors.primaryVariant,
            contentColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(0.dp)
                .clickable { expanded = !expanded }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = selected?.displayName() ?: "Select bike",
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.weight(1f)
                )
                BknIcon(
                    icon = CommunityMaterial.Icon.cmd_chevron_down, modifier = Modifier.size(16.dp)
                )
            }
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
