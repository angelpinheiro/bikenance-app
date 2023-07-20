package com.anxops.bkn.data.model


sealed class MaintenanceConfiguration(val componentTypes: Set<ComponentType>) {

    object Mtb : MaintenanceConfiguration(
        ComponentType.getAll().toSet().minus(
            setOf(
                ComponentType.Custom,
                ComponentType.DropperPost,
                ComponentType.RearSuspension,
                ComponentType.FrameBearings,
                ComponentType.PedalClipless,
                ComponentType.HandlebarTape,
            )
        )
    )

    object FullMtb : MaintenanceConfiguration(
        ComponentType.getAll().toSet().minus(
            setOf(
                ComponentType.Custom,
                ComponentType.DropperPost,
                ComponentType.PedalClipless,
                ComponentType.HandlebarTape,
            )
        )
    )

    object Road : MaintenanceConfiguration(
        ComponentType.getAll().toSet().minus(
            setOf(

                ComponentType.Custom,
                ComponentType.Fork,
                ComponentType.FrameBearings,
                ComponentType.DropperPost,
                ComponentType.PedalClipless,
                ComponentType.HandlebarTape,
            )
        )
    )

    object Gravel : MaintenanceConfiguration(
        ComponentType.getAll().toSet().minus(
            setOf(

                ComponentType.Custom,
                ComponentType.Fork,
                ComponentType.FrameBearings,
                ComponentType.DropperPost,
                ComponentType.PedalClipless,
                ComponentType.HandlebarTape,
            )
        )
    )

    object Empty : MaintenanceConfiguration(
        setOf()
    )

    companion object {

        fun getAll() = listOf(
            Mtb, FullMtb, Road, Gravel,
        )

        fun forBikeType(bikeType: BikeType): MaintenanceConfiguration {
            return when (bikeType) {
                BikeType.Mtb -> Mtb
                BikeType.Road -> Road
                BikeType.EBike -> FullMtb
                BikeType.Gravel -> Gravel
                BikeType.Stationary -> Empty
                BikeType.Unknown -> Empty
            }
        }


    }
}


