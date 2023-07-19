package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.anxops.bkn.ui.screens.bike.components.ComponentCategoryCarousel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikeScreenStatusView(
    bike: Bike, bikeStatus: BikeStatus
) {
    val scrollState = rememberLazyListState()
    val selectedCategory = rememberSaveable { mutableStateOf<ComponentCategory?>(null) }
    val selectedComponent = rememberSaveable { mutableStateOf<BikeComponent?>(null) }
    val highlightCategories = rememberSaveable { mutableStateOf(true) }

    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.Start,

        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant)
    ) {

        stickyHeader {
            Text(
                text = "${bike.fullDisplayName()}",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(10.dp),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onPrimary
            )
        }

        bike.stats?.let {
            item {
                BikeStats(bikeStats = it)
            }
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
            Column(
                Modifier
                    .background(MaterialTheme.colors.primaryVariant)
                    .padding(bottom = 10.dp)
            ) {

                ComponentCategoryCarousel(selectedCategory.value) {
                    if (it == selectedCategory.value) {
                        selectedCategory.value = null
                        selectedComponent.value = null
                        highlightCategories.value = true
                    } else {
                        selectedCategory.value = it
                        highlightCategories.value = false
                    }
                }

                BikeStatusMap(
                    bike = bike,
                    bikeStatus = bikeStatus,
                    selectedCategory = selectedCategory.value,
                    highlightCategories = highlightCategories.value,
                    onCategorySelected = {
                        if (it == selectedCategory.value) {
                            selectedCategory.value = null
                            selectedComponent.value = null
                            highlightCategories.value = true
                        } else {
                            selectedCategory.value = it
                            highlightCategories.value = false
                        }
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

                selectedComponent.value?.let {
                    BikeComponentListItem(component = it)
                }
            }
        }
    }
}



