package com.anxops.bkn.data.model

// TODO Add: Frame bearings, handlebar tape
enum class ComponentTypes(
    val category: ComponentCategory = ComponentCategory.MISC
) {
    CASSETTE(ComponentCategory.TRANSMISSION),
    CHAIN(ComponentCategory.TRANSMISSION),
    DISC_BRAKE(ComponentCategory.BRAKES),
    DISC_PAD(ComponentCategory.BRAKES),
    DROPER_POST,
    FORK(ComponentCategory.SUSPENSION),
    FRONT_HUB(ComponentCategory.WHEELS),
    PEDAL_CLIPLESS,
    REAR_DERAUILLEURS(ComponentCategory.TRANSMISSION),
    REAR_HUB(ComponentCategory.WHEELS),
    REAR_SUSPENSION(ComponentCategory.SUSPENSION),
    THRU_AXLE,
    TIRE(ComponentCategory.WHEELS),
    WHEELSET(ComponentCategory.WHEELS),
    BRAKE_LEVER(ComponentCategory.BRAKES),
    CABLE_HOUSING(ComponentCategory.TRANSMISSION),
    FRAME_BEARINGS,
    HANDLEBAR_TAPE,
    CUSTOM,
}