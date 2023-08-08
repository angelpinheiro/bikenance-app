package com.anxops.bkn.ui.screens.bike

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.ui.shared.components.BknIcon
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

enum class BikeSections(
    val id: String, val icon: IIcon, @StringRes val title: Int
) {
    Status(
        id = "BikeStatusScreenDestination",
        icon = CommunityMaterial.Icon3.cmd_progress_wrench,
        title = R.string.bike_section_status
    ),
    Components(
        id = "BikeComponentsDestination",
        icon = CommunityMaterial.Icon2.cmd_hexagon_multiple,
        title = R.string.bike_section_components
    ),
}

@ExperimentalMaterialNavigationApi
@Composable
fun BikeScreenBottomBar(
    selectedItem: BikeSections, onItemSelected: (BikeSections) -> Unit
) {
    BottomNavigation(
        elevation = 8.dp, backgroundColor = MaterialTheme.colors.primaryVariant
    ) {

        BikeSections.values().forEach { destination ->

            val isCurrent = selectedItem.id == destination.id

            BottomNavigationItem(
                selected = isCurrent,
                onClick = {
                    onItemSelected(destination)
                },
                icon = {
                    BknIcon(
                        destination.icon,
                        MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .size(18.dp)
                            .alpha(if (isCurrent) 1f else 0.5f)
                    )
                },
                label = {
                    Text(
                        stringResource(id = destination.title),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h4,
                        fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Normal,
                        modifier = Modifier.alpha(if (isCurrent) 1f else 0.5f)
                    )
                },
            )
        }
    }
}


