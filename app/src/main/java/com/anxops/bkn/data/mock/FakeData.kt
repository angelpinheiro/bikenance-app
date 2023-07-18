package com.anxops.bkn.data.mock

import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.ui.screens.maintenances.MaintenanceItem

object FakeData {

    val bike = Bike(
        _id = "1",
        brandName = "Specialized",
        modelName = "Epic",
        photoUrl = "https://assets.specialized.com/i/specialized/97620-01_EPIC-SW-REDTNT-BRSH-WHT_HERO?bg=rgb(241,241,241)&w=1600&h=900&fmt=auto",
        distance = 10456000
    )

    val maintenances = listOf(
        MaintenanceItem(
            bike = "Scott Spark",
            bikePart = "Tires tubeless liquid",
            title = "Check / Refill",
            time = "Two weeks ago",
            percentage = 1f,
            componentType = ComponentTypes.TIRE
        ),
        MaintenanceItem(
            bike = "Scott Spark",
            bikePart = "FOX 32 Float Performance Kashima Coating",
            title = "Full service",
            time = "3 months left",
            percentage = 0.8f,
            componentType = ComponentTypes.FORK
        ),
        MaintenanceItem(
            bike = "Scott Spark",
            bikePart = "Shimano XT Cassete",
            title = "Check / Replace",
            time = "2000km left",
            percentage = 0.4f,
            componentType = ComponentTypes.CASSETTE
        ),
        MaintenanceItem(
            bike = "Scott Spark",
            bikePart = "Cable Housing",
            title = "Replace",
            time = "1500km left",
            percentage = 0.6f,
            componentType = ComponentTypes.CABLE_HOUSING
        ),
        MaintenanceItem(
            bike = "Cube Analog",
            bikePart = "Front brake pads",
            title = "Check / Replace",
            time = "2 months left",
            percentage = 0.3f,
            componentType = ComponentTypes.DISC_PAD
        ),
        MaintenanceItem(
            bike = "Cube Analog",
            bikePart = "Shimano XT Chain",
            title = "Check / Replace",
            time = "500km left",
            percentage = 0.8f,
            componentType = ComponentTypes.CHAIN
        ),
    ).sortedByDescending { it.percentage }

}