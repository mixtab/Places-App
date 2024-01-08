package com.tabarkevych.places_app.domain.model

data class SearchResult (
    val id:String,
    val title:String,
    val description:String,
    val distanceInMeters:Int
){
    fun getUiDistance(): String {
        return when {
            (distanceInMeters < 1000) ->{
                "$distanceInMeters m"
            }
            (distanceInMeters in 1001..9999) ->{
                String.format("%.1f km", distanceInMeters / 1000.0)
            }
            else -> {
                "${distanceInMeters / 1000} km"
            }
        }
    }
}