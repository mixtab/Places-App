package com.tabarkevych.places_app.domain.model

data class Marker(
    val timestamp:Long? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    val image: String? = null,
    val title:String? = null,
    val description:String? = null
)