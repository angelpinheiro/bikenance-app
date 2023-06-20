package com.anxops.bkn.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.model.Bike
import com.anxops.bkn.ui.components.Bike
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.NewBikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.SetupProfileScreenDestination
import com.anxops.bkn.ui.shared.BknIcon
import com.google.accompanist.pager.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    updateBikeResult: ResultRecipient<NewBikeScreenDestination, Boolean>,
    updateProfileResult: ResultRecipient<SetupProfileScreenDestination, Boolean>,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val nav = BknNavigator(navigator)
    val state = viewModel.state.collectAsState()
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

    SwipeRefresh(
        state = SwipeRefreshState(state.value.refreshing),
        onRefresh = {
            viewModel.reload()
        },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                backgroundColor = MaterialTheme.colors.primary,
                shape = CircleShape,
            )
        },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {

            Box(
                Modifier
                    .weight(1f)
            ) {
                if (bikes != null && bikes.isNotEmpty()) {
                    Bikes(bikes = bikes) {
                        nav.navigateToBike(it._id)
                    }
                } else if (bikes != null && bikes.isEmpty()) {
                    EmptyHome {
                        nav.navigateToNewBike()
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Bikes(bikes: List<Bike>, onEditBike: (Bike) -> Unit = {}) {
    val pagerState = rememberPagerState()
    val currentBike = bikes[pagerState.currentPage]
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        Box(Modifier.weight(1F)) {
            HorizontalPager(count = bikes.size, state = pagerState) { page ->
                Box(
                    Modifier
                        .graphicsLayer {
                            // Absolute offset for the current page from the scroll position.
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                            // We animate the scaleX + scaleY, between 75% and 100%
                            lerp(
                                start = ScaleFactor(0.65f, 0.65f),
                                stop = ScaleFactor(1f, 1f),
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale.scaleX
                                scaleY = scale.scaleY
                            }
                        }
                ) {
                    Bike(bike = bikes[page]) {
                        onEditBike(bikes[page])
                    }
                }
            }
            if (bikes.size > 1) {
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                )
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center

        ) {



            OutlinedButton(
                onClick =  { onEditBike(currentBike) },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BknIcon(
                        CommunityMaterial.Icon.cmd_cog,
                        MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(16.dp)
                    )
                    Text(
                        text = "Components",
                        modifier = Modifier.padding(3.dp),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            OutlinedButton(
                onClick =  { onEditBike(currentBike) },
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BknIcon(
                        CommunityMaterial.Icon3.cmd_tools,
                        MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(16.dp)
                    )
                    Text(
                        text = "Maintenances",
                        modifier = Modifier.padding(3.dp),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            OutlinedButton(
                onClick =  { onEditBike(currentBike) },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BknIcon(
                        CommunityMaterial.Icon3.cmd_pencil,
                        MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(16.dp)
                    )
                }
            }

        }

    }
}

@Composable
fun EmptyHome(onClickNew: () -> Unit = {}) {
    Box(Modifier.fillMaxSize()) {

        Text(
            text = "You have no bikes yet",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(bottom = 100.dp)
                .align(Alignment.Center)
        )
        Button(
            onClick = onClickNew,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.Center)
        ) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_plus,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp),
                color = MaterialTheme.colors.onPrimary
            )
            Text(text = "Add a new bike")
        }
        Image(
            painter = painterResource(id = R.drawable.ic_by_the_road),
            contentDescription = "Not found",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}