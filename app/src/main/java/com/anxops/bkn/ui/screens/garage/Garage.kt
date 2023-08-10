package com.anxops.bkn.ui.screens.garage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.garage.components.BikesPager
import com.anxops.bkn.ui.screens.garage.components.RecentActivity
import com.anxops.bkn.ui.screens.garage.components.UpcomingMaintenances
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Garage(
    navigator: DestinationsNavigator,
    viewModel: GarageViewModel = hiltViewModel()
) {
    val nav = BknNavigator(navigator)
    val currentState by viewModel.screenState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = currentState.isRefreshing,
        onRefresh = { viewModel.loadData() }
    )
    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        if (currentState.isLoading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        } else if (currentState.bikes.isEmpty()) {
            EmptyGarage {
                nav.navigateToBikeSync()
            }
        } else {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BikesPager(bikes = currentState.bikes, onBikeChanged = {
                    viewModel.setSelectedBike(it)
                }, onEditBike = {
                    nav.navigateToBikeEdit(it._id)
                }, onBikeDetails = {
                    if (it.configDone) {
                        nav.navigateToBike(it._id)
                    } else {
                        nav.navigateToBikeSetup(it._id)
                    }
                }, onClickSync = {
                    nav.navigateToBikeSync()
                })

                Column(
                    Modifier.fillMaxSize().padding(horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    currentState.lastRides.let { rides ->
                        RecentActivity(rides = rides) {
                            nav.navigateToRide(it._id)
                        }
                    }

                    UpcomingMaintenances(currentState.selectedBike, onClickItem = { m ->
                        currentState.selectedBike?.let {
                            nav.navigateToBikeComponent(it._id, m.componentId)
                        }
                    })
                }
            }
        }

        if (!currentState.isLoading) {
            PullRefreshIndicator(
                currentState.isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun EmptyGarage(onClickAction: () -> Unit = {}) {
    Column(
        Modifier.fillMaxSize().padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_bikes_message),
            modifier = Modifier.padding(vertical = 30.dp, horizontal = 30.dp),
            style = MaterialTheme.typography.h2
        )

        Text(
            text = stringResource(R.string.no_bikes_get_started),
            modifier = Modifier.padding(vertical = 30.dp, horizontal = 30.dp),
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.ic_undraw_not_found),
            contentDescription = "Not found",
            modifier = Modifier.fillMaxWidth(0.6f).background(Color.Transparent).padding(top = 0.dp)
        )

        OutlinedButton(onClick = { onClickAction() }, modifier = Modifier.padding(top = 20.dp)) {
            Text(
                text = stringResource(R.string.sync_bikes_with_strava),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h3
            )
        }
    }
}
