package com.tabarkevych.places_app.presentation.navigation

sealed class NavRouteDestination(val route: String) {
    data object Map : NavRouteDestination("map_screen")
    data object MarkersList : NavRouteDestination("markers_list_screen")
    data object MarkerDetails : NavRouteDestination("marker_details_screen")
    data object Search : NavRouteDestination("search_screen")
    data object Settings : NavRouteDestination("settings_screen")

}