package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.model.ComponentType
import com.anxops.bkn.data.model.StatusLevel
import java.util.SortedMap


enum class ComponentTabHeaders(val category: ComponentCategory? = null, order: Int = 0) {
    //    GENERAL(order = 1),
    TRANSMISSION(
        ComponentCategory.TRANSMISSION, order = 2
    ),
    SUSPENSION(ComponentCategory.SUSPENSION, order = 3), BRAKES(
        ComponentCategory.BRAKES, order = 4
    ),
    WHEELS(ComponentCategory.WHEELS, order = 5), MISC(ComponentCategory.MISC, order = 6)
}

data class ComponentCategoryTabData(
    val tabHeader: ComponentTabHeaders,
    val components: List<GroupedComponents>,
    val status: StatusLevel,
)

data class GroupedComponents(
    val type: ComponentType, val items: List<BikeComponent>
)

fun buildComponentCategoryTabData(
    components: List<BikeComponent>, bikeStatus: BikeStatus
): SortedMap<ComponentTabHeaders, ComponentCategoryTabData> {
    // group by category
    val groupedByCat = components.groupBy { it.type.category }

    // for each category, group by type
    val catGroupedComponents = groupedByCat.map { (cat, comp) ->

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
    }

    return catGroupedComponents.associateBy { it.tabHeader }.toSortedMap()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BikeComponentTabsV2(
    onTabChange: (ComponentTabHeaders) -> Unit = {},
    selectedTab: ComponentTabHeaders = ComponentTabHeaders.TRANSMISSION,
    bike: Bike,
    status: BikeStatus
) {

    val lazyListState = rememberLazyListState()
    val data = remember(bike) {
        mutableStateOf(buildComponentCategoryTabData(bike.components ?: listOf(), status))
    }

    var tabHeaderIndex = data.value.keys.indexOf(selectedTab)

    LazyColumn(
        state = lazyListState, modifier = Modifier.fillMaxSize()
    ) {

        stickyHeader {
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
        }


        val tabData = data.value[selectedTab]!!
        tabData.components.forEach { gc ->

            gc.items.forEach {
                item {
                    BikeComponentListItem(component = it) {
                        // ONCLICK
                    }
                }
            }
        }


    }
}

@Composable
fun CategoryTab(d: ComponentCategoryTabData, selected: Boolean, onSelected: () -> Unit = {}) {
    Tab(
//        icon = {
//            if (d.status > StatusLevel.GOOD) {
//                BknIcon(
//                    icon = CommunityMaterial.Icon.cmd_bell_circle, getColorForStatus(
//                        d.status
//                    ), modifier = Modifier.size(18.dp)
//                )
//            }
//        },
        text = {
            Text(d.tabHeader.name, style = MaterialTheme.typography.h5)

        },
        selected = selected,
        onClick = { onSelected() },
    )
}

