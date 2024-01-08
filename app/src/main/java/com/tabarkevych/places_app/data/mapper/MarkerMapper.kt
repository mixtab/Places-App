package com.tabarkevych.places_app.data.mapper

import com.google.firebase.database.DataSnapshot
import com.tabarkevych.places_app.domain.model.Marker

fun DataSnapshot.toMarker():Marker {

   val photos =  child("marker_photos").children.map {  it.getValue(String::class.java)?:""}

  return  Marker(
        timestamp = key?.toLong(),
        latitude = child("latitude").getValue(String::class.java),
        longitude = child("longitude").getValue(String::class.java),
        images = photos,
        title = child("title").getValue(String::class.java),
        description = child("description").getValue(String::class.java)
    )

}