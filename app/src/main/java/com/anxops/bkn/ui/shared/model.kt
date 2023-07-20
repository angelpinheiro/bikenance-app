package com.anxops.bkn.ui.shared

import com.anxops.bkn.R
import com.anxops.bkn.data.model.ComponentCategory

data class ComponentCategoryResources(
    val nameResId: Int
)


data class ComponentResources(
    val nameResId: Int,
    val descriptionResId: Int,
    val iconRes: Int
)

data class MaintenanceResources(
    val nameResId: Int,
    val descriptionResId: Int
)

fun ComponentCategory.resources(): ComponentCategoryResources =
    categoryResourcesMap[this]!!


val categoryResourcesMap = mapOf(
    ComponentCategory.SUSPENSION to ComponentCategoryResources(R.string.component_category_suspension),
    ComponentCategory.TRANSMISSION to ComponentCategoryResources(R.string.component_category_transmission),
    ComponentCategory.BRAKES to ComponentCategoryResources(R.string.component_category_brakes),
    ComponentCategory.WHEELS to ComponentCategoryResources(R.string.component_category_wheels),
    ComponentCategory.MISC to ComponentCategoryResources(R.string.component_category_misc),

    )
