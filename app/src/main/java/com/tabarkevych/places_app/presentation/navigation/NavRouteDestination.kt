package com.tabarkevych.places_app.presentation.navigation

sealed class NavRouteDestination(val route: String) {
    object MapScreen : NavRouteDestination("map_screen")
    object MarkersListScreen : NavRouteDestination("markers_list_screen")
    object MarkerDetailsScreen : NavRouteDestination("marker_details_screen")
    object SearchScreen : NavRouteDestination("search_screen")
    object SettingsScreen : NavRouteDestination("settings_screen")

}