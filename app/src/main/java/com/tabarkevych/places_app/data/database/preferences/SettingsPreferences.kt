package com.tabarkevych.places_app.data.database.preferences

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.tabarkevych.places_app.presentation.ui.settings.BackgroundColorType
import com.tabarkevych.places_app.presentation.ui.settings.TextColorType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsPreferences @Inject constructor(
    @ApplicationContext context: Context,
) : ISettingsPreferences {

    private val dataStore by lazy {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }),
            migrations = listOf(SharedPreferencesMigration(context, SETTINGS_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(SETTINGS_PREFERENCES_NAME) }
        )
    }


    override suspend fun setAppTheme(isDarkMode: Boolean) {
        dataStore.edit {
            it[APP_THEME_KEY] = isDarkMode
        }
    }

    override fun getAppTheme(): Flow<Boolean> {
        return dataStore.data.map { it[APP_THEME_KEY] ?: (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) }
    }

    override suspend fun setMarkersListTextColor(color: Long) {
        dataStore.edit {
            it[MARKERS_LIST_TEXT_COLOR] = color
        }
    }

    override fun getMarkersListTextColor(): Flow<Long> {
        return dataStore.data.map {
            it[MARKERS_LIST_TEXT_COLOR] ?: TextColorType.MIRAGE.color
        }
    }

    override suspend fun setMarkersListBackgroundColor(color: Long) {
        dataStore.edit {
            it[MARKERS_LIST_BG_COLOR] = color
        }
    }

    override fun getMarkersListBackgroundColor(): Flow<Long> {
        return dataStore.data.map {
            it[MARKERS_LIST_BG_COLOR] ?: BackgroundColorType.SALOMIE.color
        }
    }

    companion object {
        private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"

        private val APP_THEME_KEY =
            booleanPreferencesKey("app_theme")

        private val MARKERS_LIST_BG_COLOR =
            longPreferencesKey("markers_list_bg_color")

        private val MARKERS_LIST_TEXT_COLOR =
            longPreferencesKey("markers_list_text_color")

    }
}