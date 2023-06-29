package com.anxops.bkn.ui.navigation


import com.anxops.bkn.ui.screens.NavGraph
import com.anxops.bkn.ui.screens.NavGraphs
import com.anxops.bkn.ui.screens.destinations.GarageDestination
import com.anxops.bkn.ui.screens.destinations.LoginScreenDestination
import com.anxops.bkn.ui.screens.destinations.NewBikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.RideScreenDestination
import com.anxops.bkn.ui.screens.destinations.SetupProfileScreenDestination
import com.anxops.bkn.ui.screens.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

enum class DeepLinkDestination(val value: String) {
    GARAGE("garage")
}

class BknNavigator(var navigator: DestinationsNavigator) {

    fun navigateToSplash() {
        navigator.navigate(SplashScreenDestination.route)
    }

    fun navigateToLogin(sessionExpired: Boolean = false) {
        navigator.navigate(LoginScreenDestination.invoke(sessionExpired = sessionExpired))
    }

    fun navigateToGarage() {
        navigator.navigate(GarageDestination.invoke(section = null))
    }

    fun navigateToGarage(section: String) {
        navigator.navigate(GarageDestination.invoke(section = section))
    }

    fun navigateToProfile() {
        navigator.navigate(SetupProfileScreenDestination.route)
    }

    fun navigateToNewBike() {
        navigator.navigate(NewBikeScreenDestination.invoke())
    }

    fun navigateToBike(id: String) {
        navigator.navigate(NewBikeScreenDestination.invoke(id))
    }

    fun navigateToRide(id: String) {
        navigator.navigate(RideScreenDestination.invoke(id))
    }

    fun popBackStack() {
        navigator.popBackStack()
    }

    fun popBackStackTo(route: String, inclusive: Boolean) {
        navigator.popBackStack(route, inclusive)
    }

    fun navigateToRoute(route: String) {
        navigator.navigate(route)
    }

    companion object {
        fun rootNavGraph(): NavGraph {
            return NavGraphs.root
        }
    }

}
