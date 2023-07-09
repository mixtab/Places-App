package com.tabarkevych.places_app.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.tabarkevych.places_app.domain.model.UserInfo
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.extensions.executeSafeWithResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import com.tabarkevych.places_app.domain.base.Result
import com.tabarkevych.places_app.domain.mapper.toUserInfo

class FirebaseAuthRepository @Inject constructor(
    @ApplicationContext context: Context
) : IAuthRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun getUserChangeFlow(): Flow<UserInfo?> = callbackFlow {
        val authListener = FirebaseAuth.AuthStateListener {
            trySendBlocking(it.currentUser?.toUserInfo())
        }
        auth.addAuthStateListener(authListener)

        awaitClose {
            auth.removeAuthStateListener(authListener)
        }
    }

    override fun getUserId(): String? {
        return auth.currentUser?.uid
    }


    override suspend fun signOut(): Result<Unit> =
        executeSafeWithResult {
            Result.Success(auth.signOut())
        }

}