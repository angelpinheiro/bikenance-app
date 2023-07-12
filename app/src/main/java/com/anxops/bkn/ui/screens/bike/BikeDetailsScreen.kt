package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeComponentTabs
import com.anxops.bkn.ui.screens.bike.components.BikeDetailsTopBar
import com.anxops.bkn.ui.screens.bike.components.BikeStats
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap
import com.anxops.bkn.ui.screens.bike.components.ComponentListBottomSheet
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

    val selectedComponentGroup = remember { mutableStateOf<ComponentCategory?>(null) }

    val bike = viewModel.bike.collectAsState()
    val components = viewModel.bikeComponents.collectAsState()

    bike.value?.let { bike ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient())
        ) {
            BottomSheetScaffold(
                topBar = {
                    BikeDetailsTopBar(bike = bike, onAddComponent = {
                        scope.launch { scaffoldState.bottomSheetState.expand() }
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
                            viewModel.addSelectedComponentsToBike()
                        })
                },
                backgroundColor = MaterialTheme.colors.primary,
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
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    item {
                        Column(
                        Modifier.background(MaterialTheme.colors.primaryVariant)
                        ) {
                            BikeStats(bike)
                            Column(
                                Modifier
                                    .padding(horizontal = 10.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                BikeStatusMap(
                                    highlightedGroup = selectedComponentGroup.value,
                                    bikeType = bike.type,
                                    showComponentGroups = false
                                )
                            }
                        }
                    }
                    if (components.value.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                            ) {
                                EmptyComponentList(onClickAction = {
                                    scope.launch { scaffoldState.bottomSheetState.expand() }
                                })
                            }
                        }
                    } else {

                        item {
                            BikeComponentTabs(components = components.value, onGroupChange = {
                                selectedComponentGroup.value = it
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




