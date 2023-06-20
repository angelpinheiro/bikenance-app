package com.anxops.bkn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.ui.shared.BknIcon
import com.anxops.bkn.ui.theme.strava
import com.anxops.bkn.util.formatAsLocalDate
import com.anxops.bkn.util.toDate
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import java.util.concurrent.TimeUnit

@Composable
fun StatDetail(title: String, value: String, units: String = "") {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle2,
        )
        Text(
            text = "$value $units",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun StatDivider() {
    Divider(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .width(1.dp)
    )
}

@Composable
fun Ride(ride: BikeRide, bikes: List<Bike>,
         onClickOpenOnStrava: () -> Unit = {},
         onClick: () -> Unit = {},
         onClickRideBike: () -> Unit = {}
) {

//    Card(elevation = 1.dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(MaterialTheme.colors.surface)
            .padding(10.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BknIcon(
                CommunityMaterial.Icon.cmd_bike_fast,
                MaterialTheme.colors.strava,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = ride.name ?: "",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }

        Text(
            text = ride.dateTime.toDate()?.formatAsLocalDate() ?: "",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(start = 3.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ride.distance?.let {
                StatDetail(
                    title = "Distance",
                    value = it.div(1000).toString(),
                    units = "km",
                )
            }
            StatDivider()
            ride.totalElevationGain?.let {
                StatDetail(
                    title = "Elevation",
                    value = it.toString(),
                    units = "m",
                )
            }
            StatDivider()
            ride.movingTime?.let {
                StatDetail(
                    title = "Moving time",
                    value = formatDuration(it),
                )
            }

        }

        getRideBike(ride, bikes)?.name?.let { bikeName ->

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                OutlinedButton(onClick = onClickRideBike) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BknIcon(
                            CommunityMaterial.Icon.cmd_bike,
                            MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(16.dp)
                        )
                        Text(
                            text = bikeName,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.primary
                        )
                        BknIcon(
                            CommunityMaterial.Icon.cmd_chevron_down,
                            MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(16.dp)
                        )
                    }
                }
                ride.stravaId?.let {
                    OutlinedButton(
                        onClick = onClickOpenOnStrava,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BknIcon(
                                CommunityMaterial.Icon.cmd_earth,
                                MaterialTheme.colors.strava,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(16.dp)
                            )
                            Text(
                                text = "View on strava",
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.colors.strava
                            )
                        }
                    }
                }
            }
        }
    }
//    }

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

fun getRideBike(ride: BikeRide, bikes: List<Bike>): Bike? {
    return bikes.firstOrNull {
        it._id == ride.bikeId
    }
}
