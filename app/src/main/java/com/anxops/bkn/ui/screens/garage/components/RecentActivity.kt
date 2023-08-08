package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.FadeInFadeOutAnimatedVisibility
import com.anxops.bkn.util.formatAsDayMonth
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun RecentActivity(rides: List<BikeRide>, onActivitySelected: (BikeRide) -> Unit = {}) {
    FadeInFadeOutAnimatedVisibility(visible = rides.isNotEmpty()) {
        if (rides.isNotEmpty()) {
            val total = rides.sumOf { it.distance ?: 0 }

            Column {
                Text(
                    text = "Recent activity (${formatDistanceAsKm(total)})",
                    modifier = Modifier.padding(start = 10.dp, top = 24.dp, bottom = 10.dp),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onBackground
                )

                Column(
                    modifier = Modifier.padding(horizontal = 10.dp).clip(RoundedCornerShape(10.dp))
                ) {
                    rides.forEach { ride ->

                        Row(
                            Modifier.padding(bottom = 1.dp),
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Button(onClick = { onActivitySelected(ride) }) {
                                BknIcon(
                                    CommunityMaterial.Icon.cmd_bike_fast,
                                    color = MaterialTheme.colors.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = ride.name ?: "",
                                    color = MaterialTheme.colors.onPrimary,
                                    style = MaterialTheme.typography.h3,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = 10.dp).weight(1f)

                                )
                                Text(
                                    text = formatDistanceAsKm(ride.distance ?: 0),
                                    color = MaterialTheme.colors.onPrimary,
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                                Text(
                                    text = ride.dateTime?.formatAsDayMonth() ?: "",
                                    color = MaterialTheme.colors.onPrimary,
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
