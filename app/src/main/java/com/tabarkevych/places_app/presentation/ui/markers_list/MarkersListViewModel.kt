package com.tabarkevych.places_app.presentation.ui.markers_list

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.domain.repository.IMarkersRepository
import com.tabarkevych.places_app.domain.repository.ISettingsRepository
import com.tabarkevych.places_app.presentation.mapper.toUi
import com.tabarkevych.places_app.presentation.model.MarkerUi
import com.tabarkevych.places_app.presentation.model.SettingsDropDownItem
import com.tabarkevych.places_app.presentation.ui.settings.BackgroundColorType
import com.tabarkevych.places_app.presentation.ui.settings.TextColorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkersListViewModel @Inject constructor(
    private val markersRepository: IMarkersRepository,
    settingsRepository: ISettingsRepository,
    authRepository: IAuthRepository
) : ViewModel() {

    val markersState = MutableSharedFlow<PagingData<MarkerUi>>()

    val isUserSignInState = authRepository.getUserChangeFlow().map {
        if (it != null) {
            getMarkers()
        }

        it != null
    }


    val markersListBackgroundColor =
        settingsRepository.getMarkersListBackgroundColor()
            .map { Color(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                Color(BackgroundColorType.SALOMIE.color)
            )

    val markersListTextColor =
        settingsRepository.getMarkersListTextColor()
            .map { Color(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                Color(TextColorType.MIRAGE.color)
            )


    private fun getMarkers() {
        viewModelScope.launch {
            markersRepository.getMarkersPaging().collectLatest {
                markersState.emit(it.map { marker -> marker.toUi() })
            }
        }
    }

    val markerPreviewState = MutableStateFlow<MarkerPreview>(MarkerPreview.SmallPreview)


    fun updateMarkersPreview() {
        markerPreviewState.update {
            when (markerPreviewState.value) {
                MarkerPreview.SmallPreview -> MarkerPreview.LargePreview
                else -> MarkerPreview.SmallPreview
            }
        }
    }

    sealed class MarkerPreview {
        object SmallPreview : MarkerPreview()
        object LargePreview : MarkerPreview()
    }

}