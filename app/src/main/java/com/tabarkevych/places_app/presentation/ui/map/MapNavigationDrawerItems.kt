package com.tabarkevych.places_app.presentation.ui.map

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tabarkevych.places_app.R

enum class MapNavigationDrawerItems(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    MARKERS_LIST("markers_list_screen",R.string.title_markers, R.drawable.ic_markers),
    SETTINGS("settings_screen",R.string.title_settings, R.drawable.ic_markers)
}