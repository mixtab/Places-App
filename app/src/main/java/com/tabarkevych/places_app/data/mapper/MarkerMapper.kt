package com.tabarkevych.places_app.data.mapper

import com.google.firebase.database.DataSnapshot
import com.tabarkevych.places_app.domain.model.Marker

fun DataSnapshot.toMarker() =
    Marker(
        timestamp = key?.toLong(),
        latitude = child("latitude").getValue(String::class.java),
        longitude  = child("longitude").getValue(String::class.java),
        image = child("image").getValue(String::class.java),
        title = child("title").getValue(String::class.java),
        description = child("description").getValue(String::class.java)
    )