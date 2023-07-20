package com.anxops.bkn.ui.screens.bikeSetup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bikeSetup.components.BikeDetailsPage
import com.anxops.bkn.ui.screens.bikeSetup.components.BikeStatusPage
import com.anxops.bkn.ui.screens.bikeSetup.components.BikeTypePage
import com.anxops.bkn.ui.screens.bikeSetup.components.InfoPage
import com.anxops.bkn.ui.screens.bikeSetup.components.RidingHabitsPage
import com.anxops.bkn.ui.shared.Loading
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
@Destination
fun BikeSetupScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeSetupViewModel = hiltViewModel(),
    bikeId: String = ""
) {

    val bknNavigator = BknNavigator(navigator)
    val state = viewModel.state.collectAsState()

    LaunchedEffect(bikeId) {
        viewModel.loadBike(bikeId)
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant)
            .padding(0.dp),
        verticalArrangement = Arrangement.Center
    ) {

        when (val screenState = state.value) {
            is BikeSetupScreenState.SetupInProgress -> {
                SetupDetailsPager(viewModel, screenState)
            }

            is BikeSetupScreenState.Error -> {
                Text(text = screenState.text)
            }

            is BikeSetupScreenState.SetupDone -> {
                LaunchedEffect(state) {
                    bknNavigator.popBackStack()
                    bknNavigator.navigateToBikeDetails(screenState.bike._id)
                }
            }

            else -> {
                Loading()
            }
        }


    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupDetailsPager(viewModel: BikeSetupViewModel, state: BikeSetupScreenState.SetupInProgress) {

    val pageCount = 5
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    fun scrollToNextPage(delayMs: Long = 0) {
        scope.launch {
            if (delayMs > 0) delay(delayMs)
            pagerState.animateScrollToPage(pagerState.settledPage + 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 50.dp).align(Alignment.Center)
        ) {
            HorizontalPager(
                pageCount = pageCount,
                state = pagerState,
                key = { it },
            ) { page ->

                when (page) {

                    0 -> InfoPage(onContinue = {
                        scrollToNextPage()
                    })

                    1 -> BikeTypePage(details = state.details,
                        bike = state.bike,
                        onBikeTypeSelected = {
                            viewModel.onSetupDetailsEvent(BSSEvent.BikeTypeSelected(it))
                            scrollToNextPage(delayMs = 300)
                        })

                    2 -> BikeStatusPage(state.details, onContinue = {
                        scrollToNextPage()
                    }, onLastMaintenanceUpdate = { category, value ->
                        viewModel.onSetupDetailsEvent(
                            BSSEvent.LastMaintenanceUpdate(
                                category,
                                value
                            )
                        )
                    })

                    3 -> BikeDetailsPage(state.details,
                        onFullSuspensionSelectionChange = {
                            viewModel.onSetupDetailsEvent(BSSEvent.FullSuspensionSelectionChange(it))
                        },
                        onCliplessPedalsSelectionChange = {
                            viewModel.onSetupDetailsEvent(BSSEvent.CliplessPedalsSelectionChange(it))
                        },
                        onDropperSelectionChange = {
                            viewModel.onSetupDetailsEvent(BSSEvent.DropperSelectionChange(it))
                        },
                        onTubelessSelectionChange = {
                            viewModel.onSetupDetailsEvent(BSSEvent.TubelessSelectionChange(it))
                        },
                        onContinue = {
                            scrollToNextPage()
                        })

                    4 -> RidingHabitsPage(details = state.details, stats = state.stats) {
                        viewModel.onFinishBikeSetup()
                    }
                }

            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount,
            activeColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.TopCenter)
        )

    }
}

@Composable
fun BikeSetupTitle(text: String) {
    Text(
        modifier = Modifier.padding(bottom = 20.dp),
        text = text,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h1
    )
}


@Composable
fun BikeSetupDescription(text: String, align: TextAlign = TextAlign.Center) {
    Text(
        modifier = Modifier,
        text = text,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h2,
        textAlign = align,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BikeSetupText(text: String) {
    Text(
        modifier = Modifier,
        text = text,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BikeSetupDivider(size: Dp) {
    Box(Modifier.height(size))
}


