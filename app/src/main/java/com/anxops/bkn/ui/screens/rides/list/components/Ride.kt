package com.anxops.bkn.ui.screens.rides.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.util.formatAsSimpleDateTime
import com.anxops.bkn.util.formatDistanceAsKm
import com.anxops.bkn.util.toDate
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import java.util.concurrent.TimeUnit


@Composable
fun Ride(
    ride: BikeRide, bikes: List<Bike>,
    onClickOpenOnStrava: () -> Unit = {},
    onClick: () -> Unit = {},
    onClickRideBike: () -> Unit = {}
) {

    val context = LocalContext.current
    val bike = getRideBike(ride, bikes)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(bottom = 5.dp)
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        BikeRideItem(item = ride, bike = bike)
        Divider(
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp)
                .height(1.dp)
        )
    }
}


@Composable
fun BikeRideItem(item: BikeRide, bike: Bike?) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BknIcon(
            CommunityMaterial.Icon.cmd_bike_fast,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .padding(top = 6.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.surface)
                .padding(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {
//item.dateTime.toDate()?.formatAsSimpleDate()
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
                bike?.let {
                    Text(
                        text = bike.displayName(),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.primaryVariant,
                                shape = CircleShape
                            )
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

            }

            Text(
                text = item.dateTime.toDate()?.formatAsSimpleDateTime() ?: "",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Row(Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {

                item.distance?.let {
                    BknIcon(
                        CommunityMaterial.Icon3.cmd_map_marker,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${formatDistanceAsKm(it)}",
                        color = MaterialTheme.colors.background,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                    )
                }

                item.totalElevationGain?.let {
                    BknIcon(
                        CommunityMaterial.Icon2.cmd_image_filter_hdr,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${it}m",
                        color = MaterialTheme.colors.background,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                    )
                }

                item.movingTime?.let {
                    BknIcon(
                        CommunityMaterial.Icon.cmd_clock_fast,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${formatDuration(it)}",
                        color = MaterialTheme.colors.background,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                    )
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

fun getRideBike(ride: BikeRide, bikes: List<Bike>): Bike? {
    return bikes.firstOrNull {
        it._id == ride.bikeId
    }
}
