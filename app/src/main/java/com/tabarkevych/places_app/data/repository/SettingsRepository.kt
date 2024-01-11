package com.tabarkevych.places_app.data.repository

import com.tabarkevych.places_app.data.database.preferences.ISettingsPreferences
import com.tabarkevych.places_app.domain.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsPreferences: ISettingsPreferences
) : ISettingsRepository {
    override suspend fun setAppTheme(isDarkMode: Boolean) {
        settingsPreferences.setAppTheme(isDarkMode)
    }

    override fun getAppTheme(): Flow<Boolean> {
        return settingsPreferences.getAppTheme()
    }

    override suspend fun setMarkersListTextColor(color: Long) {
        settingsPreferences.setMarkersListTextColor(color)
    }

    override fun getMarkersListTextColor(): Flow<Long> {
        return settingsPreferences.getMarkersListTextColor()
    }

    override suspend fun setMarkersListBackgroundColor(color: Long) {
        settingsPreferences.setMarkersListBackgroundColor(color)
    }

    override fun getMarkersListBackgroundColor(): Flow<Long> {
        return settingsPreferences.getMarkersListBackgroundColor()
    }


}