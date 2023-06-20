package com.anxops.bkn.ui.components

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
import com.anxops.bkn.ui.shared.BknIcon
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@ExperimentalMaterialNavigationApi
@Composable
fun GarageBottomBar(
    selectedItem: GarageSections,
    onItemSelected: (GarageSections) -> Unit
) {
    BottomNavigation(
        elevation = 5.dp
    ) {

        GarageSections.values().forEach { destination ->

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


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialNavigationApi::class)
enum class GarageSections(
    val id: String,
    val icon: IIcon,
    val title: String
) {
    Home("HomeScreenDestination", CommunityMaterial.Icon.cmd_bike, "Bikes"),
    Rides("RidesScreenDestination", CommunityMaterial.Icon.cmd_bike_fast, "Rides"),
    Maintenances(
        "MaintenancesScreenDestination",
        CommunityMaterial.Icon.cmd_cog,
        "Maintenance"
    ),
}
