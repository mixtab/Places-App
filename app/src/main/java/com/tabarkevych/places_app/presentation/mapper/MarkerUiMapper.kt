package com.tabarkevych.places_app.presentation.mapper

import com.tabarkevych.places_app.domain.model.Marker
import com.tabarkevych.places_app.presentation.model.MarkerUi


fun Marker.toUi() =
    MarkerUi(
        timestamp = timestamp?:0,
        latitude = latitude?:"",
        longitude  = longitude?:"",
        image = image?:"",
        title = title?:"",
        description = description?:""
    )

fun List<Marker>.toUi() = this.map { it.toUi() }

fun MarkerUi.toDomain() =
    Marker(
        timestamp = timestamp,
        latitude = latitude,
        longitude  = longitude,
        image = image,
        title = title,
        description = description
    )

fun List<MarkerUi>.toDomain() = this.map { it.toDomain() }

