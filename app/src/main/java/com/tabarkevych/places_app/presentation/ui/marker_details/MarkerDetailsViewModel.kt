package com.tabarkevych.places_app.presentation.ui.marker_details

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.repository.IMarkersRepository
import com.tabarkevych.places_app.presentation.mapper.toDomain
import com.tabarkevych.places_app.presentation.mapper.toUi
import com.tabarkevych.places_app.presentation.model.MarkerUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkerDetailsViewModel @Inject constructor(
    private val markersRepository: IMarkersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _markerDetailsState = MutableSharedFlow<MarkerUi?>(1)
    val markerDetailsState = _markerDetailsState.asSharedFlow()

    private val _editableState = MutableStateFlow(false)
    val editableState = _editableState.asSharedFlow()

    init {
        savedStateHandle.get<String>("markerId")?.let {
            getMarker(it.toLong())
        }
    }

    private fun getMarker(markerId: Long) {
        viewModelScope.launch {
            markersRepository.getMarkerById(markerId).collectLatest {
                _markerDetailsState.emit(it.toUi())
            }
        }
    }

    fun updateMarker(image: String, newImageUri: Uri?, title: String, description: String) {
        viewModelScope.launch {
            _markerDetailsState.replayCache.firstOrNull()?.let { marker ->
                if (newImageUri != null) {
                    markersRepository.uploadImageWithMarker(
                        marker.timestamp,
                        newImageUri,
                        LatLng(marker.latitude.toDouble(), marker.longitude.toDouble()), title,
                        description
                    )
                } else {
                    markersRepository.updateMarker(
                        marker.copy(
                            image = image,
                            title = title,
                            description = description
                        ).toDomain()
                    )
                }
                _editableState.update { false }
            }
        }
    }

    fun removeMarker() {
        viewModelScope.launch {
            _markerDetailsState.firstOrNull()?.timestamp?.let {
                markersRepository.deleteMarker(it)
            }
        }
    }

    fun onEditClick() {
        _editableState.update { true }
    }

}