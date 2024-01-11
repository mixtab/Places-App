package com.tabarkevych.places_app.domain.use_case.search

import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.base.Result
import com.tabarkevych.places_app.domain.manager.places.PlacesManager
import com.tabarkevych.places_app.domain.model.SearchHistory
import com.tabarkevych.places_app.domain.model.SearchResult
import com.tabarkevych.places_app.domain.repository.ISearchRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

class FetchResultPlaceInfoUseCase @Inject constructor(
    private val placesManager: PlacesManager,
    private val searchRepository: ISearchRepository
) {

    suspend fun execute(params: Params): Flow<Result<LatLng>> {
        searchRepository.addSearchHistory(
            SearchHistory(
                Instant.now().toEpochMilli(),
                params.item.id,
                params.item.title,
                params.item.description
            )
        )
        return placesManager.fetchPlaceInfoById(params.item.id)
    }

    data class Params(val item: SearchResult)
}