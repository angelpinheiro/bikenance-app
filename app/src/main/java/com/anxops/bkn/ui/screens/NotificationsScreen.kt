//package com.anxops.bkn.ui.screens
//
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import com.anxops.bkn.ui.navigation.BknNavigator
//import com.anxops.bkn.ui.screens.destinations.GarageDestination
//import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
//import com.ramcosta.composedestinations.annotation.DeepLink
//import com.ramcosta.composedestinations.annotation.Destination
//import com.ramcosta.composedestinations.navigation.DestinationsNavigator
//
////@Destination(
////    deepLinks = [
////        DeepLink(
////            uriPattern = "bikenance://garage?section={section}"
////        )
////    ]
////)
//@OptIn(ExperimentalMaterialNavigationApi::class)
//@Composable
//fun Notifications(
//    navigator: DestinationsNavigator,
//    section: String?
//) {
//    section?.let {
//        val bknNavigator = BknNavigator(navigator)
//        bknNavigator.popBackStackTo("root", false)
//        bknNavigator.navigateToGarage(it)
//    }
//
//    Text("Loading...")
//}