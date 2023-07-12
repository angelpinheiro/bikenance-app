package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun BikeStats(bike: Bike) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primaryVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BikeStat(
            title = "Distance",
            value = formatDistanceAsKm(bike.distance?.toInt() ?: 0),
            icon = CommunityMaterial.Icon3.cmd_map_marker_distance,
            modifier = Modifier.weight(1f)
        )
        Divider(
            Modifier
                .height(50.dp)
                .width(1.dp),
            color = MaterialTheme.colors.primary
        )
        BikeStat(
            title = "Ascent",
            value = "150.000 m",
            modifier = Modifier.weight(1f),
            icon = CommunityMaterial.Icon2.cmd_image_filter_hdr,
        )
        Divider(
            Modifier
                .height(50.dp)
                .width(1.dp),
            color = MaterialTheme.colors.primary
        )
        BikeStat(
            title = "Last ride",
            value = "Yesterday",
            modifier = Modifier.weight(1f),
            icon = CommunityMaterial.Icon.cmd_bike_fast
        )
    }
}

@Composable
fun BikeStat(
    title: String,
    value: String,
    icon: IIcon = CommunityMaterial.Icon.cmd_bike,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(horizontal = 4.dp)
            .padding(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BknIcon(
                icon,
                MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                text = title,
                style = MaterialTheme.typography.h4,
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onPrimary
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