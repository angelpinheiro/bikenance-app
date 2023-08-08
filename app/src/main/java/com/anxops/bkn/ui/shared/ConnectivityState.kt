package com.anxops.bkn.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.network.ConnectionState
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.FadeInFadeOutSlideAnimatedVisibility
import com.anxops.bkn.ui.theme.statusDanger
import com.anxops.bkn.ui.theme.statusGood
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun ConnectionStateBanner(connectionState: ConnectionState?, modifier: Modifier = Modifier) {
    val isConnected = connectionState === ConnectionState.Available

    val icon = if (isConnected) {
        CommunityMaterial.Icon.cmd_cloud_check_outline
    } else {
        CommunityMaterial.Icon.cmd_cloud_off_outline
    }
    val msg = if (isConnected) {
        "You're back online!"
    } else {
        "You're offline"
    }
    val color = if (isConnected) {
        MaterialTheme.colors.statusGood
    } else {
        MaterialTheme.colors.statusDanger
    }

    FadeInFadeOutSlideAnimatedVisibility(connectionState != null) {
        Box(
            Modifier.fillMaxWidth().then(modifier),
            contentAlignment = Alignment.TopStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clip(RoundedCornerShape(16.dp))
                    .background(color).padding(horizontal = 5.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                BknIcon(icon = icon, modifier = Modifier.size(16.dp))
                Spacer(Modifier.size(6.dp))
                Text(

                    text = msg,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}
