/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.NewBikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.ProfileScreenDestination
import com.anxops.bkn.ui.screens.garage.components.BikesPager
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


