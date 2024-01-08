package com.tabarkevych.places_app.presentation.ui.map

import android.location.Location
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.base.orError
import com.tabarkevych.places_app.domain.base.whenSuccess
import com.tabarkevych.places_app.domain.manager.location.DeviceLocationManager
import com.tabarkevych.places_app.domain.manager.routing.RoutingManager
import com.tabarkevych.places_app.domain.model.RouteInfo
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.domain.repository.IMarkersRepository
import com.tabarkevych.places_app.presentation.mapper.toUi
import com.tabarkevych.places_app.presentation.model.MarkerUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationManager: DeviceLocationManager,
    private val markersRepository: IMarkersRepository,
    private val routingManager: RoutingManager,
    authRepository: IAuthRepository
) : ViewModel() {


    private val _userLocationState = MutableStateFlow<Location?>(null)
    val userLocationState = _userLocationState.asStateFlow()

    private val _showLoadingState = MutableStateFlow(false)
    val showLoadingState = _showLoadingState.asStateFlow()

    private val _routeDestinationState = MutableStateFlow<RouteInfo?>(null)
    val routeDestinationState = _routeDestinationState.asStateFlow()

    private val _zoomToPlaceEvent = MutableSharedFlow<LatLng?>(1)
    val zoomToPlaceEvent = _zoomToPlaceEvent.asSharedFlow()


    val markersState = MutableStateFlow<List<MarkerUi>>(listOf())

    val isUserSignInState =
        authRepository.getUserChangeFlow().map {
            if (it != null) {
                getMarkers()
            }

            it != null
        }


    private fun getMarkers() {
        viewModelScope.launch {
            _showLoadingState.emit(false)
            markersRepository.getAllMarkers().collectLatest {
                it.whenSuccess {
                    _showLoadingState.emit(false)
                    markersState.update { this.toUi() }
                }
            }
        }
    }

    fun getUserLocation() {
        viewModelScope.launch {
            _userLocationState.emit(locationManager.getLocationUpdates().first())
        }
    }

    fun addMarker(latLng: LatLng?, images: List<Uri>?, title: String, description: String) {
        viewModelScope.launch {
            _showLoadingState.emit(true)
            if (latLng == null || images == null) return@launch

            markersRepository.uploadImageWithMarker(
                System.currentTimeMillis(),
                images,
                latLng,
                title,
                description
            )

        }
    }

    fun createRouteToLocation(endLocation: LatLng) {
        viewModelScope.launch {
            val startLocation = _userLocationState.value ?: return@launch
            routingManager.calculateRoute(
                LatLng(startLocation.latitude, startLocation.longitude),
                endLocation
            ).firstOrNull()
                ?.whenSuccess {
                    _routeDestinationState.emit(this)
                }?.orError {
                    val test = ""
                }
        }
    }

    fun removeRoute() {
        viewModelScope.launch {
            _routeDestinationState.emit(null)
        }
    }

    fun zoomToPlaceAction(place:LatLng){
        viewModelScope.launch {
            _zoomToPlaceEvent.emit(place)
            delay(1000)
            _zoomToPlaceEvent.emit(null)
        }
    }
}