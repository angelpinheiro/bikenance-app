package com.anxops.bkn.data.model

import com.anxops.bkn.R
import com.anxops.bkn.data.model.util.ComponentTypeSerializer
import com.anxops.bkn.ui.shared.ComponentResources
import kotlinx.serialization.Serializable

@Serializable(with = ComponentTypeSerializer::class)
sealed class ComponentType(val name: String, val category: ComponentCategory) {

    object Cassette : ComponentType("CASSETTE", ComponentCategory.TRANSMISSION)
    object Chain : ComponentType("CHAIN", ComponentCategory.TRANSMISSION)
    object RearDerailleurs : ComponentType("REAR_DERAILLEURS", ComponentCategory.TRANSMISSION)
    object ChainRing : ComponentType("CHAIN_RING", ComponentCategory.TRANSMISSION)
    object CableHousing : ComponentType("CABLE_HOUSING", ComponentCategory.TRANSMISSION)

    object DiscBrake : ComponentType("DISC_BRAKE", ComponentCategory.BRAKES)
    object DiscPad : ComponentType("DISC_PAD", ComponentCategory.BRAKES)
    object BrakeLever : ComponentType("BRAKE_LEVER", ComponentCategory.BRAKES)

    object Fork : ComponentType("FORK", ComponentCategory.SUSPENSION)
    object RearSuspension : ComponentType("REAR_SUSPENSION", ComponentCategory.SUSPENSION)

    object Wheel : ComponentType("WHEEL", ComponentCategory.WHEELS)
    object FrontHub : ComponentType("FRONT_HUB", ComponentCategory.WHEELS)
    object RearHub : ComponentType("REAR_HUB", ComponentCategory.WHEELS)
    object Tire : ComponentType("TIRE", ComponentCategory.WHEELS)

    object FrameBearings : ComponentType("FRAME_BEARINGS", ComponentCategory.MISC)
    object DropperPost : ComponentType("DROPPER_POST", ComponentCategory.MISC)
    object ThruAxle : ComponentType("THRU_AXLE", ComponentCategory.MISC)
    object PedalClipless : ComponentType("PEDAL_CLIPLESS", ComponentCategory.MISC)
    object HandlebarTape : ComponentType("HANDLEBAR_TAPE", ComponentCategory.MISC)
    object Custom : ComponentType("CUSTOM", ComponentCategory.MISC)

    fun resources(): ComponentResources = componentTypeResources(this)

    companion object {

        private val allComponentTypes: List<ComponentType> by lazy {
            listOf(
                Cassette,
                Chain,
                RearDerailleurs,
                ChainRing,
                CableHousing,
                DiscBrake,
                DiscPad,
                BrakeLever,
                Fork,
                RearSuspension,
                Wheel,
                FrontHub,
                RearHub,
                Tire,
                FrameBearings,
                DropperPost,
                ThruAxle,
                PedalClipless,
                HandlebarTape,
                Custom
            )
        }

        fun getAll(): List<ComponentType> {
            return allComponentTypes
        }

        fun getByName(name: String): ComponentType {
            return allComponentTypes.find { it.name == name } ?: Custom
        }
    }
}

fun componentTypeResources(type: ComponentType): ComponentResources {
    return when (type) {
        ComponentType.BrakeLever -> ComponentResources(
            nameResId = R.string.componentType_BRAKE_LEVER,
            descriptionResId = R.string.componentType_BRAKE_LEVER_description,
            iconRes = R.drawable.brake_lever
        )

        ComponentType.CableHousing -> ComponentResources(
            nameResId = R.string.componentType_CABLE_HOUSING,
            descriptionResId = R.string.componentType_CABLE_HOUSING_description,
            iconRes = R.drawable.cable_housing
        )

        ComponentType.Cassette -> ComponentResources(
            nameResId = R.string.componentType_CASSETTE,
            descriptionResId = R.string.componentType_CASSETTE_description,
            iconRes = R.drawable.cassette
        )

        ComponentType.Chain -> ComponentResources(
            nameResId = R.string.componentType_CHAIN,
            descriptionResId = R.string.componentType_CHAIN_description,
            iconRes = R.drawable.chain
        )

        ComponentType.DiscBrake -> ComponentResources(
            nameResId = R.string.componentType_DISC_BRAKE,
            descriptionResId = R.string.componentType_DISC_BRAKE_description,
            iconRes = R.drawable.disc_brake
        )

        ComponentType.DiscPad -> ComponentResources(
            nameResId = R.string.componentType_DISC_PAD,
            descriptionResId = R.string.componentType_DISC_PAD_description,
            iconRes = R.drawable.disc_pad
        )

        ComponentType.DropperPost -> ComponentResources(
            nameResId = R.string.componentType_DROPER_POST,
            descriptionResId = R.string.componentType_DROPER_POST_description,
            iconRes = R.drawable.droper_post
        )

        ComponentType.Fork -> ComponentResources(
            nameResId = R.string.componentType_FORK,
            descriptionResId = R.string.componentType_FORK_description,
            iconRes = R.drawable.fork
        )

        ComponentType.FrontHub -> ComponentResources(
            nameResId = R.string.componentType_FRONT_HUB,
            descriptionResId = R.string.componentType_FRONT_HUB_description,
            iconRes = R.drawable.front_hub
        )

        ComponentType.PedalClipless -> ComponentResources(
            nameResId = R.string.componentType_PEDAL_CLIPLESS,
            descriptionResId = R.string.componentType_PEDAL_CLIPLESS_description,
            iconRes = R.drawable.pedal_clipless
        )

        ComponentType.RearDerailleurs -> ComponentResources(
            nameResId = R.string.componentType_REAR_DERAUILLEURS,
            descriptionResId = R.string.componentType_REAR_DERAUILLEURS_description,
            iconRes = R.drawable.rear_derauilleurs
        )

        ComponentType.RearHub -> ComponentResources(
            nameResId = R.string.componentType_REAR_HUB,
            descriptionResId = R.string.componentType_REAR_HUB_description,
            iconRes = R.drawable.rear_hub
        )

        ComponentType.RearSuspension -> ComponentResources(
            nameResId = R.string.componentType_REAR_SUSPENSION,
            descriptionResId = R.string.componentType_REAR_SUSPENSION_description,
            iconRes = R.drawable.rear_suspension
        )

        ComponentType.ThruAxle -> ComponentResources(
            nameResId = R.string.componentType_THRU_AXLE,
            descriptionResId = R.string.componentType_THRU_AXLE_description,
            iconRes = R.drawable.thru_axle
        )

        ComponentType.Tire -> ComponentResources(
            nameResId = R.string.componentType_TIRE,
            descriptionResId = R.string.componentType_TIRE_description,
            iconRes = R.drawable.tire
        )

        ComponentType.Wheel -> ComponentResources(
            nameResId = R.string.componentType_WHEELSET,
            descriptionResId = R.string.componentType_WHEELSET_description,
            iconRes = R.drawable.wheelset
        )

        ComponentType.FrameBearings -> ComponentResources(
            nameResId = R.string.componentType_FRAME_BEARINGS,
            descriptionResId = R.string.componentType_FRAME_BEARINGS_description,
            iconRes = R.drawable.bike_bearing
        )

        ComponentType.Custom -> ComponentResources(
            nameResId = R.string.componentType_CUSTOM,
            descriptionResId = R.string.componentType_CUSTOM_description,
            iconRes = R.drawable.bike_bearing
        )

        ComponentType.HandlebarTape -> ComponentResources(
            nameResId = R.string.componentType_HANDLEBAR_TAPE,
            descriptionResId = R.string.componentType_HANDLEBAR_TAPE_description,
            iconRes = R.drawable.bike_handlebar_tape
        )

        ComponentType.ChainRing -> ComponentResources(
            nameResId = R.string.componentType_CHAIN_RING,
            descriptionResId = R.string.componentType_CHAIN_RING_description,
            iconRes = R.drawable.chain_ring
        )
    }
}
