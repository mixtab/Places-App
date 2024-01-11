package com.tabarkevych.places_app.data.di

import android.content.Context
import androidx.room.Room
import com.tabarkevych.places_app.data.database.PlacesAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): PlacesAppDatabase {
        return Room.databaseBuilder(context, PlacesAppDatabase::class.java, PlacesAppDatabase.NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchDao(db: PlacesAppDatabase) = db.getSearchHistoryDao()

}