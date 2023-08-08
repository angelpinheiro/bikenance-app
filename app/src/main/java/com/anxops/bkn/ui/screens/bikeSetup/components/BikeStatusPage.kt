package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.bikeSetup.BikeSetupTitle
import com.anxops.bkn.ui.screens.bikeSetup.SetupDetails
import com.anxops.bkn.ui.shared.resources

@Composable
fun BikeStatusPage(
    details: SetupDetails,
    onContinue: () -> Unit = {},
    onLastMaintenanceUpdate: (ComponentCategory, Float) -> Unit = { _, _ -> }
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scrollState)

    ) {
        BikeSetupTitle(text = "Current bike status")

        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = "When was the last maintenance of",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        Column {
            details.lastMaintenances.toList().forEachIndexed { index, (category, months) ->

                details.lastComponentMaintenances.filter { it.key.category == category }.let { items ->
                    if (items.isNotEmpty()) {
                        Column(
                            Modifier.padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = category.resources().nameResId),
                                    color = MaterialTheme.colors.onPrimary,
                                    style = MaterialTheme.typography.h2,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            DurationSelector(value = months, onUpdate = {
                                onLastMaintenanceUpdate(category, it)
                            })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DurationSelector(value: Float, onUpdate: (Float) -> Unit = {}) {
    val lazyListState = rememberLazyListState()

    val values = remember {
        listOf(
            "Recently" to 0f,
            "1 month" to 1f,
            "2 months" to 2f,
            "3 months" to 3f,
            "6 months" to 6f,
            "1 year" to 12f,
            "2 years" to 24f
        )
    }

//    LaunchedEffect(value) {
//        val index = values.indexOfFirst { it.second == value }
//        if (index > 0) lazyListState.animateScrollToItem(index)
//    }

    LazyRow(state = lazyListState) {
        items(count = values.size, key = { index -> index }) { index ->

            val shape = when (index) {
                0 -> {
                    RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                }

                values.size - 1 -> {
                    RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                }

                else -> {
                    RectangleShape
                }
            }

            val months = values[index].second
            val text = values[index].first
            val isSelected = value == months

            Button(
                modifier = Modifier.padding(horizontal = 0.dp),
                shape = shape,
                onClick = { onUpdate(months) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant
                )
            ) {
                Text(
                    modifier = Modifier.padding(3.dp),
                    textAlign = TextAlign.Center,
                    text = text,
                    color = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PeriodSelector(value: Float, onUpdate: (Float) -> Unit = {}) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
            value = value,
            valueRange = 0f..12f,
            steps = 11,
            onValueChange = { onUpdate(it) },
                colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colors.surface,
                thumbColor = MaterialTheme.colors.surface,
                inactiveTrackColor = MaterialTheme.colors.primaryVariant,
                inactiveTickColor = MaterialTheme.colors.surface.copy(alpha = 0.5f),
                activeTickColor = MaterialTheme.colors.surface
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Recently", color = MaterialTheme.colors.surface, fontSize = 10.sp)
            Text(text = "1 year +", color = MaterialTheme.colors.surface, fontSize = 10.sp)
        }
    }
}
