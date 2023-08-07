package com.anxops.bkn.ui.navigation


import com.anxops.bkn.ui.screens.NavGraph
import com.anxops.bkn.ui.screens.NavGraphs
import com.anxops.bkn.ui.screens.bike.BikeSections
import com.anxops.bkn.ui.screens.destinations.BikeComponentScreenDestination
import com.anxops.bkn.ui.screens.destinations.BikeEditScreenDestination
import com.anxops.bkn.ui.screens.destinations.BikeScreenDestination
import com.anxops.bkn.ui.screens.destinations.BikeSetupScreenDestination
import com.anxops.bkn.ui.screens.destinations.BikeSyncScreenDestination
import com.anxops.bkn.ui.screens.destinations.HomeScreenDestination
import com.anxops.bkn.ui.screens.destinations.LoginScreenDestination
import com.anxops.bkn.ui.screens.destinations.ProfileScreenDestination
import com.anxops.bkn.ui.screens.destinations.RideScreenDestination
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
        navigator.navigate(HomeScreenDestination.invoke(section = null))
    }

    fun navigateToGarage(section: String) {
        navigator.navigate(HomeScreenDestination.invoke(section = section))
    }

    fun navigateToProfile() {
        navigator.navigate(ProfileScreenDestination.route)
    }

    fun navigateToNewBike() {
        navigator.navigate(BikeEditScreenDestination.invoke())
    }

    fun navigateToBikeEdit(id: String) {
        navigator.navigate(BikeEditScreenDestination.invoke(id))
    }

    fun navigateToBikeSetup(id: String) {
        navigator.navigate(BikeSetupScreenDestination.invoke(id))
    }

    fun navigateToBike(
        id: String,
        section: String = BikeSections.Status.id,
        componentId: String? = null
    ) {
        navigator.navigate(BikeScreenDestination.invoke(id, section, componentId))
    }

    fun navigateToRide(id: String) {
        navigator.navigate(RideScreenDestination.invoke(id))
    }

    fun navigateToBikeSync() {
        navigator.navigate(BikeSyncScreenDestination.route)
    }

    fun navigateToBikeComponent(bikeId: String, componentId: String) {
        navigator.navigate(BikeComponentScreenDestination.invoke(bikeId, componentId))
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

    fun clear() {
        navigator.clearBackStack("")
    }

    companion object {
        fun rootNavGraph(): NavGraph {
            return NavGraphs.root
        }
    }

}
