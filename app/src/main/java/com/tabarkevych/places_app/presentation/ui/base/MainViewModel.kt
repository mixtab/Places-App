package com.tabarkevych.places_app.presentation.ui.base

import androidx.lifecycle.ViewModel
import com.tabarkevych.places_app.data.repository.SettingsRepository
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.domain.repository.ISettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val settingsRepository: ISettingsRepository
) : ViewModel() {

    val isUserSignInState =
        authRepository.getUserChangeFlow().map { it != null }

   val  isDarkMode = settingsRepository.getAppTheme()
}