package com.anxops.bkn.ui.screens.bike

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeComponentDetail
import com.anxops.bkn.ui.screens.bike.components.BikeDetailsTopBar
import com.anxops.bkn.ui.screens.rides.list.openStravaActivity
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.bgGradient
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BikeScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeScreenViewModel = hiltViewModel(),
    bikeId: String,
    section: String?
) {

    val context = LocalContext.current

    val bknNavigator = BknNavigator(navigator)
    val navController = rememberNavController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    var currentSection by rememberSaveable { mutableStateOf(BikeSections.Status) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    navController.navigatorProvider.addNavigator(bottomSheetNavigator)

    val state = viewModel.state.collectAsState()

    LaunchedEffect(bikeId) {
        viewModel.handleEvent(BikeScreenEvent.LoadBike(bikeId))
    }

    LaunchedEffect(section) {
        BikeSections.values().firstOrNull { it.id == section }?.let { currentSection = it }
    }

    LaunchedEffect(state.value.selectedComponent) {
        launch {
            if (state.value.selectedComponent != null) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.collapse()
            }
        }
    }

    LaunchedEffect(scaffoldState.bottomSheetState.isCollapsed) {
        if (scaffoldState.bottomSheetState.isCollapsed) {
            viewModel.handleEvent(BikeScreenEvent.SelectComponent(null))
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.openBikeOnStravaEvent.collect {
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
            openStravaBike(context, it)
        }
    }

    if (state.value.loading) {
        Loading()
    }

    state.value.bike?.let { bike ->
        BottomSheetScaffold(backgroundColor = MaterialTheme.colors.primaryVariant,
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetBackgroundColor = MaterialTheme.colors.primary,
            topBar = {
                BikeDetailsTopBar(bike = bike, onBikeSetup = {
                    bknNavigator.navigateToBikeSetup(bike._id)
                }, onBikeSettings = {
                    bknNavigator.navigateToBikeEdit(bike._id)
                },
                    onClickBack = {
                    bknNavigator.popBackStack()
                })
            },
            sheetContent = {
                state.value.selectedComponent?.let {
                    BikeComponentDetail(component = it, onClose = {
                        scope.launch { scaffoldState.bottomSheetState.collapse() }
                    })
                }
            },
            content = { paddingValues ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .background(bgGradient())
                        .padding(paddingValues)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        when (currentSection) {
                            BikeSections.Status -> {
                                BikeScreenStatusView(
                                    bike,
                                    state.value.selectedComponent,
                                    state.value.selectedCategory,
                                    onEvent = {
                                        viewModel.handleEvent(it)
                                    }
                                )
                            }

                            BikeSections.Components -> {
                                BikeScreenComponentsView(bike)
                            }
                        }
                    }
                    Box(Modifier.fillMaxWidth()) {
                        BikeScreenBottomBar(selectedItem = currentSection, onItemSelected = {
                            currentSection = it
                        })
                    }
                }
            })
    }
}

fun openStravaBike(context: Context, bikeId: String) {
    val url = "https://www.strava.com/bikes/${bikeId.replaceFirst("b", "")}"
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(context, Uri.parse(url))
}