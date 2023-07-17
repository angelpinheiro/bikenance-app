package com.anxops.bkn.data.model


fun getDefaultComponents(bikeType: BikeType): Set<ComponentTypes> {
    return maintenanceConfigurations[bikeType] ?: ComponentTypes.values().toSet()
}

val maintenanceConfigurations = mapOf(
    BikeType.MTB to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.HANDLEBAR_TAPE,
        )
    ),
    BikeType.FULL_MTB to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.HANDLEBAR_TAPE,
        )
    ),
    BikeType.ROAD to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.FORK
        )
    ),
    BikeType.GRAVEL to ComponentTypes.values().toSet().minus(
        setOf(
            ComponentTypes.CUSTOM,
            ComponentTypes.DROPER_POST,
            ComponentTypes.REAR_SUSPENSION,
            ComponentTypes.FRAME_BEARINGS,
            ComponentTypes.PEDAL_CLIPLESS,
            ComponentTypes.FORK
        )
    ),
    BikeType.STATIONARY to setOf()
)


