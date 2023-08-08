package com.anxops.bkn.ui.screens.bikeComponents.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.bikeComponentName
import com.anxops.bkn.ui.shared.components.BknIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun BikeComponentTopBar(
    component: BikeComponent,
    onClickBack: () -> Unit = {}
) {
    TopAppBar(
        contentPadding = PaddingValues(5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 5.dp
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(modifier = Modifier.padding(start = 6.dp), onClick = { onClickBack() }) {
                        BknIcon(
                            icon = CommunityMaterial.Icon.cmd_arrow_left,
                            color = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    BikeComponentIcon(
                        type = component.type,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.size(32.dp).clip(CircleShape).background(MaterialTheme.colors.surface).padding(5.dp)
                    )

                    Text(
                        text = bikeComponentName(component = component),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.padding(start = 12.dp, end = 4.dp).weight(1f)
                    )

                    IconButton(onClick = {}) {
                        BknIcon(
                            CommunityMaterial.Icon.cmd_delete,
                            MaterialTheme.colors.surface,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}
