package com.anxops.bkn.ui.screens.bikeSetup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.anxops.bkn.ui.screens.destinations.BikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.HomeScreenDestination
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknIcon
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
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
                    bknNavigator.popBackStackTo(HomeScreenDestination.route, false)
                    bknNavigator.navigateToBike(screenState.bike._id)
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
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    fun scrollToPrevPage(delayMs: Long = 0) {
        scope.launch {
            if (delayMs > 0) delay(delayMs)
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    BackgroundBox {


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primaryVariant).padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { scrollToPrevPage() }) {
                    BknIcon(
                        icon = CommunityMaterial.Icon.cmd_arrow_left,
                        modifier = Modifier.size(42.dp).clip(CircleShape)
                            .background(MaterialTheme.colors.primary).padding(5.dp)
                    )
                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        pageCount,
                        activeColor = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                IconButton(onClick = { scrollToNextPage() }) {
                    BknIcon(
                        icon = CommunityMaterial.Icon.cmd_arrow_right,
                        modifier = Modifier.size(36.dp).clip(CircleShape)
                            .background(MaterialTheme.colors.primary).padding(5.dp)
                    )
                }
            }


            HorizontalPager(
                pageCount = pageCount,
                state = pagerState,
                key = { it },
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(16.dp)
            ) { page ->

                when (page) {

                    0 -> InfoPage(onContinue = {
                        scrollToNextPage()
                    })

                    1 -> BikeTypePage(
                        details = state.details,
                        bike = state.bike,
                        onBikeTypeSelected = {
                            viewModel.onSetupDetailsEvent(BSSEvent.BikeTypeSelected(it))
                            scrollToNextPage(delayMs = 500)
                        })

                    3 -> BikeStatusPage(state.details, onContinue = {
                        scrollToNextPage()
                    }, onLastMaintenanceUpdate = { category, value ->
                        viewModel.onSetupDetailsEvent(
                            BSSEvent.LastMaintenanceUpdate(
                                category, value
                            )
                        )
                    })

                    2 -> BikeDetailsPage(state.details, onFullSuspensionSelectionChange = {
                        viewModel.onSetupDetailsEvent(BSSEvent.FullSuspensionSelectionChange(it))
                    }, onCliplessPedalsSelectionChange = {
                        viewModel.onSetupDetailsEvent(BSSEvent.CliplessPedalsSelectionChange(it))
                    }, onDropperSelectionChange = {
                        viewModel.onSetupDetailsEvent(BSSEvent.DropperSelectionChange(it))
                    }, onTubelessSelectionChange = {
                        viewModel.onSetupDetailsEvent(BSSEvent.TubelessSelectionChange(it))
                    }, onContinue = {
                        scrollToNextPage()
                    })

                    4 -> RidingHabitsPage(details = state.details, stats = state.stats) {
                        viewModel.onFinishBikeSetup()
                    }
                }
            }
        }
    }
}


@Composable
fun BikeSetupTitle(text: String) {
    Text(
        modifier = Modifier.padding(bottom = 10.dp),
        text = text,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h1
    )
}


@Composable
fun BikeSetupDescription(text: String, align: TextAlign = TextAlign.Center) {
    Text(
        modifier = Modifier.padding(bottom = 5.dp),
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


