package com.anxops.bkn.ui.screens.bike

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeEditTopBar
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.Message
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknLabelTopTextField
import com.anxops.bkn.ui.shared.components.CircularImagePicker
import com.anxops.bkn.ui.shared.components.ErrorDialog
import com.anxops.bkn.ui.shared.components.onBackgroundTextFieldColors
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination
@Composable
fun BikeEditScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeEditScreenViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    bikeId: String = ""
) {
    val bknNavigator = BknNavigator(navigator)
    val scrollState = rememberScrollState()

    val state = viewModel.state.collectAsState()

    LaunchedEffect(bikeId) {
        if (bikeId.isNotBlank()) {
            viewModel.loadBike(bikeId)
        }
    }

    LaunchedEffect(state.value.status) {
        if (state.value.status == BikeEditScreenStatus.UpdateSuccess) {
            bknNavigator.popBackStack()
        }
    }

    Scaffold(topBar = {
        BikeEditTopBar(bike = state.value.bike, onBikeDelete = {
            viewModel.deleteBike()
        }, onClickBack = {
            bknNavigator.popBackStack()
        })
    }) { padding ->

        BackgroundBox(Modifier.padding(padding), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                when (state.value.status) {
                    BikeEditScreenStatus.Loading -> {
                        Loading(color = Color.Transparent)
                    }

                    BikeEditScreenStatus.Saving -> {
                        Loading(stringResource(R.string.updating_bike_message))
                    }

                    BikeEditScreenStatus.UpdateSuccess -> {
                        Message(text = stringResource(id = R.string.done_message))
                    }

                    BikeEditScreenStatus.LoadFailed -> {
                        Message(text = stringResource(R.string.could_not_load_bike_message))
                    }

                    BikeEditScreenStatus.Editing -> {
                        Column(
                            modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(scrollState)
                        ) {
                            BikeDetailsEdit(viewModel, state.value.bike)
                        }

                        Box(
                            Modifier.fillMaxWidth().background(MaterialTheme.colors.primaryVariant).padding(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.onSaveBike() },
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                modifier = Modifier.fillMaxWidth(),
                                enabled = (state.value.bike.name != null)
                            ) {
                                Text(
                                    text = stringResource(R.string.save_bike_button_text),
                                    modifier = Modifier.padding(4.dp),
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        state.value.error?.let {
            ErrorDialog(appError = it, bknNavigator = bknNavigator) {
                viewModel.onDismissError()
                navigator.popBackStack()
            }
        }
    }
}

@Composable
fun BikeDetailsEdit(viewModel: BikeEditScreenViewModel, bike: Bike) {
    val colors = onBackgroundTextFieldColors()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.bike_details_edit_message),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(start = 10.dp),
            color = MaterialTheme.colors.onBackground
        )

        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            CircularImagePicker(url = bike.photoUrl, defaultImageResId = R.drawable.default_bike_image, onError = {
                viewModel.onUpdateBikeImageError()
            }) {
                viewModel.updateBikeImage(it)
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            BknLabelTopTextField(value = bike.name, label = "Alias", colors = colors, modifier = Modifier.fillMaxWidth(), onValueChange = {
                viewModel.updateName(it)
            })

            BknLabelTopTextField(
                value = bike.brandName,
                label = stringResource(R.string.bike_edit_brand_name_label),
                modifier = Modifier.fillMaxWidth(),
                colors = colors,
                onValueChange = {
                    viewModel.updateBrandName(it)
                }
            )

            BknLabelTopTextField(
                value = bike.modelName,
                label = stringResource(R.string.bike_edit_model_name_label),
                modifier = Modifier.fillMaxWidth(),
                colors = colors,
                onValueChange = {
                    viewModel.updateModel(it)
                }
            )
            BikeTypeDropDown(bike.type.extendedType) {
                viewModel.updateBikeType(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BikeTypeDropDown(bikeType: String, onBikeTypeChange: (BikeType) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        BknLabelTopTextField(
            readOnly = true,
            value = bikeType,
            onValueChange = {},
            label = stringResource(R.string.bike_edit_bike_type_label),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = onBackgroundTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            BikeType.getAllKnown().forEach { type ->
                DropdownMenuItem(
                    content = { Text(type.extendedType) },
                    onClick = {
                        onBikeTypeChange(type)
                        expanded = false
                    },
                    contentPadding = PaddingValues(10.dp)
                )
            }
        }
    }
}
