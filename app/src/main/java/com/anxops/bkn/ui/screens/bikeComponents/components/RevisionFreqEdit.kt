package com.anxops.bkn.ui.screens.bikeComponents.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.RevisionFrequency
import com.anxops.bkn.data.model.RevisionUnit
import com.anxops.bkn.data.model.revisionUnitRange
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.RangeNumberPicker
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun RevisionFreqEdit(
    editing: Maintenance,
    frequency: RevisionFrequency,
    original: RevisionFrequency,
    onSaveChanges: () -> Unit = {},
    onFrequencyChange: (RevisionFrequency) -> Unit
) {
    val unit by remember(frequency) {
        mutableStateOf(frequency.unit)
    }

    val range = revisionUnitRange(unit)
    val inBounds = range.contains(frequency.every)
    val showSave = frequency != original && inBounds

    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.freq_edit_service_cycle_text),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
        ) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_repeat,
                modifier = Modifier.padding(end = 10.dp).size(22.dp)
            )
            Text(
                text = "${frequency.displayText()} (${editing.displayStatus()})",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3
            )
        }

        Text(
            text = stringResource(R.string.freq_edit_cycle_type_text),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
        )

        FreqUnitCarousel(selected = frequency.unit, onSelected = {
            onFrequencyChange(
                frequency.copy(
                    unit = it,
                    every = frequency.every.coerceIn(
                        revisionUnitRange(it)
                    )
                )
            )
        })

        Text(
            text = stringResource(R.string.freq_edit_cycle_duration_from_to, range.first, range.last),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
        )

        Box(
            modifier = Modifier.padding(top = 16.dp, bottom = 26.dp).padding(horizontal = 16.dp)
        ) {
            when (unit) {
                RevisionUnit.KILOMETERS -> RevisionFrequencyDistanceEdit(
                    frequency,
                    onChange = onFrequencyChange
                )

                RevisionUnit.HOURS -> RevisionFrequencyHoursEdit(
                    frequency,
                    onChange = onFrequencyChange
                )

                RevisionUnit.MONTHS, RevisionUnit.WEEKS, RevisionUnit.YEARS -> RevisionFrequencyIntervalEdit(
                    frequency,
                    onChange = onFrequencyChange
                )
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Spacer(
                modifier = Modifier.height(52.dp).width(1.dp)
            )
            if (showSave) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = { onSaveChanges() }
                ) {
                    Text(text = stringResource(R.string.confirm_service_cycle_button_text), Modifier.padding(5.dp))
                }
            }
            Spacer(
                modifier = Modifier.height(52.dp).width(1.dp)
            )
        }
    }
}

@Composable
fun RevisionFrequencyIntervalEdit(
    frequency: RevisionFrequency,
    onChange: (RevisionFrequency) -> Unit = {}
) {
    RangeNumberPicker(
        value = frequency.every,
        increment = 1,
        suffix = frequency.unit.name,
        range = revisionUnitRange(frequency.unit)
    ) {
        onChange(frequency.copy(every = it))
    }
}

@Composable
fun RevisionFrequencyHoursEdit(
    frequency: RevisionFrequency,
    onChange: (RevisionFrequency) -> Unit = {}
) {
    RangeNumberPicker(
        value = frequency.every,
        range = revisionUnitRange(frequency.unit),
        increment = 25,
        suffix = "hours of usage"
    ) {
        onChange(frequency.copy(every = it))
    }
}

@Composable
fun RevisionFrequencyDistanceEdit(
    frequency: RevisionFrequency,
    onChange: (RevisionFrequency) -> Unit = {}
) {
    RangeNumberPicker(
        value = frequency.every,
        range = revisionUnitRange(frequency.unit),
        increment = 100,
        suffix = "kilometers"
    ) {
        onChange(frequency.copy(every = it))
    }
}

// decrement to the next factor of increment
fun incrementBy(value: Int, increment: Int, range: IntRange): Int {
    return (((value / increment) * increment) + increment).coerceIn(range)
}

fun decrementBy(value: Int, decrement: Int, range: IntRange): Int {
    return (((value / decrement) * decrement) - decrement).coerceIn(range)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FreqUnitCarousel(
    selected: RevisionUnit,
    onSelected: (RevisionUnit) -> Unit = {}
) {
    val units = remember {
        listOf(
            RevisionUnit.KILOMETERS,
            RevisionUnit.HOURS,
            RevisionUnit.WEEKS,
            RevisionUnit.MONTHS,
            RevisionUnit.YEARS
        )
    }

    val unitText = mapOf(
        RevisionUnit.KILOMETERS to stringResource(R.string.revision_unit_by_distance),
        RevisionUnit.HOURS to stringResource(R.string.revision_unit_by_usage),
        RevisionUnit.WEEKS to stringResource(R.string.revision_unit_by_weeks),
        RevisionUnit.MONTHS to stringResource(R.string.revision_unit_by_months),
        RevisionUnit.YEARS to stringResource(R.string.revision_unit_by_years)
    )

    val scroll = rememberLazyListState()

    LaunchedEffect(selected) {
        val index = units.indexOf(selected)
        if (index > 0) scroll.animateScrollToItem(index)
    }

    LazyRow(
        Modifier.fillMaxWidth(),
        state = scroll,
        horizontalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(
            horizontal = 6.dp
        )
    ) {
        units.forEach { unit ->

            val isSelected = selected == unit
            item {
                Chip(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    onClick = { onSelected(unit) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.primaryVariant,
                        contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary
                    ),
                    leadingIcon = {
                        BknIcon(
                            icon = CommunityMaterial.Icon.cmd_check,
                            modifier = Modifier.padding(start = 6.dp).size(10.dp),
                            color = if (isSelected) MaterialTheme.colors.surface else Color.Transparent

                        )
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, end = 16.dp),
                        textAlign = TextAlign.Center,
                        text = unitText[unit]!!,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
