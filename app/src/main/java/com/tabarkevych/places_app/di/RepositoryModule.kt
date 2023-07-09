package com.tabarkevych.places_app.di

import com.tabarkevych.places_app.data.repository.FirebaseAuthRepository
import com.tabarkevych.places_app.data.repository.FirebaseMarkersRepository
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.domain.repository.IMarkersRepository
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
}