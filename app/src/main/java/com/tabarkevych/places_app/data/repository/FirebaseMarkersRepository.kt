package com.tabarkevych.places_app.data.repository

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.tabarkevych.places_app.data.paging.MarkersPagingSource
import com.tabarkevych.places_app.data.mapper.toMarker
import com.tabarkevych.places_app.domain.base.Result
import com.tabarkevych.places_app.domain.model.Marker
import com.tabarkevych.places_app.domain.repository.IAuthRepository
import com.tabarkevych.places_app.domain.repository.IMarkersRepository
import com.tabarkevych.places_app.extensions.executeSafeWithResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class FirebaseMarkersRepository @Inject constructor(
    private val authRepository: IAuthRepository
) : IMarkersRepository {

    private val fireDatabase by lazy { FirebaseDatabase.getInstance().reference }
    private val fireStorage by lazy { FirebaseStorage.getInstance() }

    override fun getAllMarkers(): Flow<Result<List<Marker>>> {
        return callbackFlow {
            val userId = authRepository.getUserId().toString()
            val queryProductNames = fireDatabase.child(userId).child(CHILD_MARKERS).orderByKey()

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(
                        executeSafeWithResult { Result.Success(snapshot.children.map { it.toMarker() }) }
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    close()
                }
            }

            queryProductNames.addValueEventListener(listener)
            awaitClose {
                queryProductNames.removeEventListener(listener)
            }

        }
    }


    override fun getMarkersPaging(): Flow<PagingData<Marker>> {
        val userId = authRepository.getUserId().toString()
        return Pager(PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        ),
            pagingSourceFactory = { MarkersPagingSource(fireDatabase, userId) }
        ).flow
    }

    override suspend fun getMarkerById(markerId: Long): Flow<Marker> {
        val userId = authRepository.getUserId().toString()
        val marker = fireDatabase.child(userId).child(CHILD_MARKERS).child(markerId.toString())
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.toMarker())
                }

                override fun onCancelled(error: DatabaseError) {
                    close()
                }
            }

            marker.addValueEventListener(listener)
            awaitClose {
                marker.removeEventListener(listener)
            }
        }
    }

    override suspend fun addMarker(marker: Marker) {
        val userId = authRepository.getUserId().toString()
        fireDatabase.child(userId).child(CHILD_MARKERS).child(marker.timestamp.toString())
            .setValue(marker)
    }

    override suspend fun updateMarker(marker: Marker) {
        val userId = authRepository.getUserId().toString()
        fireDatabase.child(userId).child(CHILD_MARKERS).child(marker.timestamp.toString())
            .setValue(marker)
    }

    override suspend fun deleteMarker(markerId: Long) {
        val userId = authRepository.getUserId().toString()
        fireDatabase.child(userId).child(CHILD_MARKERS).child(markerId.toString()).removeValue()
    }

    override suspend fun uploadImageWithMarker(
        timestamp:Long,
        imageUri: Uri,
        latLng: LatLng,
        title: String,
        description: String
    ) {
        val imageRef =
            fireStorage.getReference(CHILD_MARKERS + "/" + authRepository.getUserId() + "/" + System.currentTimeMillis())
        imageRef.putFile(imageUri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                runBlocking {
                    val test = uri.toString()
                    addMarker(
                        Marker(
                            timestamp,
                            latLng.latitude.toString(),
                            latLng.longitude.toString(),
                            test,
                            title,
                            description
                        )
                    )
                }
            }
        }
    }

    companion object {

        const val CHILD_MARKERS = "markers"
    }


}