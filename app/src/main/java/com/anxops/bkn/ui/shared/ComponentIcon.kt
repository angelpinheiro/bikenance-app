package com.anxops.bkn.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.BikeComponentType


val defaultComponentIcons = mapOf(
    BikeComponentType.BRAKE_LEVER to R.drawable.brake_lever,
    BikeComponentType.CASSETTE to R.drawable.cassette,
    BikeComponentType.DISC_BRAKE to R.drawable.disc_brake,
    BikeComponentType.FORK to R.drawable.fork,
    BikeComponentType.PEDAL_CLIPLESS to R.drawable.pedal_clipless,
    BikeComponentType.REAR_HUB to R.drawable.rear_hub,
    BikeComponentType.THRU_AXLE to R.drawable.thru_axle,
    BikeComponentType.WHEELSET to R.drawable.wheelset,
    BikeComponentType.CABLE_HOUSING to R.drawable.cable_housing,
    BikeComponentType.CHAIN to R.drawable.chain,
    BikeComponentType.DISC_PAD to R.drawable.disc_pad,
    BikeComponentType.DROPER_POST to R.drawable.droper_post,
    BikeComponentType.FRONT_HUB to R.drawable.front_hub,
    BikeComponentType.REAR_DERAUILLEURS to R.drawable.rear_derauilleurs,
    BikeComponentType.REAR_SUSPENSION to R.drawable.rear_suspension,
    BikeComponentType.TIRE to R.drawable.tire
)

@Composable
fun BikeComponentIcon(
    type: BikeComponentType,
    modifier: Modifier = Modifier.padding(3.dp),
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {

    val icon = remember {
        defaultComponentIcons[type] ?: R.drawable.tire
    }

    Image(
        imageVector = ImageVector.vectorResource(id = icon),
        contentDescription = type.name,
        modifier = modifier,
        colorFilter = ColorFilter.tint(tint)

    )
}





