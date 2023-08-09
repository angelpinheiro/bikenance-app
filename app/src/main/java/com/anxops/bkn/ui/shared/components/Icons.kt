package com.anxops.bkn.ui.shared.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.mikepenz.iconics.typeface.IIcon

@Composable
fun BknIcon(
    icon: IIcon,
    color: Color = MaterialTheme.colors.onPrimary,
    modifier: Modifier = Modifier.size(26.dp)
) {
    com.mikepenz.iconics.compose.Image(
        icon,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
    )
}
