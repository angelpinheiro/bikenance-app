package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.ui.shared.components.BknIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikesPager(
    bikes: List<Bike>,
    onEditBike: (Bike) -> Unit = {},
    onBikeChanged: (Bike) -> Unit = {},
    onBikeDetails: (Bike) -> Unit = {},
    onClickSync: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState()

    val pageSize = if (bikes.size > 1) {
        PageSize.Fixed((configuration.screenWidthDp.dp.value * 0.85).toInt().dp)
    } else {
        PageSize.Fill
    }

    val selectedBike = remember {
        mutableStateOf<Bike?>(null)
    }

    LaunchedEffect(pagerState, bikes) {
        // Observe bike selection en notify callback
        snapshotFlow { pagerState.settledPage }.collect { page ->
            bikes.getOrNull(page)?.let {
                selectedBike.value = it
                onBikeChanged(it)
            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 0.dp)
    ) {


        Row(
            Modifier
                .padding(start = 16.dp, top = 6.dp, bottom = 6.dp, end = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bikes",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onBackground
            )
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (bikes.size > 1) {
                    BikePagerIndicator(pagerState, bikes.size)
                }
            }
            IconButton(onClick = { onClickSync() }) {
                BknIcon(
                    icon = CommunityMaterial.Icon.cmd_cog_sync,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
        }


        HorizontalPager(
            pageCount = bikes.size,
            state = pagerState,
            pageSize = pageSize,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(2)
            )
        ) {
            val bike = bikes[it]
            GarageBikeCard(bike = bike, onEdit = {
                onEditBike(bike)
            }, onDetail = {
                onBikeDetails(bike)
            },
                isLast = it == bikes.size - 1
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikePagerIndicator(pagerState: PagerState, bikeCount: Int) {

    if (bikeCount > 0) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                repeat(bikeCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        MaterialTheme.colors.onBackground
                    } else MaterialTheme.colors.onBackground.copy(
                        alpha = 0.2f
                    )
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)

                    )
                }
            }
        }
    }
}