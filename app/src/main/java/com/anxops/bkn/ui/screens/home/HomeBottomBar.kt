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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.mock.FakeData
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

        val critical = FakeData.maintenances.filter { it.percentage >= 1 }.size

        HomeSections.values().forEach { destination ->

            val isCurrent = selectedItem.id == destination.id

            BottomNavigationItem(
                selected = isCurrent,
                onClick = {
                    onItemSelected(destination)
                },
                icon = {


                    BadgedBox(badge = {
//                        if (destination.title.contains("Maintenance")) {
//                            Badge {
//                                Text("$critical")
//                            }
//                        }
                    }) {
                        BknIcon(
                            destination.icon,
                            MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .size(22.dp).alpha(if (isCurrent) 1f else 0.5f)
                        )
                    }
                },
//                label = {
//
//                    Text(
//                        destination.title,
//                        color = MaterialTheme.colors.onPrimary,
//                        style = MaterialTheme.typography.h4,
//                        fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Normal,
//                        modifier = Modifier.alpha(if (isCurrent) 1f else 0.5f)
//                    )
//                },

                )
        }
    }
}


enum class HomeSections(
    val id: String,
    val icon: IIcon,
    val title: String
) {
    Home("HomeScreenDestination", CommunityMaterial.Icon2.cmd_home_variant_outline, "Garage"),
    Rides("RidesScreenDestination", CommunityMaterial.Icon.cmd_bike_fast, "Rides"),
    Maintenances(
        "MaintenancesScreenDestination",
        CommunityMaterial.Icon3.cmd_tools,
        "Maintenance"
    ),
}
