package com.anxops.bkn.ui.screens.maintenances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.ui.screens.maintenances.components.MaintenanceDetail
import com.anxops.bkn.ui.screens.maintenances.components.RevisionFreqEdit
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MaintenanceScreen(
    navigator: DestinationsNavigator,
    viewModel: MaintenanceScreenViewModel = hiltViewModel(),
    bikeId: String,
    maintenanceId: String,
) {

    LaunchedEffect(bikeId, maintenanceId) {
        viewModel.loadMaintenance(bikeId, maintenanceId)
    }

    val state by viewModel.state.collectAsState()

    BackgroundBox {
        state.maintenance?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                BikeComponentInfo(bikeComponent = it.component)
                MaintenanceDetail(item = it.maintenance) { }
                RevisionFreqEdit(frequency = it.maintenance.defaultFrequency)
            }
            ExtendedFloatingActionButton(
                text = { Text("Service") },
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(16.dp),
                icon = {
                    BknIcon(icon = CommunityMaterial.Icon3.cmd_wrench)
                },
                onClick = { /*TODO*/ })
        }
    }
}

@Composable
fun BikeComponentInfo(bikeComponent: BikeComponent) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        BikeComponentIcon(
            type = bikeComponent.type,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .size(130.dp)
                .padding(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primaryVariant)
                .padding(12.dp)
        )
//        Text(
//            text = "${if (bikeComponent.modifier != null) "${bikeComponent.modifier.displayName} " else ""}" + stringResource(
//                bikeComponent.type.resources().nameResId
//            ),
//            color = MaterialTheme.colors.onBackground,
//            style = MaterialTheme.typography.h2,
//            overflow = TextOverflow.Ellipsis,
//            textAlign = TextAlign.Center,
//            maxLines = 1
//        )
    }

}