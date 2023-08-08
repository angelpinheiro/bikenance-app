package com.anxops.bkn.ui.screens.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.garage.Garage
import com.anxops.bkn.ui.screens.maintenances.MaintenancesScreen
import com.anxops.bkn.ui.screens.rides.list.RidesScreen
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknIcon
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
    section: String?
) {
    val nav = BknNavigator(navigator)

    var selectedItem by rememberSaveable { mutableStateOf(HomeSections.Home) }
    val allowRefresh by viewModel.allowRefreshState.collectAsState()
    val profileSync by viewModel.profileSyncState.collectAsState()

    LaunchedEffect(section) {
        HomeSections.values().firstOrNull { it.id == section }?.let { selectedItem = it }
    }

    LaunchedEffect(LocalContext.current) {
        viewModel.events.collect {
            when (it) {
                is HomeEvent.Logout -> {
                    nav.popBackStack()
                    nav.navigateToSplash()
                }
            }
        }
    }

    Scaffold(backgroundColor = MaterialTheme.colors.primaryVariant, topBar = {
        TopAppBar(
            elevation = 6.dp,
            contentPadding = PaddingValues(5.dp),
            backgroundColor = MaterialTheme.colors.primaryVariant
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BknIcon(
                        icon = selectedItem.icon,
                        color = Color.White,
                        modifier = Modifier.padding(start = 12.dp).size(20.dp)
                    )
                    Text(
                        text = stringResource(id = selectedItem.titleRes),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (selectedItem == HomeSections.Home) {
                        IconButton(onClick = {
                            nav.navigateToProfile()
                        }) {
                            BknIcon(
                                icon = CommunityMaterial.Icon.cmd_account,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp).size(20.dp)
                            )
                        } /*IconButton(onClick = {
                            viewModel.logout()
                        }) {
                            BknIcon(
                                icon = CommunityMaterial.Icon.cmd_exit_to_app,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .size(20.dp)
                            )
                        }*/
                    } else if (selectedItem == HomeSections.Rides) {
                        if (allowRefresh) {
                            IconButton(onClick = {
                                viewModel.refreshRides()
                            }) {
                                BknIcon(
                                    icon = CommunityMaterial.Icon.cmd_cloud_refresh,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp).size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }, bottomBar = {
        if (profileSync == true) {
            HomeBottomBar(selectedItem = selectedItem, onItemSelected = {
                selectedItem = it
            })
        }
    }) {
        BackgroundBox(
            Modifier.padding(it).fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (profileSync == true) {
                when (selectedItem) {
                    HomeSections.Home -> {
                        Garage(navigator = navigator)
                    }

                    HomeSections.Rides -> {
                        RidesScreen(navigator = navigator)
                    }

                    HomeSections.Maintenances -> {
                        MaintenancesScreen(navigator = navigator)
                    }
                }
            } else if (profileSync == false) {
                ProfileSyncInProgress()
            }
        }
    }
}

@Composable
fun ProfileSyncInProgress(onClickAction: () -> Unit = {}) {
    Column(
        Modifier.fillMaxSize().padding(top = 0.dp),
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
            style = MaterialTheme.typography.h2
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
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.primary
            )

            Image(
                painter = painterResource(id = R.drawable.ic_strava_logo),
                contentDescription = null,
                modifier = Modifier.clip(CircleShape).background(MaterialTheme.colors.primary).padding(20.dp).size(size)
            )
        }
        Text(
            text = "Fetching from Strava...",
            modifier = Modifier.padding(top = 20.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Thin,
            fontSize = 13.sp
        )
    }
}
