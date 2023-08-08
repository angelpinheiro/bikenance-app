package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.ui.shared.BikeComponentIcon

@Composable
fun BikeComponentListItem(component: BikeComponent, onClick: () -> Unit = {}) {
    val prefix = component.modifier?.let {
        "${it.displayName} "
    } ?: ""

    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(top = 10.dp).clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BikeComponentIcon(
                type = component.type,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colors.surface).padding(6.dp)
            )

            Text(
                text = prefix + stringResource(component.type.resources().nameResId),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
