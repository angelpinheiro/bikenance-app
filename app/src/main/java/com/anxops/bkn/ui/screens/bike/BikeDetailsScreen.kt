package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeComponentListItem
import com.anxops.bkn.ui.screens.bike.components.BikeDetailsHeader
import com.anxops.bkn.ui.screens.bike.components.ComponentListBottomSheet
import com.anxops.bkn.ui.screens.garage.components.GarageBikeCard
import com.anxops.bkn.ui.screens.garage.components.OngoingMaintenanceItemV2
import com.anxops.bkn.ui.shared.coloredShadow
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
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing.value, onRefresh = { })

    LaunchedEffect(bikeId) {
        viewModel.loadBike(bikeId)
    }

    val listState = rememberLazyListState()
    val gradient = bgGradient()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val bike = viewModel.bike.collectAsState()
    val components = viewModel.bikeComponents.collectAsState()

    bike.value?.let { bike ->


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
                fab(visible = scaffoldState.bottomSheetState.isCollapsed && false, click = {
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                })
            },
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primaryVariant)
            ) {
//                item {
//                    BikeDetailsHeader(bike = bike)
//                }
//                item {
//                    Box(modifier = Modifier.padding(vertical = 10.dp)) {
//                        GarageBikeCard(bike = bike, tintColor = MaterialTheme.colors.primaryVariant, elevation = 0.dp)
//                    }
//                }

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

                    components.value.groupBy { it.type.group }.toList()
                        .sortedBy { it.first.order }
                        .forEach { (group, items) ->
                            stickyHeader {
                                Text(
                                    text = group.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .coloredShadow()
                                        .background(MaterialTheme.colors.primary)
                                        .padding(10.dp),
                                    color = MaterialTheme.colors.onPrimary,
                                    style = MaterialTheme.typography.h3
                                )
                            }
                            items.forEach { c ->
                                item {
                                    BikeComponentListItem(component = c)
                                }
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


@Composable
fun EmptyComponentList(onClickAction: () -> Unit = {}) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Your bike  does not has any attached components",
            modifier = Modifier.padding(vertical = 30.dp, horizontal = 30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )

        Image(
            painter = painterResource(id = R.drawable.ic_undraw_not_found),
            contentDescription = "Not found",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .background(Color.Transparent)
                .padding(top = 0.dp)
        )

        OutlinedButton(onClick = { onClickAction() }, modifier = Modifier.padding(top = 20.dp)) {
            Text(
                text = "Proceed with initial setup",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h3,
            )
        }
    }
}


@Composable
fun BikeDetailsTopBar(bike: Bike, onAddComponent: () -> Unit = {}) {

    TopAppBar(
        contentPadding = PaddingValues(5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                BknIcon(
                    icon = CommunityMaterial.Icon.cmd_bike,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(20.dp)
                )

                Text(
                    text = bike.name ?: bike.displayName(),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            if (bike.stravaId != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {

                    }) {


                        Image(
                            painter = painterResource(id = R.drawable.ic_strava_logo),
                            contentDescription = null,

                            modifier = Modifier

                                .padding(16.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.primary)
                                .padding(5.dp),
                        )
                    }
                }

            }
        }
    }
}


