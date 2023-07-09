package com.tabarkevych.places_app.presentation.ui.root.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tabarkevych.places_app.presentation.navigation.NavDestinationHelper
import com.tabarkevych.places_app.presentation.ui.map.MapScreenRoute
import com.tabarkevych.places_app.presentation.ui.marker_details.MarkerDetailsScreenRoute
import com.tabarkevych.places_app.presentation.ui.markers_list.MarkersListScreenRoute

@Composable
fun PlacesAppNavHost(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
    onEvent: (HandleActivityEvent) -> Unit

) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = androidx.compose.ui.Modifier.padding(paddingValues)
    ) {
        composable(NavDestinationHelper.MapScreen.route) {
            MapScreenRoute(navController = navController, { onEvent(HandleActivityEvent.UserSignIn) })
        }
        composable(NavDestinationHelper.MarkerDetailsScreen.route + "/{markerId}") {
            MarkerDetailsScreenRoute(navController)
        }
        composable(NavDestinationHelper.MarkersListScreen.route) {
            MarkersListScreenRoute(navController, { onEvent(HandleActivityEvent.UserSignIn) })
        }
    }
}


sealed class HandleActivityEvent {
    object UserSignIn : HandleActivityEvent()
}