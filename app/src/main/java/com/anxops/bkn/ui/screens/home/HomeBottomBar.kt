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

package com.anxops.bkn.ui.screens.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.anxops.bkn.ui.shared.components.BknIcon
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@ExperimentalMaterialNavigationApi
@Composable
fun HomeBottomBar(
    selectedItem: HomeSections,
    onItemSelected: (HomeSections) -> Unit
) {
    BottomNavigation(
        elevation = 5.dp
    ) {

        HomeSections.values().forEach { destination ->

            val isCurrent = selectedItem.id == destination.id

            BottomNavigationItem(
                selected = isCurrent,
                onClick = {
                    onItemSelected(destination)
                },
                icon = {


                    BadgedBox(badge = {
                        if (destination.title.contains("Maintenance")) {
                            Badge {
                                Text("3")
                            }
                        }
                    }) {
                        BknIcon(
                            destination.icon,
                            MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .size(18.dp)
                        )
                    }
                },
                label = {

                    Text(
                        destination.title,
                        color = MaterialTheme.colors.onPrimary,
                        style = if (isCurrent) MaterialTheme.typography.h5 else MaterialTheme.typography.h4,
                        modifier = Modifier.alpha(if (isCurrent) 1f else 0.5f)
                    )
                },

                )
        }
    }
}


enum class HomeSections(
    val id: String,
    val icon: IIcon,
    val title: String
) {
    Home("HomeScreenDestination", CommunityMaterial.Icon.cmd_bike, "Garage"),
    Rides("RidesScreenDestination", CommunityMaterial.Icon.cmd_bike_fast, "Rides"),
    Maintenances(
        "MaintenancesScreenDestination",
        CommunityMaterial.Icon.cmd_cog,
        "Maintenance"
    ),
}
