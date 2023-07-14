package com.anxops.bkn.ui.screens.bikeSetup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
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
import com.google.accompanist.pager.VerticalPagerIndicator
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

    val pagerState = rememberPagerState()
    val pageCount = 5


    val scope = rememberCoroutineScope()
    fun scrollToNext(delayMs: Long = 0) {
        scope.launch {
            if (delayMs > 0) delay(delayMs)
            pagerState.animateScrollToPage(pagerState.settledPage + 1)
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            VerticalPager(
                pageCount = pageCount,
                state = pagerState,
                key = { it },
            ) { page ->

                when (page) {

                    0 -> InfoPage(onContinue = {
                        scrollToNext()
                    })

                    1 -> BikeTypePage(details = state.details,
                        bike = state.bike,
                        onBikeTypeSelected = {
                            viewModel.onBikeTypeSelected(it)
                            scrollToNext(delayMs = 300)
                        })

                    2 -> BikeStatusPage(state.details, onContinue = {
                        scrollToNext()
                    }, onWearLevelUpdate = { category, value ->
                        viewModel.onWearLevelUpdate(category, value)
                    })

                    3 -> BikeDetailsPage(state.details,
                        onCliplessPedalsSelectionChange = {
                            viewModel.onCliplessPedalsSelectionChange(it)
                        },
                        onDropperSelectionChange = {
                            viewModel.onDropperSelectionChange(it)
                        },
                        onTubelessSelectionChange = {
                            viewModel.onTubelessSelectionChange(it)
                        },
                        onContinue = {
                            scrollToNext()
                        })

                    4 -> RidingHabitsPage(details = state.details, stats = state.stats) {
                        viewModel.finishBikeSetup()
                    }
                }

            }
        }

        VerticalPagerIndicator(
            pagerState = pagerState,
            pageCount,
            activeColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterStart)
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


