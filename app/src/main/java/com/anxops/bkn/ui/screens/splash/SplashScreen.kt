/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterialNavigationApi::class)
@RootNavGraph(start = true)
@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "bikenance://garage?section={section}"
        ),
        DeepLink(
            uriPattern = "bikenance://notification?route={route}"
        )
    ]
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
        println("ROUTE: $route")
        nav.popBackStack()
        nav.navigateToRoute(route)
    }

    // Execute this when isLogged state changes
    LaunchedEffect(isLogged.value) {
        when (isLogged.value) {
            is CheckLoginState.LoggedIn -> {
                SendTokenToServerWorker.launch(context)
                nav.popBackStack()
                if (section != null)
                    nav.navigateToGarage(section)
                else
                    nav.navigateToGarage()
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

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.bicycle),
                contentDescription = "LocationPin",
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = "Bikenance",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h2
            )
        }
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