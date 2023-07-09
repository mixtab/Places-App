package com.tabarkevych.places_app.presentation.navigation

sealed class NavDestinationHelper(val route: String) {
    object MapScreen : NavDestinationHelper("map_screen")
    object MarkersListScreen : NavDestinationHelper("markers_list_screen")
    object MarkerDetailsScreen : NavDestinationHelper("marker_details_screen")

}