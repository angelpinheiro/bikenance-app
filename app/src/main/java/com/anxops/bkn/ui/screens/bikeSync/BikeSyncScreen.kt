package com.anxops.bkn.ui.screens.bikeSync

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.garage.components.GarageBikeCard
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.theme.strava
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun BikeSyncScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeSyncViewModel = hiltViewModel()
) {
    val nav = BknNavigator(navigator)
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(state.status) {
        if (state.status == BikeSyncScreenStatus.DONE) {
            nav.popBackStack()
        }
    }

    BackgroundBox {
        when (state.status) {
            BikeSyncScreenStatus.LOADING, BikeSyncScreenStatus.SAVING -> Loading()
            BikeSyncScreenStatus.LOADED -> {
                Column(
                    Modifier.fillMaxSize()
                ) {
                    Column(
                        Modifier.fillMaxWidth().background(MaterialTheme.colors.primaryVariant).padding(26.dp)
                    ) {
                        Text(
                            text = "STRAVA bike tracking",
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.onPrimary
                        )

                        Text(
                            text = "You have ${state.bikes.size} bikes on Strava. Check the ones you want to track to receive maintenance recommendations.",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.onPrimary

                        )
                    }

                    Column(
                        Modifier.fillMaxSize().weight(1f).padding(horizontal = 26.dp).verticalScroll(scrollState),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Divider(
                            Modifier.fillMaxWidth().height(20.dp)
                        )

                        state.bikes.forEach { (bike, sync) ->

                            GarageBikeCard(bike = bike, elevation = 2.dp, topLeftSlot = {
                                Switch(
                                    checked = sync,
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
                                Modifier.fillMaxWidth().padding(6.dp).height(0.dp)
                            )
                        }
                    }

                    Box(
                        Modifier.fillMaxWidth().background(MaterialTheme.colors.primaryVariant).padding(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.performSync() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Let's go!",
                                modifier = Modifier.padding(4.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }

            else -> {}
        }
    }
}
