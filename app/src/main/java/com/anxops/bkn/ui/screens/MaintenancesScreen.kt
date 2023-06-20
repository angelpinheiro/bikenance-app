package com.anxops.bkn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.ui.components.MaintenanceItem
import com.anxops.bkn.ui.components.MaintenanceItemView
import com.anxops.bkn.ui.shared.coloredShadow
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

//@GarageNavGraph
//@Destination
@Composable
fun MaintenancesScreen(
    navigator: DestinationsNavigator,
    viewModel: RidesScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    val maintenances = listOf(
        MaintenanceItem(
            bikePart = "Tubeless liquid",
            title = "Check / Refill",
            time = "Two weeks ago",
            percentage = 1f
        ),
        MaintenanceItem(
            bikePart = "FOX 32 Float Performance",
            title = "Full service",
            time = "3 months left",
            percentage = 0.8f
        ),
        MaintenanceItem(
            bikePart = "Shimano XT Chain",
            title = "Check / Replace",
            time = "2000km left",
            percentage = 0.4f
        ),
        MaintenanceItem(
            bikePart = "Front brake pads",
            title = "Check / Replace",
            time = "2 months left",
            percentage = 0.3f
        )
    ).sortedByDescending { it.percentage }

    Column {

        Text(
            text = "Maintenances", modifier = Modifier
                .fillMaxWidth()
                .coloredShadow()
                .background(MaterialTheme.colors.secondary)
                .padding(8.dp),
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.typography.h3
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(items = maintenances, itemContent = {
                MaintenanceItemView(it)
            })
        }

    }


}