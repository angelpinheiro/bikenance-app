package com.anxops.bkn.data.model

enum class MaintenanceTypes(val componentType: ComponentTypes) {
    BRAKE_MAINTENANCE(ComponentTypes.BRAKE_LEVER),
    DISC_PAD_MAINTENANCE(ComponentTypes.DISC_PAD),
    CABLES_AND_HOUSING_MAINTENANCE(ComponentTypes.CABLE_HOUSING),
    CASSETTE_MAINTENANCE(ComponentTypes.CASSETTE),
    REAR_DERAILLEUR_MAINTENANCE(ComponentTypes.REAR_DERAUILLEURS),
    CHAIN_MAINTENANCE(ComponentTypes.CHAIN),
    DISC_BRAKE_MAINTENANCE(ComponentTypes.DISC_BRAKE),
    DROPPER_POST_MAINTENANCE(ComponentTypes.DROPER_POST),
    FORK_MAINTENANCE(ComponentTypes.FORK),
    FRONT_HUB_MAINTENANCE(ComponentTypes.FRONT_HUB),
    REAR_HUB_MAINTENANCE(ComponentTypes.REAR_HUB),
    REAR_SUSPENSION_MAINTENANCE(ComponentTypes.REAR_SUSPENSION),
    THRU_AXLE_MAINTENANCE(ComponentTypes.THRU_AXLE),
    FRAME_BEARINGS_MAINTENANCE(ComponentTypes.FRAME_BEARINGS),
    TIRE_MAINTENANCE(ComponentTypes.TIRE),
    WHEELSET_TUBELESS_MAINTENANCE(ComponentTypes.WHEELSET),
    WHEELSET_WHEELS_AND_SPOKES_MAINTENANCE(ComponentTypes.WHEELSET),
}