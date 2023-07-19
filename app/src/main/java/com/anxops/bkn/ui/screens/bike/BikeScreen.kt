package com.anxops.bkn.ui.screens.bike

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeDetailsTopBar
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.bgGradient
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun BikeScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeScreenViewModel = hiltViewModel(),
    bikeId: String,
    section: String?
) {

    val bknNavigator = BknNavigator(navigator)
    val navController = rememberNavController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    var currentSection by rememberSaveable { mutableStateOf(BikeSections.Status) }

    navController.navigatorProvider.addNavigator(bottomSheetNavigator)

    LaunchedEffect(section) {
        BikeSections.values().firstOrNull { it.id == section }?.let { currentSection = it }
    }

    LaunchedEffect(bikeId) {
        viewModel.handleEvent(BikeScreenEvent.LoadBike(bikeId))
    }

    val state = viewModel.state.collectAsState()

    when (val currentState = state.value) {
        is BikeScreenState.Loaded -> {

            Scaffold(backgroundColor = MaterialTheme.colors.primaryVariant, topBar = {
                BikeDetailsTopBar(bike = currentState.bike, onBikeSetup = {
                    bknNavigator.navigateToBikeSetup(currentState.bike._id)
                }, onClickBack = {
                    bknNavigator.popBackStack()
                })
            }, bottomBar = {
                BikeScreenBottomBar(selectedItem = currentSection, onItemSelected = {
                    currentSection = it
                })
            }, content = { paddingValues ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .background(bgGradient())
                        .padding(paddingValues)
                ) {
                    when (currentSection) {
                        BikeSections.Status -> {
                            BikeScreenStatusView(currentState.bike, currentState.bikeStatus)
                        }

                        BikeSections.Components -> {
                            BikeScreenComponentsView(currentState.bike, currentState.bikeStatus)
                        }
                    }
                }
            })
        }

        BikeScreenState.Loading -> Loading()
    }

}