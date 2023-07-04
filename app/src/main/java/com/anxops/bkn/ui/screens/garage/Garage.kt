package com.anxops.bkn.ui.screens.garage

import android.util.Log
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.NewBikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.ProfileScreenDestination
import com.anxops.bkn.ui.screens.garage.components.BikesPager
import com.anxops.bkn.ui.screens.garage.components.OngoingMaintenance
import com.anxops.bkn.ui.screens.garage.components.RecentActivity
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
    val state = viewModel.state.collectAsState()
    val rides = viewModel.selectedBikeRides
    val bikes = viewModel.bikes.collectAsState()

    var selectedBike = viewModel.selectedBike.collectAsState()

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
            .background(bgGradient)
            .pullRefresh(pullRefreshState)
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            BikesPager(
                bikes = bikes.value,
                onBikeChanged = {
                    Log.d("onBikeChanged", "Bike changed ${it.name}")
                    viewModel.setSelectedBike(it)
                },
                onEditBike = {
                    nav.navigateToBike(it._id)
                })

            RecentActivity(rides = rides.value)

            selectedBike.value?.let {
                OngoingMaintenance(it)
            }


        }

        PullRefreshIndicator(
            state.value.refreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}


