package com.anxops.bkn.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.SplashScreenDestination
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import timber.log.Timber

@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "bikenance://redirect?code={code}&refresh={refresh}"
        )
    ]
)
@Composable
fun HandleLoginScreen(
    code: String?,
    refresh: String?,
    navigator: DestinationsNavigator,
    viewModel: HandleLoginScreenViewModel = hiltViewModel()
) {
    val nav = BknNavigator(navigator)
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    Timber.d("Handle login with tokens: [$code,$refresh]")

    viewModel.setAuthTokens(code, refresh)

    LaunchedEffect(context) {
        viewModel.loadProfileEvent.collect { ev ->
            when (ev) {
                is LoadProfileEvent.NewAccount -> {
                    delay(1000)
                    nav.popBackStackTo(SplashScreenDestination.route, true)
                    nav.navigateToProfile()
                }

                is LoadProfileEvent.ExistingAccount -> {
                    delay(1000)
                    nav.popBackStackTo(SplashScreenDestination.route, true)
                    nav.navigateToGarage()
                }

                else -> nav.navigateToLogin()
            }
        }
    }

    BackgroundBox {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_chain_ring),
                contentDescription = "LocationPin",
                modifier = Modifier.size(100.dp).padding(10.dp)
            )

            when (state.isNewAccount) {
                null -> {
                    Text(
                        text = stringResource(R.string.loading_profile_message),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h3
                    )
                }

                true -> {
                    Text(
                        text = stringResource(R.string.welcome_message, state.profile?.firstname ?: ""),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h3
                    )
                }

                else -> {
                    Text(
                        text = stringResource(R.string.welcome_again_message, state.profile?.firstname ?: ""),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h3
                    )
                }
            }
        }
    }
}
