package com.tabarkevych.places_app.presentation.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.toLatLang():LatLng{
    return LatLng(this.latitude,this.longitude)
}