package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bike.components.BikeStats
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap
import com.anxops.bkn.ui.screens.bike.components.ComponentTabHeaders


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikeScreenStatusView(
    bike: Bike, bikeStatus: BikeStatus
) {

    val scrollState = rememberLazyListState()
    val selectedComponentCategory = remember { mutableStateOf<ComponentCategory?>(null) }
    val selectedTab = remember { mutableStateOf(ComponentTabHeaders.TRANSMISSION) }
    val highlightCategories = remember { mutableStateOf(true) }

    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Bike statistics",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 0.dp),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary
            )
        }
        item {
            BikeStats(bike = bike)
        }
        item {
            Text(
                text = "Bike status",
                modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 0.dp),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary
            )
        }
        stickyHeader {
            Box(
                Modifier
                    .background(MaterialTheme.colors.primaryVariant)
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                BikeStatusMap(highlightedGroup = selectedComponentCategory.value,
                    bike = bike,
                    bikeStatus = bikeStatus,
                    highlightCategories = highlightCategories.value,
                    onCategorySelected = {
                        highlightCategories.value = false
                        selectedComponentCategory.value = it
                        ComponentTabHeaders.values().firstOrNull { h -> h.category == it }
                            ?.let { th ->
                                selectedTab.value = th
                            }
                    },
                    onCategoryUnselected = {
                        selectedComponentCategory.value = null
                        highlightCategories.value = true
                    })
            }
        }
    }
}



