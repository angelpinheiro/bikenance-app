package com.anxops.bkn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.model.Bike
import com.anxops.bkn.ui.components.Ride
import com.anxops.bkn.ui.shared.Loading
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.maps.android.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun RideScreen(
    navigator: DestinationsNavigator,
    viewModel: RideScreenViewModel = hiltViewModel(),
    rideId: String
) {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState()
    val bikes = viewModel.bikes.collectAsState()

    LaunchedEffect(rideId) {
        viewModel.loadRide(rideId)
    }

    when (state.value) {
        is RideScreenState.RideLoading -> {
            Loading()
        }
        is RideScreenState.RideLoaded -> {

            val loaded = state.value as RideScreenState.RideLoaded



            BottomSheetScaffold(
                modifier = Modifier.fillMaxSize(),
                sheetContent = {
                    Text(
                        text = "Ride performed with...",
                        modifier = Modifier.padding(10.dp),
                        style = MaterialTheme.typography.h5
                    )
                    bikes.value.forEach {

                        val selected = loaded.ride.bikeId == it._id

                        Text(text = it.name ?: "",
                            style = MaterialTheme.typography.h3,
                            color = if (selected) MaterialTheme.colors.onSecondary
                            else MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .background(
                                    if (selected) MaterialTheme.colors.secondary
                                    else MaterialTheme.colors.surface
                                )
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    scope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                        viewModel.setRideBike(it)
                                    }
                                }
                        )
                    }
                },
                scaffoldState = bottomSheetScaffoldState,
                sheetPeekHeight = 0.dp,
            ) {
                RideView(loaded, bikes.value, onClickRideBike = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                })
            }


        }
        is RideScreenState.RideNotFound -> {
            Text("Ride not found", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }
    }

}


@Composable
fun RideView(
    state: RideScreenState.RideLoaded,
    bikes: List<Bike>,
    onClickRideBike: () -> Unit = {}
) {

    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {

        Ride(ride = state.ride, bikes = bikes, onClickOpenOnStrava = {
            state.ride.stravaId?.let {
                openStravaActivity(context, it)
            }
        }, onClickRideBike = onClickRideBike)

        if(state.polyline?.isNotEmpty() == true) {

            state.polyline.let { pl ->

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
                            mapType = MapType.HYBRID
                        )
                    )
                }
                var mapUiSettings by remember {
                    mutableStateOf(
                        MapUiSettings(mapToolbarEnabled = true)
                    )
                }
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .weight(1f),
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
                        position = end,
                    )

                }
            }
        }

    }

}