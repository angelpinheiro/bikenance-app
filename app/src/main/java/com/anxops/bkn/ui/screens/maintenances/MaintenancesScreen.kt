/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.ui.screens.maintenances

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
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.ui.screens.maintenances.components.MaintenanceItem
import com.anxops.bkn.ui.screens.maintenances.components.MaintenanceItemView
import com.anxops.bkn.ui.screens.rides.list.RidesScreenViewModel
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
    MaintenanceList(FakeData.maintenances)
}

@Composable
fun MaintenanceList(maintenances: List<MaintenanceItem>) {
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