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
    BikeComponentType.FRAME to R.drawable.noun_bike_frame_2,
    BikeComponentType.FORK to R.drawable.noun_suspension_fork,
    BikeComponentType.HANDLEBAR to null,
    BikeComponentType.BRAKES to R.drawable.noun_bottom_bracket,
    BikeComponentType.DERAILLEURS to R.drawable.noun_rear_derailleur,
    BikeComponentType.CHAIN to R.drawable.noun_chain,
    BikeComponentType.PEDALS to null,
    BikeComponentType.RIMS to R.drawable.noun_bike_tire,
    BikeComponentType.TIRES to R.drawable.noun_bike_tube,
    BikeComponentType.SADDLE to null,
    BikeComponentType.CABLES to null,
    BikeComponentType.BOTTOM_BRACKET to R.drawable.noun_bottom_bracket,
    BikeComponentType.HEADSET to null
)

@Composable
fun BikeComponentIcon(
    type: BikeComponentType,
    modifier: Modifier = Modifier.padding(3.dp),
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {

    val icon = remember {
        defaultComponentIcons[type] ?: R.drawable.noun_crank
    }

    Image(
        imageVector = ImageVector.vectorResource(id = icon),
        contentDescription = type.name,
        modifier = modifier,
        colorFilter = ColorFilter.tint(tint)

    )
}





