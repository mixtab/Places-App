package com.tabarkevych.places_app.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.base.whenSuccess
import com.tabarkevych.places_app.domain.manager.location.DeviceLocationManager
import com.tabarkevych.places_app.domain.manager.routing.RoutingManager
import com.tabarkevych.places_app.domain.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val routingManager: RoutingManager,
    private val locationManager: DeviceLocationManager
) : ViewModel() {


    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState = _searchUiState.asSharedFlow()

    private val _showSearchResultOnMapEvent = MutableSharedFlow<LatLng>()
    val showSearchResultOnMapEvent = _showSearchResultOnMapEvent.asSharedFlow()


    init {
        // fetchSearchHistory
    }

    fun fetchSearchResults(searchValue: String) {
        viewModelScope.launch {
            routingManager.fetchPlacesByText(
                searchValue,
                LatLng(
                    locationManager.getLocationUpdates().first().latitude,
                    locationManager.getLocationUpdates().first().longitude
                )
            ).firstOrNull()?.let {
                it.whenSuccess {
                    _searchUiState.emit(_searchUiState.value.copy(searchResults = this))
                }
            }
        }
    }

    fun fetchPlaceDetails(id: String) {
        viewModelScope.launch {
            routingManager.fetchPlaceInfoById(id).firstOrNull()?.whenSuccess {
                _showSearchResultOnMapEvent.emit(this)
            }
        }
    }

    data class SearchUiState(
        val searchHistory: List<String> = listOf(),
        val searchResults: List<SearchResult> = listOf()
    )
}