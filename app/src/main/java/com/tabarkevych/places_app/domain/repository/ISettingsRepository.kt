package com.tabarkevych.places_app.domain.repository

import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {

   suspend fun setAppTheme(isDarkMode: Boolean)

    fun getAppTheme(): Flow<Boolean>

    suspend  fun setMarkersListTextColor(color: Long)

    fun getMarkersListTextColor(): Flow<Long>

    suspend  fun setMarkersListBackgroundColor(color: Long)

    fun getMarkersListBackgroundColor(): Flow<Long>
}