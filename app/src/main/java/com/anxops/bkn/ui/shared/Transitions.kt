package com.anxops.bkn.ui.shared

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object NavigationSlideUp : DestinationStyle.Animated {

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return fadeIn() + slideInVertically(animationSpec = tween(400), initialOffsetY = { fullHeight -> fullHeight })
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(200))
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return fadeIn() + slideInVertically(animationSpec = tween(400), initialOffsetY = { fullHeight -> fullHeight })
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(200))
    }
}

@OptIn(ExperimentalAnimationApi::class)
object NavigationFadeInOut : DestinationStyle.Animated {

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(400))
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(200))
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(400))
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(200))
    }
}
