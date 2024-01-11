package com.tabarkevych.places_app.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.PlaceTypes

import com.tabarkevych.places_app.domain.model.SearchHistory
import com.tabarkevych.places_app.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow
import com.tabarkevych.places_app.domain.base.Result

interface ISearchRepository {

    fun getSearchHistory(): Flow<List<SearchHistory>>

    suspend fun addSearchHistory(item: SearchHistory)

    suspend fun removeSearchHistory()

}