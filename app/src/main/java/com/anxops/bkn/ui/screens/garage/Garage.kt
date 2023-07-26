package com.anxops.bkn.ui.screens.garage

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.garage.components.BikesPager
import com.anxops.bkn.ui.screens.garage.components.GarageBikeCard
import com.anxops.bkn.ui.screens.garage.components.RecentActivity
import com.anxops.bkn.ui.screens.garage.components.UpcomingMaintenance
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.theme.strava
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Garage(
    navigator: DestinationsNavigator, viewModel: GarageViewModel = hiltViewModel()
) {
    val nav = BknNavigator(navigator)
    val state = viewModel.screenState.collectAsState()

    val pullRefreshState =
        rememberPullRefreshState(refreshing = state.value is GarageScreenState.Loading,
            onRefresh = { viewModel.loadData() })
    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        when (val currentState = state.value) {
            is GarageScreenState.ShowingGarage -> {
                if (currentState.showSync) {
                    SyncBikes(currentState, viewModel)
                } else {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        BikesPager(bikes = currentState.bikes, onBikeChanged = {
                            viewModel.setSelectedBike(it)
                        }, onEditBike = {
                            nav.navigateToBikeEdit(it._id)
                        }, onBikeDetails = {
                            if (it.configDone) nav.navigateToBike(it._id)
                            else nav.navigateToBikeSetup(it._id)
                        }, onClickSync = {
                            viewModel.onShowOrHideSync(true)
                        })

                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            currentState.lastRides?.let { rides ->
                                RecentActivity(rides = rides) {
                                    nav.navigateToRide(it._id)
                                }
                            }

                            UpcomingMaintenance(currentState.selectedBike)
                        }

                    }

                }


            }

            is GarageScreenState.Loading -> {
                Loading()
            }

            GarageScreenState.ProfileNotSync -> {
                ProfileSyncInProgress()
            }
        }

        PullRefreshIndicator(
            state.value is GarageScreenState.Loading,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun SyncBikes(
    state: GarageScreenState.ShowingGarage, viewModel: GarageViewModel
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "STRAVA bike tracking",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary
            )

            Text(
                text = "We found ${state.allBikes.size} bikes on Strava. Check the ones you want to track to receive maintenance recommendations.",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onPrimary
            )
        }

        Divider(
            Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(0.dp)
        )

        state.allBikes.forEach { bike ->

            GarageBikeCard(bike = bike, elevation = 2.dp, topLeftSlot = {
                Switch(
                    checked = !bike.draft,
                    onCheckedChange = { viewModel.syncBike(bike, it) },
                    colors = SwitchDefaults.colors(

                        checkedThumbColor = MaterialTheme.colors.strava,
                        uncheckedTrackColor = MaterialTheme.colors.primaryVariant,
                        uncheckedThumbColor = MaterialTheme.colors.onPrimary.copy(
                            alpha = 0.8f
                        )
                    )
                )
            }, isLast = true)

            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(0.dp)
            )
        }




        OutlinedButton(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            onClick = { viewModel.finishBikeSync() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

        ) {
            Text(text = "Let's go!", Modifier.padding(5.dp))
        }

        if (state.allBikes.any { !it.draft }) {

            TextButton(
                onClick = { viewModel.onShowOrHideSync(false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)

            ) {
                BknIcon(
                    icon = CommunityMaterial.Icon.cmd_arrow_left,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = "Back",
                    Modifier.padding(5.dp),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}


@Composable
fun ProfileSyncInProgress(onClickAction: () -> Unit = {}) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val infiniteTransition = rememberInfiniteTransition(label = "")

        val size: Dp = infiniteTransition.animateValue(
            label = "",
            initialValue = 30.dp,
            targetValue = 40.dp,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            )
        ).value



        Text(
            text = "Building profile",
            modifier = Modifier.padding(top = 30.dp),
            style = MaterialTheme.typography.h2,
        )

        Text(
            text = "We're fetching your bikes and rides from Strava. It'll be a quick process, and we'll let you know once your profile is ready!",
            modifier = Modifier.padding(horizontal = 30.dp).padding(top = 10.dp, bottom = 50.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )

        Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colors.primary
            )

            Image(
                painter = painterResource(id = R.drawable.ic_strava_logo),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .padding(20.dp)
                    .size(size),
            )

        }
        Text(
            text = "Fetching from Strava...",
            modifier = Modifier.padding(top = 20.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Thin,
            fontSize = 13.sp,
        )
    }
}


