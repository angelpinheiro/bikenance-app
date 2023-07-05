package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.SlideInOutAnimatedVisibility
import com.anxops.bkn.util.formatAsSimpleDate
import com.anxops.bkn.util.formatDistanceAsKm
import com.anxops.bkn.util.toDate
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun RecentActivity(rides: List<BikeRide>) {

    SlideInOutAnimatedVisibility(visible = rides.isNotEmpty()) {

        if (rides.isNotEmpty()) {

            val total = rides.sumOf { it.distance ?: 0 }


            Column {
                Text(
                    text = "Recent activity (${formatDistanceAsKm(total)})",
                    modifier = Modifier.padding(start = 10.dp, top = 24.dp, bottom = 10.dp),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onPrimary
                )



                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {

                    rides.forEach() {

                        Box(
                            modifier = Modifier
                                .padding(bottom = 1.dp)
                                .background(MaterialTheme.colors.primary)
                                .fillMaxWidth()
                                .padding(horizontal = 0.dp),
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                BknIcon(
                                    CommunityMaterial.Icon.cmd_bike_fast,
                                    color = MaterialTheme.colors.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = it.name ?: "",
                                    color = MaterialTheme.colors.onPrimary,
                                    style = MaterialTheme.typography.h3,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .weight(1f)
                                )
                                Text(
                                    text = formatDistanceAsKm(it.distance ?: 0),
                                    color = MaterialTheme.colors.onPrimary,
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                )
                                Text(
                                    text = it.dateTime.toDate()?.formatAsSimpleDate() ?: "",
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