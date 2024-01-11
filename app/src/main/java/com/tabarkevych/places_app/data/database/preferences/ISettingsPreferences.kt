package com.tabarkevych.places_app.data.database.preferences

import kotlinx.coroutines.flow.Flow

interface ISettingsPreferences {

    suspend fun setAppTheme(isDarkMode: Boolean)

    fun getAppTheme(): Flow<Boolean>

    suspend fun setMarkersListTextColor(color: Long)

    fun getMarkersListTextColor(): Flow<Long>

    suspend fun setMarkersListBackgroundColor(color: Long)

    fun getMarkersListBackgroundColor(): Flow<Long>
}
