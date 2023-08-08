package com.anxops.bkn.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.ui.shared.components.BknIcon
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

enum class HomeSections(
    val id: String,
    val icon: IIcon,
    @StringRes val titleRes: Int
) {
    Home(
        "HomeScreenDestination",
        CommunityMaterial.Icon2.cmd_home_variant_outline,
        R.string.garage_section_title
    ),
    Rides(
        "RidesScreenDestination",
        CommunityMaterial.Icon.cmd_bike_fast,
        R.string.rides_section_title
    ),
    Maintenances(
        "MaintenancesScreenDestination",
        CommunityMaterial.Icon3.cmd_tools,
        R.string.maintenance_section_title
    )
}

@ExperimentalMaterialNavigationApi
@Composable
fun HomeBottomBar(
    selectedItem: HomeSections,
    onItemSelected: (HomeSections) -> Unit
) {
    BottomNavigation(
        elevation = 32.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        HomeSections.values().forEach { destination ->

            val isCurrent = selectedItem.id == destination.id

            BottomNavigationItem(selected = isCurrent, onClick = {
                onItemSelected(destination)
            }, icon = {
                BknIcon(
                    destination.icon,
                    MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(22.dp).alpha(if (isCurrent) 1f else 0.5f)
                )
            })
        }
    }
}
