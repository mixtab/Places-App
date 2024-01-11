package com.tabarkevych.places_app.domain.di

import com.tabarkevych.places_app.data.repository.FirebaseAuthRepository
import com.tabarkevych.places_app.data.repository.FirebaseMarkersRepository
import com.tabarkevych.places_app.data.repository.SearchRepository
import com.tabarkevych.places_app.data.repository.SettingsRepository
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.domain.repository.IMarkersRepository
import com.tabarkevych.places_app.domain.repository.ISearchRepository
import com.tabarkevych.places_app.domain.repository.ISettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAuthRepository(repository: FirebaseAuthRepository): IAuthRepository

    @Binds
    fun bindMarkersRepository(repository: FirebaseMarkersRepository): IMarkersRepository

    @Binds
    fun bindSettingRepository(repository: SettingsRepository): ISettingsRepository

    @Binds
    fun bindSearchRepository(repository: SearchRepository): ISearchRepository
}