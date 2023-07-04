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

package com.anxops.bkn.ui.screens.login

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.data.network.firebase.SendTokenToServerWorker
import com.anxops.bkn.ui.navigation.BknNavigator
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
    viewModel: HandleLoginScreenViewModel = hiltViewModel(),
) {

    val nav = BknNavigator(navigator)
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    Log.d("HandleLoginScreen", "Tokens: [$code,$refresh]")

    viewModel.setAuthTokens(code, refresh)

    LaunchedEffect(key1 = context) {
        viewModel.loadProfileEvent.collect { ev ->
            nav.popBackStack()
            SendTokenToServerWorker.launch(context)
            when (ev) {
                is LoadProfileEvent.NewAccount -> nav.navigateToProfile()
                is LoadProfileEvent.ExistingAccount -> nav.navigateToGarage()
                else -> nav.navigateToLogin()
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

            // Text(text = state.value.profile?.profile?.firstname  + " Code: $code")

            when (state.value.isNewAccount) {
                null -> {
                    Text(
                        text = "Loading profile...",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h3
                    )
                }

                true -> {
                    Text(
                        text = "Welcome ${state.value.profile?.firstname}...",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h3
                    )
                }

                else -> {
                    Text(
                        text = "Hi ${state.value.profile?.firstname}, nice to see you again!",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h3
                    )
                }
            }


        }
    }
}