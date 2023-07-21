package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bike.components.BikeStats
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap
import com.anxops.bkn.ui.screens.bike.components.ComponentCarousel
import com.anxops.bkn.ui.screens.bike.components.ComponentCategoryCarousel
import com.anxops.bkn.ui.shared.components.BackgroundBox


@Composable
fun BikeScreenStatusView(
    bike: Bike,
    selectedComponent: BikeComponent?,
    selectedCategory: ComponentCategory?,
    onEvent: (BikeScreenEvent) -> Unit = {}
) {

    BackgroundBox {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)

        ) {

            Column(Modifier.height(100.dp)) {
                ComponentCategoryCarousel(selectedCategory) {
                    onEvent(BikeScreenEvent.SelectComponentCategory(it))
                }
                selectedCategory?.let {
                    ComponentCarousel(bike.components.filter { c -> c.type.category == it },
                        selectedComponent,
                        onComponentSelected = {
                            onEvent(BikeScreenEvent.SelectComponent(it))
                        })
                }
            }
            Column(
                Modifier.fillMaxWidth().weight(1f).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BikeStatusMap(bike = bike,
                    selectedCategory = selectedCategory,
                    selectedComponent = selectedComponent,
                    onCategorySelected = {
                        onEvent(BikeScreenEvent.SelectComponentCategory(it))
                    },
                    onCategoryUnselected = {
                        onEvent(BikeScreenEvent.SelectComponentCategory(null))
                    },
                    onComponentSelected = {
                        onEvent(BikeScreenEvent.SelectComponent(it))
                    })
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
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
                    modifier = Modifier.padding(start = 6.dp, top = 0.dp, bottom = 16.dp),
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





