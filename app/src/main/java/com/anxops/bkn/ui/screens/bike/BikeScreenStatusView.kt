package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bike.components.BikeComponentDetail
import com.anxops.bkn.ui.screens.bike.components.BikeStats
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap
import com.anxops.bkn.ui.screens.bike.components.ComponentCarousel
import com.anxops.bkn.ui.screens.bike.components.ComponentCategoryCarousel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BikeScreenStatusView(
    bike: Bike
) {

    val selectedCategory = remember { mutableStateOf<ComponentCategory?>(null) }
    val selectedComponent = remember { mutableStateOf<BikeComponent?>(null) }
    val highlightCategories = remember { mutableStateOf(true) }

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

    LaunchedEffect(scaffoldState.bottomSheetState.isCollapsed) {
        if (scaffoldState.bottomSheetState.isCollapsed) {
            selectedComponent.value = null
        }
    }

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.primary,
        sheetContent = {
            selectedComponent.value?.let {
                BikeComponentDetail(component = it, onClose = {
                    scope.launch { scaffoldState.bottomSheetState.collapse() }
                })
            }
        }) { paddingValues ->

        BackgroundBox {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)

            ) {


                Column(Modifier.height(100.dp)) {


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

                    selectedCategory.value?.let {
                        ComponentCarousel(
                            bike.components.filter { c -> c.type.category == it },
                            selectedComponent.value,
                            onComponentSelected = {
                                if (it == selectedComponent.value) {
                                    selectedComponent.value = null
                                } else {
                                    selectedComponent.value = it
                                }
                            })
                    }
                }

                Column(Modifier, verticalArrangement = Arrangement.Center) {
                    BikeStatusMap(bike = bike,
                        selectedCategory = selectedCategory.value,
                        selectedComponent = selectedComponent.value,
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    Text(
                        text = "${bike.displayName()}",
                        modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 0.dp),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onPrimary
                    )

                    Text(
                        text = "${bike.type.extendedType}",
                        modifier = Modifier.padding(start = 16.dp, top = 6.dp, bottom = 0.dp),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onPrimary
                    )

                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    bike.stats?.let {
                        BikeStats(bikeStats = it)
                    }
                }

            }
        }
    }
}

@Composable
fun BackgroundBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.polygons_background),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.25f)
        )
        content()
    }
}





