package com.anxops.bkn.ui.shared.components

import androidx.compose.animation.core.AnimationConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.anxops.bkn.data.repository.AppError
import com.anxops.bkn.data.repository.ErrorType
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.HomeScreenDestination
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import kotlinx.coroutines.delay

@Composable
fun ErrorDialog(appError: AppError, bknNavigator: BknNavigator?, onDismissRequest: () -> Unit) {

    val (title, defaultText) = getErrorTitleAndText(appError)
    val content = (appError.message?.let { "$it. " } ?: "") + defaultText

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(appError) {
        visible = true
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(AnimationConstants.DefaultDurationMillis.toLong())
            onDismissRequest()
        }
    }

    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnBackPress = true
        ), onDismissRequest = onDismissRequest
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            SlideFromBottomAnimatedVisibility(visible = visible) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .shadow(8.dp)
                        .background(MaterialTheme.colors.primary)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = title,
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h2,
                        )
                        IconButton(onClick = { visible = false }) {
                            BknIcon(icon = CommunityMaterial.Icon.cmd_close)
                        }
                    }

                    Text(
                        text = content,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    ErrorActions(appError, bknNavigator)
                }
            }
        }
    }
}


@Composable
private fun getErrorTitleAndText(appError: AppError) = when (appError.type) {
    is ErrorType.Network -> "Network error" to "Something failed while talking to server."
    is ErrorType.Authorization -> "Authorization error" to "Your session may have expired, try login again."
    is ErrorType.Backend -> "Server error" to "Something went wrong on the clouds, please try again later."
    is ErrorType.Unexpected -> "Unexpected error" to "Something went wrong."
}

@Composable
private fun ErrorActions(appError: AppError, bknNavigator: BknNavigator?) = when (appError.type) {

    ErrorType.Authorization -> {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(
                onClick = {
                    bknNavigator?.popBackStackTo(HomeScreenDestination.route, true)
                    bknNavigator?.navigateToLogin()
                },
                colors = secondaryButtonColors(),
            ) {
                Text("Login again")
            }
        }
    }

    else -> {}
}

