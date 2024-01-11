package com.tabarkevych.places_app.presentation.ui.base.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tabarkevych.places_app.presentation.navigation.NavRouteDestination
import com.tabarkevych.places_app.presentation.ui.map.MapScreenRoute
import com.tabarkevych.places_app.presentation.ui.marker_details.MarkerDetailsScreenRoute
import com.tabarkevych.places_app.presentation.ui.markers_list.MarkersListScreenRoute
import com.tabarkevych.places_app.presentation.ui.search.SearchScreenRoute
import com.tabarkevych.places_app.presentation.ui.settings.SettingsScreenRoute

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
        composable(NavRouteDestination.Map.route) {
            MapScreenRoute(navController = navController, { onEvent(HandleActivityEvent.UserSignIn) })
        }
        composable(NavRouteDestination.MarkerDetails.route + "/{markerId}") {
            MarkerDetailsScreenRoute(navController)
        }
        composable(NavRouteDestination.MarkersList.route) {
            MarkersListScreenRoute(navController, { onEvent(HandleActivityEvent.UserSignIn) })
        }
        composable(NavRouteDestination.Settings.route) {
            SettingsScreenRoute(navController)
        }
        composable(NavRouteDestination.Search.route) {
            SearchScreenRoute(navController)
        }
    }
}


sealed class HandleActivityEvent {
    object UserSignIn : HandleActivityEvent()
}