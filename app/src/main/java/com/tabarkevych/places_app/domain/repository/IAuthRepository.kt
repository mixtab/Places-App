package com.tabarkevych.places_app.domain.repository

import com.tabarkevych.places_app.domain.base.Result
import com.tabarkevych.places_app.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow


interface IAuthRepository {

    fun getUserChangeFlow(): Flow<UserInfo?>

    fun getUserId():String?

    suspend fun signOut():Result<Unit>

}