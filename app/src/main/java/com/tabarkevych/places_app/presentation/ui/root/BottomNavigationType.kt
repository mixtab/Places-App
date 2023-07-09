package com.tabarkevych.places_app.presentation.ui.root

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tabarkevych.places_app.R

enum class BottomNavigationType(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    MARKERS_LIST("map_screen",R.string.title_map,R.drawable.ic_map),
    MAP("markers_list_screen", R.string.title_markers,R.drawable.ic_markers)
}
