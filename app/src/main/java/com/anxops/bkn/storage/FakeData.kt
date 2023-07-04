/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.storage

import com.anxops.bkn.model.Bike
import com.anxops.bkn.ui.screens.maintenances.components.MaintenanceItem

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
            bikePart = "Tubeless liquid",
            title = "Check / Refill",
            time = "Two weeks ago",
            percentage = 1f
        ),
        MaintenanceItem(
            bike = "Scott Spark",
            bikePart = "FOX 32 Float Performance",
            title = "Full service",
            time = "3 months left",
            percentage = 0.8f
        ),
        MaintenanceItem(
            bike = "Scott Spark",
            bikePart = "Shimano XT Chain",
            title = "Check / Replace",
            time = "2000km left",
            percentage = 0.4f
        ),
        MaintenanceItem(
            bike = "Cube Analog",
            bikePart = "Front brake pads",
            title = "Check / Replace",
            time = "2 months left",
            percentage = 0.3f
        ),
        MaintenanceItem(
            bike = "Cube Analog",
            bikePart = "Shimano XT Chain",
            title = "Check / Replace",
            time = "500km left",
            percentage = 0.8f
        ),
    ).sortedByDescending { it.percentage }

}