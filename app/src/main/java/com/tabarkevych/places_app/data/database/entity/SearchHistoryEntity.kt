package com.tabarkevych.places_app.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    val id:String,
    val title:String,
    val subtitle:String,

)