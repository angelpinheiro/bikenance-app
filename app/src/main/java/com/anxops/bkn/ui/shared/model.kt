package com.anxops.bkn.ui.shared

import com.anxops.bkn.R
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.MaintenanceTypes

data class ComponentResources(
    val nameResId: Int,
    val descriptionResId: Int
)

data class MaintenanceResources(
    val nameResId: Int,
    val descriptionResId: Int
)


fun ComponentTypes.resources(): ComponentResources =
    componentResourcesMap[this] ?: componentResourcesMap[ComponentTypes.CUSTOM]!!


fun MaintenanceTypes.resources(): MaintenanceResources =
    maintenanceResourcesMap[this]
        ?: maintenanceResourcesMap[MaintenanceTypes.DISC_PAD_MAINTENANCE]!!


val componentResourcesMap = mapOf(
    ComponentTypes.BRAKE_LEVER to ComponentResources(
        R.string.componentType_BRAKE_LEVER,
        R.string.componentType_BRAKE_LEVER_description
    ),
    ComponentTypes.CABLE_HOUSING to ComponentResources(
        R.string.componentType_CABLE_HOUSING,
        R.string.componentType_CABLE_HOUSING_description
    ),
    ComponentTypes.CASSETTE to ComponentResources(
        R.string.componentType_CASSETTE,
        R.string.componentType_CASSETTE_description
    ),
    ComponentTypes.CHAIN to ComponentResources(
        R.string.componentType_CHAIN,
        R.string.componentType_CHAIN_description
    ),
    ComponentTypes.DISC_BRAKE to ComponentResources(
        R.string.componentType_DISC_BRAKE,
        R.string.componentType_DISC_BRAKE_description
    ),
    ComponentTypes.DISC_PAD to ComponentResources(
        R.string.componentType_DISC_PAD,
        R.string.componentType_DISC_PAD_description
    ),
    ComponentTypes.DROPER_POST to ComponentResources(
        R.string.componentType_DROPER_POST,
        R.string.componentType_DROPER_POST_description
    ),
    ComponentTypes.FORK to ComponentResources(
        R.string.componentType_FORK,
        R.string.componentType_FORK_description
    ),
    ComponentTypes.FRONT_HUB to ComponentResources(
        R.string.componentType_FRONT_HUB,
        R.string.componentType_FRONT_HUB_description
    ),
    ComponentTypes.PEDAL_CLIPLESS to ComponentResources(
        R.string.componentType_PEDAL_CLIPLESS,
        R.string.componentType_PEDAL_CLIPLESS_description
    ),
    ComponentTypes.REAR_DERAUILLEURS to ComponentResources(
        R.string.componentType_REAR_DERAUILLEURS,
        R.string.componentType_REAR_DERAUILLEURS_description
    ),
    ComponentTypes.REAR_HUB to ComponentResources(
        R.string.componentType_REAR_HUB,
        R.string.componentType_REAR_HUB_description
    ),
    ComponentTypes.REAR_SUSPENSION to ComponentResources(
        R.string.componentType_REAR_SUSPENSION,
        R.string.componentType_REAR_SUSPENSION_description
    ),
    ComponentTypes.THRU_AXLE to ComponentResources(
        R.string.componentType_THRU_AXLE,
        R.string.componentType_THRU_AXLE_description
    ),
    ComponentTypes.TIRE to ComponentResources(
        R.string.componentType_TIRE,
        R.string.componentType_TIRE_description
    ),
    ComponentTypes.WHEELSET to ComponentResources(
        R.string.componentType_WHEELSET,
        R.string.componentType_WHEELSET_description
    ),
    ComponentTypes.FRAME_BEARINGS to ComponentResources(
        R.string.componentType_FRAME_BEARINGS,
        R.string.componentType_FRAME_BEARINGS_description
    ),
    ComponentTypes.CUSTOM to ComponentResources(
        R.string.componentType_CUSTOM,
        R.string.componentType_CUSTOM_description
    ),
    ComponentTypes.HANDLEBAR_TAPE to ComponentResources(
        R.string.componentType_HANDLEBAR_TAPE,
        R.string.componentType_HANDLEBAR_TAPE_description
    ),
)

val maintenanceResourcesMap = mapOf(
    MaintenanceTypes.BRAKE_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.brake_maintenance,
        descriptionResId = R.string.brake_maintenance_description
    ),
    MaintenanceTypes.DISC_PAD_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.disc_brake_maintenance,
        descriptionResId = R.string.disc_pads_maintenance_description
    ),
    MaintenanceTypes.CABLES_AND_HOUSING_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.cables_and_housing_maintenance,
        descriptionResId = R.string.cables_and_housing_maintenance_description
    ),
    MaintenanceTypes.CASSETTE_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.cassette_maintenance,
        descriptionResId = R.string.cassette_maintenance_description
    ),
    MaintenanceTypes.CHAIN_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.chain_maintenance,
        descriptionResId = R.string.chain_maintenance_description
    ),
    MaintenanceTypes.DISC_BRAKE_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.disc_brake_maintenance,
        descriptionResId = R.string.disc_brake_maintenance_description
    ),
    MaintenanceTypes.DROPPER_POST_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.dropper_post_maintenance,
        descriptionResId = R.string.dropper_post_maintenance_description
    ),
    MaintenanceTypes.FORK_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.fork_maintenance,
        descriptionResId = R.string.fork_maintenance_description
    ),
    MaintenanceTypes.FRONT_HUB_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.front_hub_maintenance,
        descriptionResId = R.string.front_hub_maintenance_description
    ),
    MaintenanceTypes.REAR_SUSPENSION_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.rear_suspension_maintenance,
        descriptionResId = R.string.rear_suspension_maintenance_description
    ),
    MaintenanceTypes.THRU_AXLE_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.thru_axle_maintenance,
        descriptionResId = R.string.thru_axle_maintenance_description
    ),
    MaintenanceTypes.TIRE_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.tire_maintenance,
        descriptionResId = R.string.tire_maintenance_description
    ),
    MaintenanceTypes.WHEELSET_TUBELESS_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.wheelset_tubeless_maintenance,
        descriptionResId = R.string.wheelset_tubeless_maintenance_description
    ),
    MaintenanceTypes.WHEELSET_WHEELS_AND_SPOKES_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.wheelset_wheels_and_spokes_maintenance,
        descriptionResId = R.string.wheelset_wheels_and_spokes_maintenance_description
    ),
    MaintenanceTypes.FRAME_BEARINGS_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.frame_bearings_maintenance,
        descriptionResId = R.string.frame_bearings_maintenance_description
    ),
    MaintenanceTypes.REAR_HUB_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.rear_hub_maintenance,
        descriptionResId = R.string.rear_hub_maintenance_description
    ),
    MaintenanceTypes.REAR_DERAILLEUR_MAINTENANCE to MaintenanceResources(
        nameResId = R.string.rear_derailleur_maintenance,
        descriptionResId = R.string.rear_derailleur_maintenance_description
    ),
)


