package com.anxops.bkn.ui.screens.bikeSetup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlin.math.floor


@Composable
fun BikeStatusPage(
    details: SetupDetails,
    onContinue: () -> Unit = {},
    onLastMaintenanceUpdate: (ComponentCategory, Float) -> Unit = { _, _ -> },
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

        Column(
            Modifier.clip(
                RoundedCornerShape(10.dp)
            )
        ) {
            details.lastMaintenances.toList().forEachIndexed { index, (category, months) ->


                details.lastComponentMaintenances.filter { it.key.category == category }
                    .let { items ->
                        if (items.isNotEmpty()) {

                            val displayValue = when (months) {
                                in 0f..1f -> "Recently"
                                12f -> "One year ago or more"
                                else -> {
                                    "${floor(months).toInt()} months ago"
                                }
                            }

                            Column(
                                Modifier
                                    .background(MaterialTheme.colors.primaryVariant)
                                    .padding(bottom = if (index < details.lastMaintenances.size - 1) 5.dp else 0.dp)
                                    .background(MaterialTheme.colors.primary)
                                    .padding(10.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(id = category.resources().nameResId),
                                        color = MaterialTheme.colors.onPrimary,
                                        style = MaterialTheme.typography.h3,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Text(
                                        text = "$displayValue",
                                        color = MaterialTheme.colors.secondary,
                                        style = MaterialTheme.typography.h3,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MaterialTheme.colors.primary)
                                            .padding(3.dp)

                                    )
                                }

                                DurationSelector(value = months, onUpdate = {
                                    onLastMaintenanceUpdate(category, it)
                                })
//                                Row(
//                                    Modifier.fillMaxWidth(),
//                                    verticalAlignment = Alignment.CenterVertically
//
//                                ) {
//                                    items.forEach { (type, value) ->
//
//                                        Text(
//                                            text = stringResource(type.resources().nameResId),
//                                            color = MaterialTheme.colors.onPrimary,
//                                            style = MaterialTheme.typography.h4,
//                                            modifier = Modifier.padding(end = 5.dp),
//                                            overflow = TextOverflow.Ellipsis,
//                                            maxLines = 1
//                                        )
//
//                                    }
//
//                                }
                            }
                        }
                    }
            }
        }

        OutlinedButton(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            onClick = { onContinue() },
            modifier = Modifier.padding(top = 25.dp)

        ) {
            Text(text = "Continue", Modifier.padding(2.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DurationSelector(value: Float, onUpdate: (Float) -> Unit = {}) {

    val values = listOf(
        "Recently" to 0f,
        "1 month" to 1f,
        "2 months" to 2f,
        "3 months" to 3f,
        "6 months" to 6f,
        "1 year" to 12f,
        "2 years" to 24f
    )

    LazyRow() {
        items(count = values.size, key = { index -> index }) { index ->

            val months = values[index].second
            val text = values[index].first
            val isSelected = value == months

            Chip(
                modifier = Modifier.padding(horizontal = 3.dp),
                onClick = { onUpdate(months) },
                colors = ChipDefaults.chipColors(
                    backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
                    contentColor = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface
                ),
            ) {
                Text(
                    modifier = Modifier.padding(3.dp),
                    textAlign = TextAlign.Center,
                    text = text,
                    color = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
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