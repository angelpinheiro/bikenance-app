package com.anxops.bkn.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterialNavigationApi
@Composable
fun BottomBar(
    navController: NavHostController
) {
//    BottomNavigation(
//        elevation = 5.dp
//    ) {
//        BottomBarItem.values().forEach { destination ->
//
//            val isCurrent = destination.direction.route == navController.currentDestination?.route
//            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)
//
//            BottomNavigationItem(
//                selected = isCurrent,
//                onClick = {
//
//                    // When we click again on a bottom bar item and it was already selected
//                    // we want to pop the back stack until the initial destination of this bottom bar item
//                    navController.popBackStack(destination.direction, false)
//
//                    if (isCurrentDestOnBackStack) {
//                        return@BottomNavigationItem
//                    }
//
//                    // navController.popBackStack()
//                    navController.nav(destination.direction.route)
//                },
//                icon = {
//
//                    val isCurrent =
//                        destination.direction.route == navController.currentDestination?.route
//
//                    BadgedBox(badge = {
//                        if (destination.title.contains("Maintenance")) {
//                            Badge {
//                                Text("3")
//                            }
//                        }
//                    }) {
//                        BknIcon(
//                            destination.icon,
//                            ColorFilter.tint(MaterialTheme.colors.onPrimary),
//                            modifier = Modifier
//                                .size(18.dp)
//                        )
//                    }
//                },
//                label = {
//
//                    val isCurrent =
//                        destination.direction.route == navController.currentDestination?.route
//                    Text(
//                        destination.title,
//                        color = MaterialTheme.colors.onPrimary,
//                        style = if (isCurrent) MaterialTheme.typography.h5 else MaterialTheme.typography.h4,
//                        modifier = Modifier.alpha(if (isCurrent) 1f else 0.5f)
//                    )
//                },
//
//                )
//        }
//    }
}
//
//fun NavHostController.nav(r: String) {
//    this.navigate(r) {
//        // Pop up to the root of the graph to
//        // avoid building up a large stack of destinations
//        // on the back stack as users select items
//        popUpTo(NavGraphs.garage.route) {
//            saveState = true
//        }
//
//        // Avoid multiple copies of the same destination when
//        launchSingleTop = true
//        // Restore state when reselecting a previously selected item
//        restoreState = true
//    }
//}
//
//@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialNavigationApi::class)
//enum class BottomBarItem(
//    val direction: DirectionDestination,
//    val icon: IIcon,
//    val title: String
//) {
//    Home(HomeScreenDestination, CommunityMaterial.Icon.cmd_bike, "Bikes"),
//    Rides(RidesScreenDestination, CommunityMaterial.Icon.cmd_bike_fast, "Rides"),
//    Maintenances(MaintenancesScreenDestination, CommunityMaterial.Icon.cmd_cog, "Maintenance"),
//}