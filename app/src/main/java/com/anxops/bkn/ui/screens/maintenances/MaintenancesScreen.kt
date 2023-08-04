package com.anxops.bkn.ui.screens.maintenances

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.anxops.bkn.ui.screens.garage.components.UpcomingMaintenances
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun MaintenancesScreen(
    navigator: DestinationsNavigator
) {
    MaintenanceList()
}

@Composable
fun MaintenanceList() {

    var tabIndex = remember { mutableStateOf(0) }

    val tabs = listOf("Critical", "Upcoming", "History")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex.value) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index },
                )
            }
        }
        when (tabIndex.value) {
            0 -> AllUpcomingMaintenances(filter = 1f)
            1 -> AllUpcomingMaintenances(0.75f)
            2 -> AllUpcomingMaintenances()
        }
    }
}

@Composable
fun AllUpcomingMaintenances(filter: Float = 0f) {
    UpcomingMaintenances()
}