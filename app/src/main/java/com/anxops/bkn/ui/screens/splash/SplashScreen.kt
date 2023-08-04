package com.anxops.bkn.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.data.network.firebase.SendTokenToServerWorker
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialNavigationApi::class)
@RootNavGraph(start = true)
@Destination(
    deepLinks = [DeepLink(
        uriPattern = "bikenance://garage?section={section}"
    ), DeepLink(
        uriPattern = "bikenance://notification?route={route}"
    )]
)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    viewModel: SplashScreenViewModel = hiltViewModel(),
    section: String?,
    route: String?,

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
                if (section != null) nav.navigateToGarage(section)
                else nav.navigateToGarage()
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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bicycle),
                contentDescription = "Bikenance Logo",
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(R.string.bikenance),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h2
            )
            Text(
                text = "   ",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 5.dp, bottom = 100.dp)
            )
        }

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.pwrdby_strava_stack),
            contentDescription = stringResource(R.string.powered_by_strava),
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 50.dp)
                .align(Alignment.BottomCenter)
        )
    }

}


@ExperimentalMaterialNavigationApi
@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    BikenanceAndroidTheme(useSystemUIController = false) {
        SplashScreen(EmptyDestinationsNavigator, section = null, route = null)
    }
}