package com.tabarkevych.places_app.presentation.model

data class MarkerUi(
    val timestamp:Long,
    val latitude: String,
    val longitude: String,
    val image: String,
    val title:String,
    val description:String
)