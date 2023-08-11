package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.EmptyPlaceholder
import com.anxops.bkn.util.formatAsDayMonth
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import kotlinx.coroutines.delay

@Composable
fun RecentActivity(rides: List<BikeRide>, onActivitySelected: (BikeRide) -> Unit = {}) {
    val total = rides.sumOf { it.distance ?: 0 }

    Column {
        Text(
            text = stringResource(R.string.recent_activity_title, formatDistanceAsKm(total)),
            modifier = Modifier.padding(start = 10.dp, top = 24.dp, bottom = 10.dp),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )

        if (rides.isEmpty()) {
            EmptyPlaceholder(stringResource(R.string.no_recent_activity))
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
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
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .weight(1f)

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

@Composable
fun OneShotDelayedVisibility(delayMs: Long = 100, reverse: Boolean = false, content: @Composable () -> Unit) {
    var visible by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(delayMs)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { if (reverse) -50 else 50 }),
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { if (reverse) -50 else 50 })

    ) {
        content()
    }
}
