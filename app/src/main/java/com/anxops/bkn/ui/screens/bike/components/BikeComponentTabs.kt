package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.ui.screens.maintenances.components.getColorForProgress
import com.anxops.bkn.ui.shared.components.BknIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun BikeComponentTabs(
    components: List<BikeComponent>,
    onGroupChange: (ComponentCategory) -> Unit = {}
) {


    var tabIndex = remember { mutableStateOf(0) }

    val grouped = remember {
        mutableStateOf(
            components.groupBy { it.type.group }.toList()
                .sortedBy { it.first.order }
        )
    }

    val tabs = grouped.value.map { it.first.name }
    val items = grouped.value.map { it.second }
    val status = items.map { cs -> cs.map { c -> FakeData.maintenances.firstOrNull  {c.type == it.componentType} }.filterNotNull().maxBy { it.percentage }}

    LaunchedEffect(tabIndex.value) {
        onGroupChange(grouped.value[tabIndex.value].first)
    }

    ScrollableTabRow(
        selectedTabIndex = tabIndex.value,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        edgePadding = 10.dp,
        divider = {},
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                icon = {
                    BknIcon(
                        icon = CommunityMaterial.Icon.cmd_bell_circle,
                        getColorForProgress(percentage = status.getOrNull(index)?.percentage ?: 0f, 0.6f),
                        modifier = Modifier
                            .size(18.dp)
                    )
                },
                text = {
                    Text(title, style = MaterialTheme.typography.h5)

                },
                selected = tabIndex.value == index,
                onClick = { tabIndex.value = index },
            )
        }
    }

    Column {
        items[tabIndex.value].forEach { c ->
            BikeComponentListItem(component = c)
        }
    }


}