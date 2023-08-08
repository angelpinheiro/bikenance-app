package com.anxops.bkn.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.shared.ConnectionStateBanner
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun app() {
    val navController = rememberNavController()
    BikenanceAndroidTheme(darkTheme = true) {
        Column(Modifier.fillMaxSize().background(MaterialTheme.colors.primary)) {
            DestinationsNavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                navGraph = BknNavigator.rootNavGraph()
            )
            ConnectionStateBanner(Modifier.background(MaterialTheme.colors.primary))
        }
    }
}
