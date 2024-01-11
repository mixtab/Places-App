package com.tabarkevych.places_app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tabarkevych.places_app.data.database.dao.SearchHistoryDao
import com.tabarkevych.places_app.data.database.entity.SearchHistoryEntity

@Database(
    version = 1,
    entities = [
        SearchHistoryEntity::class,
    ],
    exportSchema = true
)
abstract class PlacesAppDatabase : RoomDatabase() {

    companion object {

        internal const val NAME = "places_app.db"
    }

    abstract fun getSearchHistoryDao(): SearchHistoryDao


}