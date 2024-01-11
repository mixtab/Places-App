package com.tabarkevych.places_app.presentation.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarkevych.places_app.domain.repository.ISettingsRepository
import com.tabarkevych.places_app.presentation.model.SettingsDropDownItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingsRepository: ISettingsRepository
) : ViewModel() {

    val isDarkMode =
        settingsRepository.getAppTheme().filterIsInstance<Boolean>()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(), false
            )

    val markersListBackgroundColor =
        settingsRepository.getMarkersListBackgroundColor().map { value ->
            val item = BackgroundColorType.entries.first { it.color == value }
            SettingsDropDownItem(item.title, item.color.toString())
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                SettingsDropDownItem(
                    BackgroundColorType.SALOMIE.title,
                    BackgroundColorType.SALOMIE.color.toString()
                )
            )

    val markersListTextColor =
        settingsRepository.getMarkersListTextColor()
            .map { value ->
                val item = TextColorType.entries.first { it.color == value }
                SettingsDropDownItem(item.title, item.color.toString())
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(), SettingsDropDownItem(
                    TextColorType.MIRAGE.title,
                    TextColorType.MIRAGE.color.toString()
                )
            )


    fun setAppTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            AppCompatDelegate.setDefaultNightMode( if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            settingsRepository.setAppTheme(isDarkMode)
        }
    }

    fun setMarkersListTextColor(color: Long) {
        viewModelScope.launch {
            settingsRepository.setMarkersListTextColor(color)
        }
    }

    fun setMarkersListBackgroundColor(color: Long) {
        viewModelScope.launch {
            settingsRepository.setMarkersListBackgroundColor(color)
        }
    }
}