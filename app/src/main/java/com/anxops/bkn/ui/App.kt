package com.anxops.bkn.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun app() {
    val navController = rememberNavController()
    BikenanceAndroidTheme(darkTheme = true) {
        DestinationsNavHost(
            navController = navController, navGraph = BknNavigator.rootNavGraph()
        )
    }
}