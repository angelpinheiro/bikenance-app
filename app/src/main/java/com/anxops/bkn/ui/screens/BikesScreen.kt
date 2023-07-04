package com.anxops.bkn.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.anxops.bkn.R
import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.storage.FakeData
import com.anxops.bkn.ui.components.BikeCardV2
import com.anxops.bkn.ui.components.LastRides
import com.anxops.bkn.ui.components.getColorForProgress
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.NewBikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.SetupProfileScreenDestination
import com.anxops.bkn.ui.theme.statusDanger
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BikesScreen(
    navigator: DestinationsNavigator,
    updateBikeResult: ResultRecipient<NewBikeScreenDestination, Boolean>,
    updateProfileResult: ResultRecipient<SetupProfileScreenDestination, Boolean>,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val nav = BknNavigator(navigator)
    val state = viewModel.state.collectAsState()
    val rides = viewModel.paginatedRidesFlow.collectAsLazyPagingItems()
    val bikesState = viewModel.bikes.collectAsState()
    val bikes = bikesState.value.sortedByDescending { it.distance }

    updateBikeResult.onNavResult {
        when (it) {
            is NavResult.Value -> {
                Log.d("HOmeScreen", "Nav result received ${it.value}")
//                viewModel.reload()
            }

            else -> {}
        }
    }

    updateProfileResult.onNavResult {
        when (it) {
            is NavResult.Value -> {
                Log.d("HOmeScreen", "Nav result received ${it.value}")
//                viewModel.reload()
            }

            else -> {}
        }
    }

    val bgGradient = Brush.verticalGradient(
        0f to MaterialTheme.colors.primaryVariant.copy(alpha = 0.95f),
        0.1f to MaterialTheme.colors.primaryVariant.copy(alpha = 0.98f),
        0.5f to MaterialTheme.colors.primaryVariant.copy(alpha = 0.98f),
        1f to MaterialTheme.colors.primaryVariant.copy(alpha = 0.96f),
    )



    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.value.refreshing,
        onRefresh = { viewModel.reload() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        Box(Modifier.fillMaxSize()) {

//            Image(
//                painter = painterResource(id = R.drawable.ic_biking),
//                contentDescription = "Not found",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .alpha(0.5f)
//                    .align(Alignment.BottomCenter)
//            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(bgGradient)
            )

        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                Modifier
                    .weight(1f)
            ) {

                if (bikes != null && bikes.isNotEmpty()) {
                    BikesPager(bikes = bikes, rides = rides) {
                        nav.navigateToBike(it._id)
                    }
                }
            }
        }

        PullRefreshIndicator(
            state.value.refreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}


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

        NextMaintenance(bikes, pagerState)


    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NextMaintenance(bikes: List<Bike>, pagerState: PagerState) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {

        FakeData.maintenances
            .filter { it.bike == bikes[pagerState.currentPage].name }
            .forEach {

            Column(
                modifier = Modifier
                    .padding(bottom = 1.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .padding(bottom = 20.dp),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    CircularProgressIndicator(
                        progress = it.percentage,
                        modifier = Modifier.size(26.dp),
                        strokeWidth = 6.dp,
                        color = getColorForProgress(percentage = it.percentage),
                        backgroundColor = MaterialTheme.colors.primary
                    )
                    Text(
                        text = it.bikePart,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(horizontal = 10.dp)
//                            .weight(1f)
                    )
                    Divider(
                        color = getColorForProgress(percentage = it.percentage),
                        modifier = Modifier
                            .height(1.dp)
                            .padding(end = 10.dp)
                            .weight(1f)
                    )
                    Text(
                        text = it.title,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.primaryVariant, shape = CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    )

                }
                Text(
                    text = "Last ${it.title}: 3 months ago",
                    color = MaterialTheme.colors.background,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 36.dp)
                )
                if (it.percentage < 1.0) {
                    Text(
                        text = "Estimated duration: 2 months",
                        color = MaterialTheme.colors.background,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(start = 36.dp)
                    )
                } else {
                    Text(
                        text = "Service required",
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .padding(start = 34.dp)
                            .background(
                                color = MaterialTheme.colors.statusDanger, shape = CircleShape
                            )
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    )
                }

                Divider(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(1.dp)
                )

            }
        }
    }
}