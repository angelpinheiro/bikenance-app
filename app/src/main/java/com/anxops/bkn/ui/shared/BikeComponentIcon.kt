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
import com.anxops.bkn.data.model.ComponentType


@Composable
fun BikeComponentIcon(
    type: ComponentType,
    modifier: Modifier = Modifier.padding(3.dp),
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {

    val icon = remember(type) {
        mutableStateOf (
            type.resources().iconRes
        )
    }

    Image(
        imageVector = ImageVector.vectorResource(id = icon.value),
        contentDescription = type.name,
        modifier = modifier,
        colorFilter = ColorFilter.tint(tint)

    )
}





