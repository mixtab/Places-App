package com.tabarkevych.places_app.domain.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.Distance
import com.google.maps.model.Duration

data class RouteInfo (
    val duration: Duration,
    val distanceInMeters:Distance,
    val routePoints:List<LatLng>
)