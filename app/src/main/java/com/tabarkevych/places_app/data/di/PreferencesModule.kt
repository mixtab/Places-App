package com.tabarkevych.places_app.data.di

import com.tabarkevych.places_app.data.database.preferences.ISettingsPreferences
import com.tabarkevych.places_app.data.database.preferences.SettingsPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface PreferencesModule {

    @Binds
    @Singleton
    fun bindSettingsPreferences(preferences: SettingsPreferences): ISettingsPreferences

}