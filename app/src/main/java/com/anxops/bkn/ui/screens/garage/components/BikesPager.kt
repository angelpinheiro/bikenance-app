package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.google.accompanist.pager.HorizontalPagerIndicator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikesPager(
    bikes: List<Bike>,
    rides: LazyPagingItems<BikeRide>?,
    onEditBike: (Bike) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState()
    val currentBike = bikes[pagerState.currentPage]

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 10.dp)
    ) {

        val bikesTitle = remember {
            "Bikes" + if (bikes.size > 1) " (${pagerState.currentPage + 1}/${bikes.size})" else ""
        }

        val pageWidth: Int = remember {
            (configuration.screenWidthDp.dp.value * 0.85).toInt()
        }

        Text(
            text = "Bikes",
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onPrimary
        )

        HorizontalPager(
            pageCount = bikes.size,
            state = pagerState,
            pageSize = PageSize.Fixed(pageWidth.dp),
        ) { page ->

            BikeCardV2(bike = bikes[page]) {
                onEditBike(bikes[page])
            }
        }

        if (bikes.size > 1) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    bikes.size,
                    activeColor = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .padding(top = 16.dp),
                )
            }
        }

        rides?.let {

            Text(
                text = "Recent activity",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary
            )

            LastRides(rides = it)
        }

        Text(
            text = "Upcoming maintenance",
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onPrimary
        )

        OngoingMaintenance(bikes, pagerState)


    }

}