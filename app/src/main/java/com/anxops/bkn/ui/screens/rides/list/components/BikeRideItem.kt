package com.anxops.bkn.ui.screens.rides.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.util.formatAsRelativeTime
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


@Composable
fun Ride(
    ride: BikeRide,
    bikes: List<Bike>,
    onClickOpenOnStrava: () -> Unit = {},
    onClick: () -> Unit = {},
    onClickRideBike: () -> Unit = {}
) {

    val context = LocalContext.current
    val bike = getRideBike(ride, bikes)

    Card(backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)) {

        BikeRideItem(item = ride, bike = bike)
    }
}


@Composable
fun BikeRideItem(item: BikeRide, bike: Bike?) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.Top
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
                    text = item.name ?: "",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )


            }



            Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {

                item.distance?.let {
                    BknIcon(
                        CommunityMaterial.Icon3.cmd_map_marker, modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${formatDistanceAsKm(it)}",
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(start = 4.dp, end = 10.dp)
                    )
                }

                item.totalElevationGain?.let {
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

                item.movingTime?.let {
                    BknIcon(
                        CommunityMaterial.Icon.cmd_clock_fast, modifier = Modifier.size(14.dp)
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
                text = item.dateTime?.formatAsRelativeTime(showDay = true) ?: "",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h4,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            if(item.dateTime?.isAfter(LocalDateTime.now().minusDays(10)) == true){
            Row(Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {

                bike?.let {
                    OutlinedButton(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                    ) {
                        Text(
                            text = bike.displayName(),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }

                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                ) {
                    Text(
                        text = "Confirm bike",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
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

fun secondsToHours(seconds: Int): Long {
    return TimeUnit.SECONDS.toHours(seconds.toLong()) % 24

}

fun getRideBike(ride: BikeRide, bikes: List<Bike>): Bike? {
    return bikes.firstOrNull {
        it._id == ride.bikeId
    }
}
