package com.tabarkevych.places_app.presentation.ui.base

import androidx.lifecycle.ViewModel
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: IAuthRepository
) : ViewModel() {

    val isUserSignInState =
        authRepository.getUserChangeFlow().map { it != null }
}