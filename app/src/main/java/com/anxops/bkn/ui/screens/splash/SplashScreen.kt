package com.anxops.bkn.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.network.firebase.SendTokenToServerWorker
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@OptIn(ExperimentalMaterialNavigationApi::class)
@RootNavGraph(start = true)
@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "bikenance://garage?section={section}"
        ), DeepLink(
            uriPattern = "bikenance://notification?route={route}"
        )
    ]
)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    viewModel: SplashScreenViewModel = hiltViewModel(),
    section: String?,
    route: String?

) {
    val context = LocalContext.current
    val nav = BknNavigator(navigator)

    val isLogged = viewModel.isLogged.collectAsState()

    route?.let {
        nav.popBackStack()
        nav.navigateToRoute(route)
    }

    // Execute this when isLogged state changes
    LaunchedEffect(isLogged.value) {
        when (isLogged.value) {
            is CheckLoginState.LoggedIn -> {
                SendTokenToServerWorker.launch(context)
                nav.popBackStack()
                if (section != null) {
                    nav.navigateToGarage(section)
                } else {
                    nav.navigateToGarage()
                }
            }

            is CheckLoginState.NotLoggedIn -> {
                nav.popBackStack()
                nav.navigateToLogin()
            }

            is CheckLoginState.LoginExpired -> {
                nav.popBackStack()
                nav.navigateToLogin(sessionExpired = true)
            }

            is CheckLoginState.Checking -> {
                // do nothing
            }
        }
    }

    BackgroundBox {}
}

@ExperimentalMaterialNavigationApi
@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    BikenanceAndroidTheme(useSystemUIController = false) {
        SplashScreen(EmptyDestinationsNavigator, section = null, route = null)
    }
}
