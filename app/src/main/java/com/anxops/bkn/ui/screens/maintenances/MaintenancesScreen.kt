package com.anxops.bkn.ui.screens.maintenances

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.ui.screens.garage.components.UpcomingMaintenance
import com.anxops.bkn.ui.screens.maintenances.components.MaintenanceItem
import com.anxops.bkn.ui.screens.rides.list.RidesScreenViewModel
import com.anxops.bkn.ui.shared.components.bgGradient
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

//@GarageNavGraph
//@Destination
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MaintenancesScreen(
    navigator: DestinationsNavigator,
    viewModel: RidesScreenViewModel = hiltViewModel(),
) {
    MaintenanceList(FakeData.maintenances)
}

@Composable
fun MaintenanceList(maintenances: List<MaintenanceItem>) {

    var tabIndex = remember { mutableStateOf(0) }

    val tabs = listOf("Critical", "Ongoing", "History")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex.value) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index },
//                    icon = {
//                        when (index) {
//                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
//                            1 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
//                            2 -> Icon(imageVector = Icons.Default.Settings, contentDescription = null)
//                        }
//                    }
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

@Composable fun AllUpcomingMaintenances(filter: Float = 0f) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(bgGradient())
    ) {
        UpcomingMaintenance(filter = filter)
    }
}