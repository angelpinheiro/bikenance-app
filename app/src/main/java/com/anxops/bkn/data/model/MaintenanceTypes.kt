package com.anxops.bkn.data.model

import com.anxops.bkn.R
import com.anxops.bkn.ui.shared.MaintenanceResources

sealed class MaintenanceType(val name: String, val componentType: ComponentType) {

    object BrakeMaintenance : MaintenanceType("BRAKE_MAINTENANCE", ComponentType.BrakeLever)
    object DiscPadMaintenance : MaintenanceType("DISC_PAD_MAINTENANCE", ComponentType.DiscPad)
    object CablesAndHousingMaintenance :
        MaintenanceType("CABLES_AND_HOUSING_MAINTENANCE", ComponentType.CableHousing)

    object CassetteMaintenance : MaintenanceType("CASSETTE_MAINTENANCE", ComponentType.Cassette)
    object RearDerailleurMaintenance :
        MaintenanceType("REAR_DERAILLEUR_MAINTENANCE", ComponentType.RearDerailleurs)

    object ChainRingMaintenance : MaintenanceType("CHAIN_RING_MAINTENANCE", ComponentType.ChainRing)
    object ChainMaintenance : MaintenanceType("CHAIN_MAINTENANCE", ComponentType.Chain)
    object DiscBrakeMaintenance : MaintenanceType("DISC_BRAKE_MAINTENANCE", ComponentType.DiscBrake)
    object DropperPostMaintenance :
        MaintenanceType("DROPPER_POST_MAINTENANCE", ComponentType.DropperPost)

    object ForkMaintenance : MaintenanceType("FORK_MAINTENANCE", ComponentType.Fork)
    object FrontHubMaintenance : MaintenanceType("FRONT_HUB_MAINTENANCE", ComponentType.FrontHub)
    object RearHubMaintenance : MaintenanceType("REAR_HUB_MAINTENANCE", ComponentType.RearHub)
    object RearSuspensionMaintenance :
        MaintenanceType("REAR_SUSPENSION_MAINTENANCE", ComponentType.RearSuspension)

    object ThruAxleMaintenance : MaintenanceType("THRU_AXLE_MAINTENANCE", ComponentType.ThruAxle)
    object FrameBearingsMaintenance :
        MaintenanceType("FRAME_BEARINGS_MAINTENANCE", ComponentType.FrameBearings)

    object TireMaintenance : MaintenanceType("TIRE_MAINTENANCE", ComponentType.Tire)
    object WheelTubelessMaintenance : MaintenanceType("TUBELESS_MAINTENANCE", ComponentType.Wheel)

    object WheelSpokesMaintenance : MaintenanceType("SPOKES_MAINTENANCE", ComponentType.Wheel)

    object CustomMaintenance : MaintenanceType("CustomMaintenance", ComponentType.Custom)

    fun resources(): MaintenanceResources = maintenanceTypeResources(this)

    companion object {
        private val allMaintenanceTypes: List<MaintenanceType> by lazy {
            listOf(
                BrakeMaintenance,
                DiscPadMaintenance,
                CablesAndHousingMaintenance,
                CassetteMaintenance,
                RearDerailleurMaintenance,
                ChainMaintenance,
                ChainRingMaintenance,
                DiscBrakeMaintenance,
                DropperPostMaintenance,
                ForkMaintenance,
                FrontHubMaintenance,
                RearHubMaintenance,
                RearSuspensionMaintenance,
                ThruAxleMaintenance,
                FrameBearingsMaintenance,
                TireMaintenance,
                WheelTubelessMaintenance,
                WheelSpokesMaintenance,
                CustomMaintenance
            )
        }

        fun getAll(): List<MaintenanceType> {
            return allMaintenanceTypes
        }

        fun getByName(name: String): MaintenanceType {
            return allMaintenanceTypes.find { it.name == name } ?: CustomMaintenance
        }
    }
}

fun maintenanceTypeResources(type: MaintenanceType): MaintenanceResources {
    return when (type) {
        MaintenanceType.BrakeMaintenance -> MaintenanceResources(
            nameResId = R.string.brake_maintenance,
            descriptionResId = R.string.brake_maintenance_description
        )

        MaintenanceType.DiscPadMaintenance -> MaintenanceResources(
            nameResId = R.string.disc_brake_maintenance,
            descriptionResId = R.string.disc_pads_maintenance_description
        )

        MaintenanceType.CablesAndHousingMaintenance -> MaintenanceResources(
            nameResId = R.string.cables_and_housing_maintenance,
            descriptionResId = R.string.cables_and_housing_maintenance_description
        )

        MaintenanceType.CassetteMaintenance -> MaintenanceResources(
            nameResId = R.string.cassette_maintenance,
            descriptionResId = R.string.cassette_maintenance_description
        )

        MaintenanceType.ChainMaintenance -> MaintenanceResources(
            nameResId = R.string.chain_maintenance,
            descriptionResId = R.string.chain_maintenance_description
        )

        MaintenanceType.ChainRingMaintenance -> MaintenanceResources(
            nameResId = R.string.chain_ring_maintenance,
            descriptionResId = R.string.chain_ring_maintenance_description
        )

        MaintenanceType.DiscBrakeMaintenance -> MaintenanceResources(
            nameResId = R.string.disc_brake_maintenance,
            descriptionResId = R.string.disc_brake_maintenance_description
        )

        MaintenanceType.DropperPostMaintenance -> MaintenanceResources(
            nameResId = R.string.dropper_post_maintenance,
            descriptionResId = R.string.dropper_post_maintenance_description
        )

        MaintenanceType.ForkMaintenance -> MaintenanceResources(
            nameResId = R.string.fork_maintenance,
            descriptionResId = R.string.fork_maintenance_description
        )

        MaintenanceType.FrontHubMaintenance -> MaintenanceResources(
            nameResId = R.string.front_hub_maintenance,
            descriptionResId = R.string.front_hub_maintenance_description
        )

        MaintenanceType.RearSuspensionMaintenance -> MaintenanceResources(
            nameResId = R.string.rear_suspension_maintenance,
            descriptionResId = R.string.rear_suspension_maintenance_description
        )

        MaintenanceType.ThruAxleMaintenance -> MaintenanceResources(
            nameResId = R.string.thru_axle_maintenance,
            descriptionResId = R.string.thru_axle_maintenance_description
        )

        MaintenanceType.TireMaintenance -> MaintenanceResources(
            nameResId = R.string.tire_maintenance,
            descriptionResId = R.string.tire_maintenance_description
        )

        MaintenanceType.WheelTubelessMaintenance -> MaintenanceResources(
            nameResId = R.string.wheelset_tubeless_maintenance,
            descriptionResId = R.string.wheelset_tubeless_maintenance_description
        )

        MaintenanceType.WheelSpokesMaintenance -> MaintenanceResources(
            nameResId = R.string.wheelset_wheels_and_spokes_maintenance,
            descriptionResId = R.string.wheelset_wheels_and_spokes_maintenance_description
        )

        MaintenanceType.FrameBearingsMaintenance -> MaintenanceResources(
            nameResId = R.string.frame_bearings_maintenance,
            descriptionResId = R.string.frame_bearings_maintenance_description
        )

        MaintenanceType.RearHubMaintenance -> MaintenanceResources(
            nameResId = R.string.rear_hub_maintenance,
            descriptionResId = R.string.rear_hub_maintenance_description
        )

        MaintenanceType.RearDerailleurMaintenance -> MaintenanceResources(
            nameResId = R.string.rear_derailleur_maintenance,
            descriptionResId = R.string.rear_derailleur_maintenance_description
        )

        MaintenanceType.CustomMaintenance -> MaintenanceResources(
            nameResId = R.string.componentType_CUSTOM,
            descriptionResId = R.string.componentType_CUSTOM
        )
    }
}

