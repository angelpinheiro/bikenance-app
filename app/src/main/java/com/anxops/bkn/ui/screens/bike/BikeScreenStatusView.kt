package com.anxops.bkn.ui.screens.bike


import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bike.components.BikeStats
import com.anxops.bkn.ui.screens.bike.components.BikeStatusMap
import com.anxops.bkn.ui.screens.bike.components.ComponentCarousel
import com.anxops.bkn.ui.screens.bike.components.ComponentCategoryCarousel
import com.anxops.bkn.ui.theme.strava


@Composable
fun BikeScreenStatusView(
    bike: Bike,
    selectedComponent: BikeComponent?,
    selectedCategory: ComponentCategory?,
    onEvent: (BikeScreenEvent) -> Unit = {}
) {

    LandscapeAwareBikeStatus(carouselView = {
        Column(Modifier.height(100.dp)) {
            ComponentCategoryCarousel(selectedCategory) {
                onEvent(BikeScreenEvent.SelectComponentCategory(it))
            }
            selectedCategory?.let {
                ComponentCarousel(bike.componentList().filter { c -> c.type.category == it },
                    selectedComponent,
                    onComponentSelected = {
                        onEvent(BikeScreenEvent.SelectComponent(it))
                    })
            }
        }
    }, statusView = {
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
    }, bikeNameAndTypeView = {
        BikeNameAndType(bike, onEvent)
    }, bikeStatsView = {
        bike.stats?.let {
            BikeStats(bikeStats = it)
        }
    })

}

@Composable
fun LandscapeAwareBikeStatus(
    carouselView: @Composable () -> Unit,
    statusView: @Composable () -> Unit,
    bikeNameAndTypeView: @Composable () -> Unit,
    bikeStatsView: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)

        ) {
            Column(
                Modifier
                    .weight(0.4f)
                    .fillMaxHeight(), verticalArrangement = Arrangement.Center
            ) {
                bikeStatsView()
            }
            Column(
                Modifier
                    .weight(0.6f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                statusView()
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)

        ) {
            carouselView()
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                statusView()
            }
            bikeNameAndTypeView()
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                bikeStatsView()
            }
        }
    }

}

@Composable
private fun BikeNameAndType(
    bike: Bike, onEvent: (BikeScreenEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.padding(start = 16.dp, top = 0.dp, bottom = 6.dp),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onPrimary
        )

        Text(text = "View on Strava",
            color = MaterialTheme.colors.strava,
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 16.dp, top = 0.dp, bottom = 16.dp)
                .clickable { onEvent(BikeScreenEvent.ViewOnStrava) })
    }
}



