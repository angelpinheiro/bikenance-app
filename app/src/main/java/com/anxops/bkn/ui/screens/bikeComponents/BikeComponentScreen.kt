package com.anxops.bkn.ui.screens.bikeComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.RevisionFrequency
import com.anxops.bkn.ui.screens.bike.components.BikeStat
import com.anxops.bkn.ui.screens.bikeComponents.components.BikeComponentTopBar
import com.anxops.bkn.ui.screens.bikeComponents.components.MaintenanceDetail
import com.anxops.bkn.ui.screens.bikeComponents.components.RevisionFreqEdit
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.bikeComponentName
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.util.formatAsDate
import com.anxops.bkn.util.formatElapsedTimeUntilNow
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun BikeComponentScreen(
    navigator: ResultBackNavigator<Boolean>,
    viewModel: BikeComponentScreenViewModel = hiltViewModel(),
    bikeId: String,
    componentId: String
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    val bottomSheetExpanded by remember {
        derivedStateOf {
            state is BikeComponentScreenState.Loaded && (state as BikeComponentScreenState.Loaded).editingMaintenance != null
        }
    }

    LaunchedEffect(bikeId, componentId) {
        viewModel.loadComponent(bikeId, componentId)
    }

    LaunchedEffect(bottomSheetExpanded) {
        if (bottomSheetExpanded && scaffoldState.bottomSheetState.isCollapsed) {
            scope.launch { scaffoldState.bottomSheetState.expand() }
        } else if (!bottomSheetExpanded && scaffoldState.bottomSheetState.isExpanded) {
            scope.launch { scaffoldState.bottomSheetState.collapse() }
        }
    }

    when (val currentState = state) {
        is BikeComponentScreenState.Loading -> Loading()
        is BikeComponentScreenState.Error -> Text(text = "Error")
        is BikeComponentScreenState.Loaded -> {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 0.dp,
                sheetBackgroundColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f),
                topBar = {
                    BikeComponentTopBar(component = currentState.component, onClickBack = {
                        scope.launch {
                            navigator.navigateBack()
                        }
                    })
                },
                sheetContent = {
                    Box(
                        // this box acts as an overlay
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.6f))
                            .padding(top = 1.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            color = MaterialTheme.colors.primary
                        ) {
                            currentState.editingMaintenance?.let { m ->
                                MaintenanceEdit(maintenance = m.editingMaintenance, original = m.original, onFrequencyChange = {
                                    viewModel.onMaintenanceFreqUpdate(it)
                                }, onWearChange = {
                                    viewModel.onMaintenanceWearUpdate(it)
                                }, onSaveChanges = {
                                    viewModel.onConfirmEdit()
                                }, onClose = {
                                    viewModel.onMaintenanceEdit(null)
                                })
                            }
                        }
                    }
                }
//                floatingActionButton = {
//                    ExtendedFloatingActionButton(text = { Text("Replace component") }, icon = {
//                        BknIcon(icon = CommunityMaterial.Icon3.cmd_wrench)
//                    }, onClick = { })
//                },
            ) {
                BackgroundBox(contentAlignment = Alignment.TopCenter) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(it)
                    ) {
                        Column(
                            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth().padding(16.dp).weight(1f)
                        ) {
                            Row(
                                Modifier.padding(top = 0.dp).fillMaxWidth().padding(vertical = 10.dp).clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly

                            ) {
                                BikeStat(
                                    title = stringResource(R.string.component_mounted_on_label),
                                    value = "${currentState.bike.displayName()}"
                                )
                                BikeStat(
                                    title = stringResource(R.string.component_installation_date_label),
                                    value = "${currentState.component.from?.formatAsDate()}"
                                )
                            }

                            Row(
                                Modifier.padding(top = 0.dp).fillMaxWidth().padding(vertical = 10.dp).clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly

                            ) {
                                BikeStat(
                                    title = stringResource(R.string.component_distance_label),
                                    value = "${currentState.component.displayDistance()}"
                                )
                                BikeStat(
                                    title = stringResource(R.string.component_duration_label),
                                    value = "${currentState.component.from?.formatElapsedTimeUntilNow()}"
                                )
                                BikeStat(
                                    title = stringResource(R.string.component_active_hours_label),
                                    value = "${currentState.component.displayDuration()}"
                                )
                            }

                            currentState.maintenances.forEach { m ->
                                MaintenanceDetail(item = m) {
                                    viewModel.onMaintenanceEdit(m)
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primaryVariant).padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                onClick = { viewModel.onComponentReplace() }
                            ) {
                                Text(text = stringResource(R.string.replace_component_button_text), Modifier.padding(5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MaintenanceEdit(
    maintenance: Maintenance,
    onFrequencyChange: (RevisionFrequency) -> Unit,
    onWearChange: (Double) -> Unit,
    original: Maintenance,
    onSaveChanges: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            BikeComponentIcon(
                type = maintenance.componentType,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(32.dp).clip(CircleShape).background(MaterialTheme.colors.surface).padding(5.dp)
            )

            Text(
                stringResource(id = maintenance.type.resources().nameResId),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.weight(1f).padding(horizontal = 10.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            IconButton(onClick = { onClose() }) {
                BknIcon(icon = CommunityMaterial.Icon.cmd_close)
            }
        }

        RevisionFreqEdit(
            editing = maintenance,
            frequency = maintenance.defaultFrequency,
            original = original.defaultFrequency,
            onFrequencyChange = onFrequencyChange,
            onSaveChanges = onSaveChanges
        )
    }
}

@Composable
fun WearEdit(currentWear: Double, onUpdate: (Double) -> Unit) {
    Slider(
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colors.surface,
            thumbColor = MaterialTheme.colors.surface,
            inactiveTrackColor = MaterialTheme.colors.primaryVariant,
            inactiveTickColor = MaterialTheme.colors.surface.copy(alpha = 0.5f),
            activeTickColor = MaterialTheme.colors.surface
        ),
        value = currentWear.toFloat(),
        onValueChange = { onUpdate(it.toDouble()) }
    )
}

@Composable
fun BikeComponentInfo(bikeComponent: BikeComponent) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BikeComponentIcon(
                type = bikeComponent.type,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.align(Alignment.Center).size(120.dp).padding(24.dp).clip(CircleShape)
                    .background(MaterialTheme.colors.primaryVariant).padding(16.dp)
            )
        }
        Text(
            text = "Component type",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = bikeComponentName(component = bikeComponent),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = "Component alias",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 10.dp)
        )

        Text(
            text = bikeComponent.alias ?: "--",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Text(
            text = "Installation date",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = bikeComponent.from?.formatAsDate() ?: "--",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
