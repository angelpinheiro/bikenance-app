package com.anxops.bkn.ui.screens.garage

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.NewBikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.ProfileScreenDestination
import com.anxops.bkn.ui.screens.garage.components.BikesPager
import com.anxops.bkn.ui.screens.garage.components.RecentActivity
import com.anxops.bkn.ui.screens.garage.components.UpcomingMaintenance
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.bgGradient
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Garage(
    navigator: DestinationsNavigator,
    updateBikeResult: ResultRecipient<NewBikeScreenDestination, Boolean>,
    updateProfileResult: ResultRecipient<ProfileScreenDestination, Boolean>,
    viewModel: GarageViewModel = hiltViewModel()
) {
    val nav = BknNavigator(navigator)
    val state = viewModel.screenState.collectAsState()
//    val rides = viewModel.selectedBikeRides
//    val bikes = viewModel.bikes.collectAsState()
//
//
//
//    var selectedBike = viewModel.selectedBike.collectAsState()

    updateBikeResult.onNavResult {
        when (it) {
            is NavResult.Value -> {
                // viewModel.reload()
            }

            else -> {}
        }
    }

    updateProfileResult.onNavResult {
        when (it) {
            is NavResult.Value -> {
                // viewModel.reload()
            }

            else -> {}
        }
    }

    val gradientBackground = bgGradient()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.value is GarageScreenState.Loading,
        onRefresh = { viewModel.loadData() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .pullRefresh(pullRefreshState)
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (val currentState = state.value) {
                is GarageScreenState.ShowingGarage -> {
                    BikesPager(
                        bikes = currentState.bikes,
                        onBikeChanged = {
                            viewModel.setSelectedBike(it)
                        },
                        onEditBike = {
                            nav.navigateToBike(it._id)
                        }, onBikeDetails = {
                            if (it.configDone)
                                nav.navigateToBikeDetails(it._id)
                            else
                                nav.navigateToBikeSetup(it._id)
                        })

                    RecentActivity(rides = currentState.lastRides) {
                        nav.navigateToRide(it._id)
                    }

                    UpcomingMaintenance(currentState.selectedBike)
                }
                is GarageScreenState.Loading -> {

//                    Loading()
                }
                else -> {
//                    Text(text = "Sync bikes!")
                }
            }


        }

        PullRefreshIndicator(
            state.value is GarageScreenState.Loading,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}


