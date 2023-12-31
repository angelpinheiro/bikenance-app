package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.BikeStats
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.util.formatAsDayMonth
import com.anxops.bkn.util.formatDistanceAsShortKm
import com.anxops.bkn.util.formatElevationShort
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun BikeStats(bikeStats: BikeStats) {
    Column(
        Modifier.padding(10.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BikeStat(
                title = stringResource(R.string.bike_stats_rides),
                value = "${bikeStats.rideCount?.toInt() ?: 0}",
                icon = CommunityMaterial.Icon.cmd_bike_fast,
                modifier = Modifier.weight(1f)
            )
            Divider(
                Modifier.height(50.dp).width(1.dp),
                color = MaterialTheme.colors.primary
            )
            BikeStat(
                title = stringResource(R.string.bike_stats_distance),
                value = "${formatDistanceAsShortKm(bikeStats.distance.toInt())}",
                icon = CommunityMaterial.Icon3.cmd_map_marker_distance,
                modifier = Modifier.weight(1f)
            )
            Divider(
                Modifier.height(50.dp).width(1.dp),
                color = MaterialTheme.colors.primary
            )
            BikeStat(
                title = stringResource(R.string.bike_stats_ascent),
                value = "${formatElevationShort(bikeStats.elevationGain.toInt())}",
                modifier = Modifier.weight(1f),
                icon = CommunityMaterial.Icon2.cmd_image_filter_hdr
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BikeStat(
                title = stringResource(R.string.bike_stats_speed),
                value = "${bikeStats.averageSpeed?.toInt()} kmh",
                icon = CommunityMaterial.Icon.cmd_clock_fast,
                modifier = Modifier.weight(1f)
            )
            Divider(
                Modifier.height(50.dp).width(1.dp),
                color = MaterialTheme.colors.primary
            )
            BikeStat(
                title = stringResource(R.string.bike_stats_max_speed),
                value = "${bikeStats.maxSpeed?.toInt()} kmh",
                modifier = Modifier.weight(1f),
                icon = CommunityMaterial.Icon3.cmd_speedometer
            )
            Divider(
                Modifier.height(50.dp).width(1.dp),
                color = MaterialTheme.colors.primary
            )
            BikeStat(
                title = stringResource(R.string.bike_stats_last_ride),
                value = "${bikeStats.lastRideDate?.formatAsDayMonth()}",
                modifier = Modifier.weight(1f),
                icon = CommunityMaterial.Icon2.cmd_history
            )
        }
    }
}

@Composable
fun BikeStat(
    title: String,
    value: String,
    icon: IIcon? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier.padding(horizontal = 4.dp).padding(6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon?.let {
                BknIcon(
                    it,
                    MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                modifier = Modifier.padding(start = if (icon == null) 0.dp else 10.dp),
                color = MaterialTheme.colors.onBackground,
                text = title,
                style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BikeStatPreview() {
    BikenanceAndroidTheme(useSystemUIController = false, darkTheme = false) {
        BikeStat("Title", "1000 km")
    }
}
