package com.tabarkevych.places_app.domain.use_case.search

import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.base.Result
import com.tabarkevych.places_app.domain.manager.places.PlacesManager
import com.tabarkevych.places_app.domain.model.SearchResult

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPlacesResultsByTextUseCase @Inject constructor(
    private val placesManager: PlacesManager
) {

     suspend fun execute(params: Params):Flow<Result<List<SearchResult>>> {
       return placesManager.fetchPlacesByText(params.searchValue,params.userPosition)
    }

    data class Params(val searchValue: String, val userPosition: LatLng)
}