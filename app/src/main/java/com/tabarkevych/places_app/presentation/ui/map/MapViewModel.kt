package com.tabarkevych.places_app.presentation.ui.map

import android.location.Location
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.base.whenSuccess
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.domain.repository.IMarkersRepository
import com.tabarkevych.places_app.presentation.mapper.toUi
import com.tabarkevych.places_app.presentation.model.MarkerUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationManager: com.tabarkevych.places_app.domain.location.DeviceLocationManager,
    private val markersRepository: IMarkersRepository,
    authRepository: IAuthRepository
) : ViewModel() {


    private val _userLocationState = MutableStateFlow<Location?>(null)
    val userLocationState = _userLocationState.asStateFlow()

    private val _showLoadingState = MutableStateFlow(false)
    val showLoadingState = _showLoadingState.asStateFlow()


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
            _showLoadingState.emit(true)
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

    fun addMarker(latLng: LatLng?, image: Uri?, title: String, description: String) {
        viewModelScope.launch {
            _showLoadingState.emit(true)
            if (latLng == null || image == null) return@launch

            markersRepository.uploadImageWithMarker( System.currentTimeMillis(),image, latLng, title, description)

        }

    }

}