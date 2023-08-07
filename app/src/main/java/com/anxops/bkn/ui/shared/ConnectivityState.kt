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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.network.ConnectionState
import com.anxops.bkn.data.network.currentConnectivityState
import com.anxops.bkn.data.network.observeConnectivityAsFlow
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.FadeInFadeOutSlideAnimatedVisibility
import com.anxops.bkn.ui.theme.statusDanger
import com.anxops.bkn.ui.theme.statusGood
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current
    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().distinctUntilChanged().collect {
            Timber.d("[ConnectivityState] ConnectionState: $it")
            value = it
        }
    }
}

@Composable
fun ConnectionStateBanner(modifier: Modifier = Modifier) {

    val connectionState by connectivityState()

    var prevConnectionState by remember {
        mutableStateOf<ConnectionState?>(null)
    }

    var showConnectionState by remember {
        mutableStateOf<ConnectionState?>(null)
    }

    val isConnected = connectionState === ConnectionState.Available

    LaunchedEffect(connectionState) {
        // if disconnected, show banner
        if (!isConnected) {
            showConnectionState = connectionState
        }
        // connection recovery, show banner during 2 seconds
        else if (prevConnectionState == ConnectionState.Unavailable) {
            showConnectionState = connectionState
            delay(2000)
            showConnectionState = null
        } // in other case, there is connection, hide banner
        else {
            showConnectionState = null
        }
        prevConnectionState = connectionState
    }

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

    FadeInFadeOutSlideAnimatedVisibility(showConnectionState != null) {
        Box(
            Modifier
                .fillMaxWidth()
                .then(modifier), contentAlignment = Alignment.TopStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color)
                    .padding(horizontal = 5.dp, vertical = 2.dp),
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