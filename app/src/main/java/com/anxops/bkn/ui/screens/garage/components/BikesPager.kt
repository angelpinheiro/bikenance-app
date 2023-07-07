package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.google.accompanist.pager.HorizontalPagerIndicator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikesPager(
    bikes: List<Bike>,
    onEditBike: (Bike) -> Unit = {},
    onBikeChanged: (Bike) -> Unit = {},
    onBikeDetails: (Bike) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState()
    val pageSize = remember(bikes) {
        mutableStateOf(
            if (bikes.size == 1) {
                PageSize.Fill
            } else {
                PageSize.Fixed((configuration.screenWidthDp.dp.value * 0.85).toInt().dp)
            }
        )
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
            .padding(top = 16.dp)
    ) {

        if (bikes.size > 1) {
            Box(
                Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Bikes",
                    modifier = Modifier.align(Alignment.CenterStart),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onPrimary
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 10.dp)
                ) {
                    BikePagerIndicator(pagerState, bikes.size)
                }
            }
        }

        HorizontalPager(
            pageCount = bikes.size,
            state = pagerState,
            pageSize = pageSize.value,
        ) {
            val bike = bikes[it]
            GarageBikeCard(bike = bike, onEdit = {
                onEditBike(bike)
            }, onDetail = {
                onBikeDetails(bike)
            })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikePagerIndicator(pagerState: PagerState, bikeCount: Int) {

    if (bikeCount > 0) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                bikeCount,
                activeColor = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}