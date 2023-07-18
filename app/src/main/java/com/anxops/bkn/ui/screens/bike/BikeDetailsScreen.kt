package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeComponentTabsV2
import com.anxops.bkn.ui.screens.bike.components.BikeDetailsTopBar
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap
import com.anxops.bkn.ui.screens.bike.components.ComponentListBottomSheet
import com.anxops.bkn.ui.screens.bike.components.ComponentTabHeaders
import com.anxops.bkn.ui.screens.bike.components.EmptyComponentList
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.bgGradient
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Destination
@Composable
fun BikeDetailsScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeDetailsScreenViewModel = hiltViewModel(),
    bikeId: String = ""
) {
    val bknNav = BknNavigator(navigator)
    val isRefreshing = remember { mutableStateOf(false) }

    LaunchedEffect(bikeId) {
        viewModel.loadBike(bikeId)
    }

    val listState = rememberLazyListState()
    val gradient = bgGradient()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scrollState = rememberScrollState()

    val selectedComponentCategory = remember { mutableStateOf<ComponentCategory?>(null) }
    val selectedTab = remember { mutableStateOf(ComponentTabHeaders.GENERAL) }
    val highlightCategories = remember { mutableStateOf(true) }

    val bike = viewModel.bike.collectAsState()
    val components = bike.value?.bike?.components ?: emptyList()

    bike.value?.let { bikeWithStatus ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient())
        ) {
            BottomSheetScaffold(
                topBar = {
                    BikeDetailsTopBar(bike = bikeWithStatus.bike, onBikeSetup = {
                        // scope.launch { scaffoldState.bottomSheetState.expand() }
                        bknNav.navigateToBikeSetup(bikeWithStatus.bike._id)
                    })
                },
                sheetContent = {
                    ComponentListBottomSheet(
                        selectedComponents = viewModel.selectedComponentTypes.value,
                        selectable = true,
                        onSelectConfiguration = {
                            viewModel.onSelectConfiguration(it)
                        },
                        onSelectionChanged = {
                            viewModel.onComponentTypeSelectionChange(it)
                        },
                        onDone = {
                            scope.launch { scaffoldState.bottomSheetState.collapse() }
//                            viewModel.addSelectedComponentsToBike()
                        })
                },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                sheetBackgroundColor = MaterialTheme.colors.surface,
                scaffoldState = scaffoldState,
                sheetShape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                sheetPeekHeight = 0.dp,
                floatingActionButton = {
                    fab(visible = scaffoldState.bottomSheetState.isCollapsed, click = {
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    })
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                ) {


                    if (components.isEmpty()) {
                        EmptyComponentList(onClickAction = {
                            scope.launch {
                                bknNav.navigateToBikeSetup(bikeWithStatus.bike._id)
                            }
                        })
                    } else {

                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.verticalScroll(scrollState)
                        ) {
                            Box(
                                Modifier
                                    .background(MaterialTheme.colors.primaryVariant)
                            ) {
                                BikeStatusMap(
                                    highlightedGroup = selectedComponentCategory.value,
                                    bike = bikeWithStatus.bike,
                                    bikeStatus = bikeWithStatus.status,
                                    highlightCategories = highlightCategories.value,
                                    onCategorySelected = {
                                        highlightCategories.value = false
                                        selectedComponentCategory.value = it
                                        ComponentTabHeaders.values()
                                            .firstOrNull { h -> h.category == it }?.let { th ->
                                                selectedTab.value = th
                                            }
                                    }
                                )
                            }
                            BikeComponentTabsV2(
                                bike = bikeWithStatus.bike,
                                selectedTab = selectedTab.value,
                                components = components,
                                onTabChange = { tab ->
                                    selectedTab.value = tab
                                    tab.category?.let { selectedComponentCategory.value = it }
                                    highlightCategories.value = tab.category == null

                                })
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun fab(visible: Boolean = true, click: () -> Unit = {}) {
    Box {
        if (visible) {
            FloatingActionButton(onClick = {
                click()
            }) {
                BknIcon(
                    icon = CommunityMaterial.Icon3.cmd_plus,
                    color = Color.White,
                    modifier = Modifier
                        .size(26.dp)
                )
            }
        }
    }
}




