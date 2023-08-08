package com.anxops.bkn.ui.screens.rides.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.screens.rides.list.components.BikeRideItem
import com.anxops.bkn.ui.screens.rides.list.components.RideAndBike
import com.anxops.bkn.ui.screens.rides.list.openStravaActivity
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.bgGradient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun RideScreen(
    navigator: DestinationsNavigator,
    viewModel: RideScreenViewModel = hiltViewModel(),
    rideId: String
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(rideId) {
        viewModel.loadRide(rideId)
    }

    if (state.isLoading) {
        Loading()
    } else {
        RideView(state) { bike, _ ->
            viewModel.setRideBike(bike)
        }
    }
}

@Composable
fun RideView(
    state: RideScreenState,
    onBikeConfirm: (Bike, BikeRide) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current

    Box(
        Modifier.fillMaxSize().background(bgGradient())
    ) {
        if (state.ride?.decodedPolyline?.isNotEmpty() == true) {
            state.ride.decodedPolyline?.let { pl ->

                val start = pl.first()
                val end = pl.last()
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(start, 12f)
                }
                var mapProperties by remember {
                    mutableStateOf(
                        MapProperties(
                            maxZoomPreference = 20f,
                            minZoomPreference = 8f,
                            mapType = MapType.NORMAL
                        )
                    )
                }
                var mapUiSettings by remember {
                    mutableStateOf(
                        MapUiSettings(mapToolbarEnabled = true)
                    )
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize().padding(0.dp),
                    cameraPositionState = cameraPositionState,
                    uiSettings = mapUiSettings,
                    properties = mapProperties
                ) {
                    Polyline(
                        points = pl,
                        color = MaterialTheme.colors.secondary,
                        jointType = JointType.ROUND,
                        width = 10.dp.value
                    )

                    Marker(
                        title = "Start",
                        position = start
                    )

                    Marker(
                        title = "End",
                        position = end
                    )
                }
            }
        }

        state.ride?.let { ride ->
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 10.dp).align(Alignment.TopCenter)
            ) {
                BikeRideItem(
                    item = RideAndBike(ride, state.bike),
                    state.allBikes,
                    onClickOpenOnStrava = {
                        ride.stravaId?.let {
                            openStravaActivity(context, it)
                        }
                    },
                    onBikeConfirm = onBikeConfirm
                )
            }
        }
    }
}
