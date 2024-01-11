package com.tabarkevych.places_app.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.base.whenSuccess
import com.tabarkevych.places_app.domain.manager.location.DeviceLocationManager
import com.tabarkevych.places_app.domain.model.SearchHistory
import com.tabarkevych.places_app.domain.model.SearchResult
import com.tabarkevych.places_app.domain.repository.ISearchRepository
import com.tabarkevych.places_app.domain.use_case.search.FetchResultPlaceInfoUseCase
import com.tabarkevych.places_app.domain.use_case.search.FetchPlacesResultsByTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fetchPlacesResultsByTextUseCase: FetchPlacesResultsByTextUseCase,
    private val fetchPlaceByIdUseCase: FetchResultPlaceInfoUseCase,
    private val searchRepository: ISearchRepository,
    private val locationManager: DeviceLocationManager
) : ViewModel() {


    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState = _searchUiState.asSharedFlow()

    private val _showSearchResultOnMapEvent = MutableSharedFlow<LatLng>()
    val showSearchResultOnMapEvent = _showSearchResultOnMapEvent.asSharedFlow()


    init {
        viewModelScope.launch {
            searchRepository.getSearchHistory().collectLatest {
                _searchUiState.emit(_searchUiState.value.copy(searchHistory = it))
            }

        }
    }

    fun fetchSearchResults(searchValue: String) {
        viewModelScope.launch {
            fetchPlacesResultsByTextUseCase.execute(
                FetchPlacesResultsByTextUseCase.Params(
                    searchValue,
                    LatLng(
                        locationManager.getLocationUpdates().first().latitude,
                        locationManager.getLocationUpdates().first().longitude
                    )
                )
            ).firstOrNull()?.let {
                it.whenSuccess {
                    _searchUiState.emit(_searchUiState.value.copy(searchResults = this))
                }
            }
        }
    }

    fun fetchPlaceDetails(item: SearchResult) {
        viewModelScope.launch {
            fetchPlaceByIdUseCase.execute(FetchResultPlaceInfoUseCase.Params(item)).firstOrNull()
                ?.whenSuccess {
                    _showSearchResultOnMapEvent.emit(this)
                }
        }
    }

    fun removeSearchHistory() {
        viewModelScope.launch {
            searchRepository.removeSearchHistory()
        }
    }

    data class SearchUiState(
        val searchHistory: List<SearchHistory> = listOf(),
        val searchResults: List<SearchResult> = listOf()
    )
}