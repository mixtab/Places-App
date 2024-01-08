package com.tabarkevych.places_app.domain.mapper

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.tabarkevych.places_app.domain.model.SearchResult

fun AutocompletePrediction.toSearchResult() = SearchResult(
    this.placeId,
    this.getPrimaryText(null).toString(),
    this.getSecondaryText(null).toString(),
    this.distanceMeters
)

fun List<AutocompletePrediction>.toSearchResult() = this.map { it.toSearchResult() }