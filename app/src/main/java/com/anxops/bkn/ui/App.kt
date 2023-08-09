package com.anxops.bkn.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.shared.ConnectionStateBanner
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun app(viewModel: AppViewModel = hiltViewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.displayConnectionState.collectAsState()
    val navController = rememberNavController()

//    viewModel.subscribeToConnectivity(lifeCycleScope = lifecycleOwner.lifecycleScope, lifeCycle = lifecycleOwner.lifecycle)

    BikenanceAndroidTheme(darkTheme = true) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primaryVariant)
        ) {
            DestinationsNavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                navGraph = BknNavigator.rootNavGraph()
            )
            ConnectionStateBanner(state, Modifier.background(MaterialTheme.colors.primaryVariant))
        }
    }
}
