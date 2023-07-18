package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.StatusLevel
import com.anxops.bkn.ui.screens.maintenances.getColorForProgress
import com.anxops.bkn.ui.screens.maintenances.getColorForStatus
import com.anxops.bkn.ui.shared.components.BknIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import java.util.SortedMap


enum class ComponentTabHeaders(val category: ComponentCategory? = null, order: Int = 0) {
    GENERAL(order = 1),
    TRANSMISSION(ComponentCategory.TRANSMISSION, order = 2),
    SUSPENSION(ComponentCategory.SUSPENSION, order = 3),
    BRAKES(ComponentCategory.BRAKES, order = 4),
    WHEELS(ComponentCategory.WHEELS, order = 5),
    MISC(ComponentCategory.MISC, order = 6)
}

data class ComponentCategoryTabData(
    val tabHeader: ComponentTabHeaders,
    val components: List<GroupedComponents>,
    val status: StatusLevel,
)

data class GroupedComponents(
    val type: ComponentTypes, val items: List<BikeComponent>
)

fun buildComponentCategoryTabData(components: List<BikeComponent>, bikeStatus: BikeStatus): SortedMap<ComponentTabHeaders, ComponentCategoryTabData> {
    // group by category
    val groupedByCat = components.groupBy { it.type.category }

    // add general tab
    var catGroupedComponents = listOf(
        ComponentCategoryTabData(
            ComponentTabHeaders.GENERAL, emptyList(), StatusLevel.UNKNOWN
        )
    )

    // for each category, group by type
    catGroupedComponents = catGroupedComponents.plus(groupedByCat.map { (cat, comp) ->

        val items = comp.groupBy { it.type }.map { (t, c) ->
            GroupedComponents(
                type = t, items = c
            )
        }

        val cat = ComponentTabHeaders.values().first {
            it.category == cat
        }

        val status = bikeStatus.componentCategoryStatus[cat.category] ?: StatusLevel.UNKNOWN

        ComponentCategoryTabData(
            components = items, status = status, tabHeader = cat
        )
    })

    return catGroupedComponents.associateBy { it.tabHeader }.toSortedMap()
}

@Composable
fun BikeComponentTabsV2(
    components: List<BikeComponent>, onTabChange: (ComponentTabHeaders) -> Unit = {},
    selectedTab: ComponentTabHeaders = ComponentTabHeaders.GENERAL,
    bike: Bike,
    status: BikeStatus
) {

    val data = remember(components) {
        mutableStateOf(buildComponentCategoryTabData(components, status))
    }

    var tabHeaderIndex = data.value.keys.indexOf(selectedTab)


    ScrollableTabRow(
        selectedTabIndex = tabHeaderIndex,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        edgePadding = 10.dp,
        divider = {},
    ) {
        data.value.forEach { (h, data) ->
            CategoryTab(d = data, selected = selectedTab == h, onSelected = {
                onTabChange(h)
            })
        }
    }

    Column(Modifier.fillMaxSize()) {

        when (selectedTab) {
            ComponentTabHeaders.GENERAL -> {
                BikeStatus(bike = bike)
            }

            else -> {
                val tabData = data.value[selectedTab]!!

                tabData.components.forEach { gc ->

                    if (gc.items.size > 1) {
                        MultiComponentListItem(gc)
                    } else {
                        gc.items.forEach {
                            BikeComponentListItem(component = it)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun CategoryTab(d: ComponentCategoryTabData, selected: Boolean, onSelected: () -> Unit = {}) {
    Tab(
        icon = {
            if(d.status > StatusLevel.GOOD) {
                BknIcon(
                    icon = CommunityMaterial.Icon.cmd_bell_circle, getColorForStatus(
                        d.status
                    ), modifier = Modifier.size(18.dp)
                )
            }
        },
        text = {
            Text(d.tabHeader.name, style = MaterialTheme.typography.h5)

        },
        selected = selected,
        onClick = { onSelected() },
    )
}

