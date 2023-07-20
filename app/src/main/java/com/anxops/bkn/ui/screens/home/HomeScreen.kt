package com.anxops.bkn.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.NewBikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.ProfileScreenDestination
import com.anxops.bkn.ui.screens.garage.Garage
import com.anxops.bkn.ui.screens.maintenances.MaintenancesScreen
import com.anxops.bkn.ui.screens.rides.list.RidesScreen
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknIcon
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient

@Destination
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
    bikeUpdateResult: ResultRecipient<NewBikeScreenDestination, Boolean>,
    profileUpdateResult: ResultRecipient<ProfileScreenDestination, Boolean>,
    section: String?
) {
    val navController = rememberNavController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider.addNavigator(bottomSheetNavigator)

    var selectedItem by rememberSaveable { mutableStateOf(HomeSections.Home) }

    LaunchedEffect(section) {
        HomeSections.values().firstOrNull { it.id == section }?.let { selectedItem = it }
    }

    val nav = BknNavigator(navigator)
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
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(20.dp)
                    )
                    Text(
                        text = selectedItem.title,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        nav.navigateToProfile()
                    }) {
                        BknIcon(
                            icon = CommunityMaterial.Icon.cmd_account,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(20.dp)
                        )
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        nav.popBackStack()
                        nav.navigateToSplash()
                    }) {
                        BknIcon(
                            icon = CommunityMaterial.Icon.cmd_exit_to_app,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(20.dp)
                        )
                    }
                }
            }
        }
    }, bottomBar = {
        HomeBottomBar(selectedItem = selectedItem, onItemSelected = {
            selectedItem = it
        })
    }) {
        BackgroundBox(
            Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

            when (selectedItem) {
                HomeSections.Home -> {
                    Garage(navigator = navigator, bikeUpdateResult, profileUpdateResult)
                }

                HomeSections.Rides -> {
                    RidesScreen(navigator = navigator)
                }

                HomeSections.Maintenances -> {
                    MaintenancesScreen(navigator = navigator)
                }
            }
        }
    }
}