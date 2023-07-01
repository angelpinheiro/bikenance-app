package com.anxops.bkn.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.storage.FakeData
import com.anxops.bkn.ui.components.MaintenanceItemView
import com.anxops.bkn.ui.shared.coloredShadow
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

//@GarageNavGraph
//@Destination
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MaintenancesScreen(
    navigator: DestinationsNavigator,
    viewModel: RidesScreenViewModel = hiltViewModel(),
) {
    val maintenances = FakeData.maintenances

    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colors.primary)
    ) {

        maintenances.groupBy { it.bike }.forEach { (name, items) ->

//            stickyHeader {
//                Text(
//                    text = name + " (${items.size})",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .coloredShadow()
//                        .background(MaterialTheme.colors.secondary)
//                        .padding(16.dp),
//                    color = MaterialTheme.colors.onSecondary,
//                    style = MaterialTheme.typography.h3
//                )
//            }

            item {
                Text(
                    text = name + " (${items.size})",
                    modifier = Modifier
                        .fillMaxWidth()
                        .coloredShadow()
                        .background(MaterialTheme.colors.primaryVariant)
                        .padding(16.dp),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3
                )
            }
            items(items = items, itemContent = {
                MaintenanceItemView(it)
            })
        }


    }


}