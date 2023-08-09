package com.anxops.bkn.ui.screens.login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.Browser
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.BuildConfig
import com.anxops.bkn.R
import com.anxops.bkn.data.network.ApiEndpoints
import com.anxops.bkn.data.preferences.BknDataStore
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.onSurfaceTextFieldColors
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.ui.theme.strava
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginScreenViewModel = hiltViewModel(),
    sessionExpired: Boolean = false
) {
    val context = LocalContext.current
    val store = BknDataStore(context)
    val state = viewModel.state.collectAsState()
    val useDebugApi = viewModel.useDebugApi.collectAsState(true)

    LaunchedEffect(key1 = context) {
        viewModel.loginEvent.collect {
            onClickLogin(navigator, it, context)
        }
    }

    val colors = onSurfaceTextFieldColors()

    BackgroundBox(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_login_artwork),
//            contentDescription = "LocationPin",
//
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .align(Alignment.BottomEnd),
//            contentScale = ContentScale.FillBounds
//
//        )

        if (sessionExpired) {
//            Text(
//                text = "Your session has been closed, please sign in again!",
//                color = MaterialTheme.colors.onSecondary,
//                style = MaterialTheme.typography.h3,
//                modifier = Modifier
//                    .background(MaterialTheme.colors.secondary)
//                    .fillMaxWidth()
//                    .padding(10.dp)
//                    .align(Alignment.BottomEnd)
//            )
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.bicycle),
                contentDescription = stringResource(R.string.bikenance_logo_content_description),
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(R.string.login_screen_title),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h2
            )
            Text(
                text = stringResource(R.string.login_screen_subtitle),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 5.dp, bottom = 100.dp)
            )

//            BknOutlinedTextField(
//                value = state.value.email,
//                label = "E-mail address",
//                update = { viewModel.updateEmail(it) },
//                modifier = Modifier.padding(top = 36.dp),
//                colors = colors
//            )
//            BknOutlinedTextField(
//                value = state.value.password,
//                label = "Password",
//                update = { viewModel.updatePassword(it) },
//                modifier = Modifier.padding(top = 16.dp, bottom = 40.dp),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                colors = colors
//            )
//            SignIn(onClick = { viewModel.signInWithEmailAndPassword() })

//            Text(
//                text = "Forgot Password ?",
//                color = MaterialTheme.colors.onPrimary,
//                modifier = Modifier.padding(top = 60.dp)
//            )
//
            if (BuildConfig.ENABLE_DEBUG_API) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Use debug api", color = MaterialTheme.colors.onPrimary)
                    RadioButton(selected = useDebugApi.value, onClick = {
                        viewModel.setUseDebugApi(useDebugApi.value.not())
                    }, colors = RadioButtonDefaults.colors(MaterialTheme.colors.onPrimary))
                }
            }
        }

        Box(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
        ) {
            AnimatedVisibility(visible = true) {
                ConnectWithStrava(onClick = { viewModel.signInWithStrava() })
            }
        }
    }
}

@Composable
fun SignIn(onClick: () -> Unit = {}) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        onClick = onClick,
        modifier = Modifier.padding(top = 25.dp).requiredWidth(277.dp)

    ) {
        Text(text = "Sign In", modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun SignInWithStrava(onClick: () -> Unit = {}) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.strava
        ),
        onClick = onClick,
        modifier = Modifier.padding(top = 10.dp).requiredWidth(277.dp)

    ) {
        Text(text = stringResource(R.string.sign_in_with_strava), color = Color.White, modifier = Modifier.padding(6.dp))
    }
}

@Composable
fun ConnectWithStrava(onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.btn_strava_connect),
            contentDescription = stringResource(R.string.connect_with_strava),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.width(220.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BikenanceAndroidTheme(useSystemUIController = false, darkTheme = false) {
        LoginScreen(EmptyDestinationsNavigator)
    }
}

private fun onClickLogin(
    navigator: DestinationsNavigator,
    loginEvent: LoginEvent,
    context: Context
) {
    when (loginEvent) {
        is LoginEvent.StravaLogin -> {
            navigator.popBackStack()
            launchStravaLogin(context)
        }

        is LoginEvent.EmailPasswordLogin -> {
            Toast.makeText(
                context,
                loginEvent.email + ":" + loginEvent.password,
                Toast.LENGTH_SHORT
            ).show()
        }

        else -> {}
    }
}

private fun launchStravaLogin(context: Context) {
    val intent = CustomTabsIntent.Builder().build()

    val headers = Bundle()
    headers.putString("Content-Type", "application/json")
    intent.intent.putExtra(Browser.EXTRA_HEADERS, headers)
    intent.launchUrl(context, Uri.parse(ApiEndpoints.stravaLoginEndpoint()))
}
