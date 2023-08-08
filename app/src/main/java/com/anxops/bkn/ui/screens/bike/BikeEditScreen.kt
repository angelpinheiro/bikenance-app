package com.anxops.bkn.ui.screens.bike

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeEditTopBar
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.Message
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknLabelTopTextField
import com.anxops.bkn.ui.shared.components.ErrorDialog
import com.anxops.bkn.ui.shared.components.onBackgroundTextFieldColors
import com.anxops.bkn.ui.theme.statusGood
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
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
                        Loading("Updating bike...")
                    }

                    BikeEditScreenStatus.UpdateSuccess -> {
                        Message(text = "Done!")
                    }

                    BikeEditScreenStatus.LoadFailed -> {
                        Message(text = "Could not load bike")
                    }

                    BikeEditScreenStatus.Editing -> {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(scrollState)
                        ) {

                            BikeDetailsEdit(viewModel, state.value.bike)

                        }

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.primaryVariant)
                                .padding(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.onSaveBike() },
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                modifier = Modifier.fillMaxWidth(),
                                enabled = (state.value.bike.name != null)
                            ) {
                                Text(
                                    text = "Save",
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

    val context = LocalContext.current
    val colors = onBackgroundTextFieldColors()
    val imageData = remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = (ActivityResultContracts.GetContent()),
            onResult = { uri ->
                imageData.value = uri
                uri?.let {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val imageByteArray = inputStream?.readBytes()
                    viewModel.updateBikeImage(imageByteArray)
                }
            })



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Edit your bike details below",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(start = 10.dp),
            color = MaterialTheme.colors.onBackground
        )

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Box {
                IconButton(onClick = {
                    launcher.launch("image/jpeg")
                }) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(bike.photoUrl)
                            .crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(0.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.background),
                        loading = {
                            CircularProgressIndicator(
                                strokeWidth = 5.dp, color = MaterialTheme.colors.statusGood
                            )
                        },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {

            BknLabelTopTextField(value = bike.name,
                label = "Alias",
                colors = colors,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    viewModel.updateName(it)

                })

            BknLabelTopTextField(value = bike.brandName,
                label = "Brand name",
                modifier = Modifier.fillMaxWidth(),
                colors = colors,
                onValueChange = {
                    viewModel.updateBrandName(it)
                })

            BknLabelTopTextField(value = bike.modelName,
                label = "Model name",
                modifier = Modifier.fillMaxWidth(),
                colors = colors,
                onValueChange = {
                    viewModel.updateModel(it)
                })
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
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        BknLabelTopTextField(readOnly = true,
            value = bikeType,
            onValueChange = {},
            label = "Bike Type",
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = onBackgroundTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            BikeType.getAllKnown().forEach { type ->
                DropdownMenuItem(content = { Text(type.extendedType) }, onClick = {
                    onBikeTypeChange(type)
                    expanded = false
                }, contentPadding = PaddingValues(10.dp)
                )
            }
        }
    }
}