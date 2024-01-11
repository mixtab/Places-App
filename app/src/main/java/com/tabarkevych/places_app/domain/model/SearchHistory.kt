package com.tabarkevych.places_app.domain.model

data class SearchHistory(
    val timestamp: Long,
    val id:String,
    val title:String,
    val subtitle:String,
)
