package com.anxops.bkn.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.ComponentTypes


val defaultComponentIcons = mapOf(
    ComponentTypes.FRAME_BEARINGS to R.drawable.bike_bearing,
    ComponentTypes.BRAKE_LEVER to R.drawable.brake_lever,
    ComponentTypes.CASSETTE to R.drawable.cassette,
    ComponentTypes.DISC_BRAKE to R.drawable.disc_brake,
    ComponentTypes.FORK to R.drawable.fork,
    ComponentTypes.PEDAL_CLIPLESS to R.drawable.pedal_clipless,
    ComponentTypes.REAR_HUB to R.drawable.rear_hub,
    ComponentTypes.THRU_AXLE to R.drawable.thru_axle,
    ComponentTypes.WHEELSET to R.drawable.wheelset,
    ComponentTypes.CABLE_HOUSING to R.drawable.cable_housing,
    ComponentTypes.CHAIN to R.drawable.chain,
    ComponentTypes.DISC_PAD to R.drawable.disc_pad,
    ComponentTypes.DROPER_POST to R.drawable.droper_post,
    ComponentTypes.FRONT_HUB to R.drawable.front_hub,
    ComponentTypes.REAR_DERAUILLEURS to R.drawable.rear_derauilleurs,
    ComponentTypes.REAR_SUSPENSION to R.drawable.rear_suspension,
    ComponentTypes.HANDLEBAR_TAPE to R.drawable.bike_handlebar_tape,
    ComponentTypes.TIRE to R.drawable.tire,
)

@Composable
fun BikeComponentIcon(
    type: ComponentTypes,
    modifier: Modifier = Modifier.padding(3.dp),
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {

    val icon = remember(type) {
        mutableStateOf (
            defaultComponentIcons[type] ?: R.drawable.tire
        )
    }

    Image(
        imageVector = ImageVector.vectorResource(id = icon.value),
        contentDescription = type.name,
        modifier = modifier,
        colorFilter = ColorFilter.tint(tint)

    )
}





