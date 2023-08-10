package com.anxops.bkn.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.shared.ConnectionStateBanner
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun app(viewModel: AppViewModel = hiltViewModel()) {
    val showConnectionState by viewModel.displayConnectionState.collectAsState()
    val showBanner = showConnectionState !is DisplayConnectionState.DisplayNone
    val navController = rememberNavController()
    BikenanceAndroidTheme(darkTheme = true) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant)
        ) {
            DestinationsNavHost(
                modifier = if (showBanner) Modifier.weight(1f) else Modifier.fillMaxSize(),
                navController = navController,
                navGraph = BknNavigator.rootNavGraph()
            )
            if (showBanner) {
                ConnectionStateBanner(showConnectionState, Modifier.background(MaterialTheme.colors.primaryVariant))
            }
        }
    }
}
