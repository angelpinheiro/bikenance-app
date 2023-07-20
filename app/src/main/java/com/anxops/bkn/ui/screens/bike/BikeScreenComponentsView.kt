package com.anxops.bkn.ui.screens.bike

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bike.components.BikeComponentTabsV2
import com.anxops.bkn.ui.screens.bike.components.ComponentTabHeaders

@Composable
fun BikeScreenComponentsView(bike: Bike) {

    val selectedTab =
        remember { mutableStateOf(ComponentTabHeaders.TRANSMISSION) }
    val selectedComponentCategory =
        remember { mutableStateOf<ComponentCategory?>(null) }
    val highlightCategories = remember { mutableStateOf(true) }

    BikeComponentTabsV2(bike = bike,
        status = bike.status,
        selectedTab = selectedTab.value,
        onTabChange = { tab ->
            selectedTab.value = tab
            tab.category?.let { selectedComponentCategory.value = it }
            highlightCategories.value = tab.category == null

        })


}