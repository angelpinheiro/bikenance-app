package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bike.components.BikeComponentDetail
import com.anxops.bkn.ui.screens.bike.components.BikeStats
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap
import com.anxops.bkn.ui.screens.bike.components.ComponentCategoryCarousel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BikeScreenStatusView(
    bike: Bike, bikeStatus: BikeStatus
) {

    val scrollState = rememberLazyListState()
    val selectedCategory = rememberSaveable { mutableStateOf<ComponentCategory?>(null) }
    val selectedComponent = rememberSaveable { mutableStateOf<BikeComponent?>(null) }
    val highlightCategories = rememberSaveable { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(selectedComponent.value) {
        launch {
            if (selectedComponent.value != null) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.collapse()
            }
        }

    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.primary,
        sheetContent = {
            selectedComponent.value?.let {
                BikeComponentDetail(component = it)
            }
        }) { paddingValues ->
        LazyColumn(
            state = scrollState,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colors.primaryVariant)
        ) {

//            item {
//                Text(
//                    text = "Bike status",
//                    modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 0.dp),
//                    style = MaterialTheme.typography.h2,
//                    color = MaterialTheme.colors.onPrimary
//                )
//            }
            stickyHeader {
                Column(
                    Modifier
                        .background(MaterialTheme.colors.primaryVariant)
                        .padding(vertical = 10.dp)
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

                    BikeStatusMap(bike = bike,
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
                        })
                }
            }

            item {
                Text(
                    text = "${bike.fullDisplayName()} stats",
                    modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 0.dp),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onPrimary
                )
            }

            bike.stats?.let {
                item {
                    BikeStats(bikeStats = it)
                }
            }
        }
    }
}




