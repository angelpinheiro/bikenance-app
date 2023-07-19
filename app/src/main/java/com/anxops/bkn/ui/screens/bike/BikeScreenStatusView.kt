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
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bike.components.BikeComponentListItem
import com.anxops.bkn.ui.screens.bike.components.BikeStats
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikeScreenStatusView(
    bike: Bike, bikeStatus: BikeStatus
) {

    val scrollState = rememberLazyListState()
    val selectedCategory = remember { mutableStateOf<ComponentCategory?>(null) }
    val selectedComponent = remember { mutableStateOf<BikeComponent?>(null) }
    val highlightCategories = remember { mutableStateOf(true) }

    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "${bike.fullDisplayName()}",
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
                BikeStatusMap(
                    bike = bike,
                    bikeStatus = bikeStatus,
                    selectedCategory = selectedCategory.value,
                    highlightCategories = highlightCategories.value,
                    onCategorySelected = {
                        highlightCategories.value = false
                        selectedCategory.value = it
                    },
                    onCategoryUnselected = {
                        selectedCategory.value = null
                        selectedComponent.value = null
                        highlightCategories.value = true
                    },
                    onComponentSelected = {
                        if (it == selectedComponent.value) {
                            selectedComponent.value = null
                        } else {
                            selectedComponent.value = it
                        }

                    }
                )
            }
        }
        selectedComponent.value?.let {
            item {
                BikeComponentListItem(component = it)
            }

        }

    }
}



