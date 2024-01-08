package com.tabarkevych.places_app.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import com.tabarkevych.places_app.domain.model.Marker
import kotlinx.coroutines.flow.Flow
import com.tabarkevych.places_app.domain.base.Result

interface IMarkersRepository {

    fun getAllMarkers(): Flow<Result<List<Marker>>>

    fun getMarkersPaging(): Flow<PagingData<Marker>>

    suspend fun getMarkerById(markerId: Long): Flow<Marker>

    suspend fun addMarker(marker: Marker)

    suspend fun deleteMarker(markerId: Long)

    suspend fun uploadImageWithMarker(
        timestamp: Long,
        imagesUri: List<Uri>?,
        latLng: LatLng,
        title: String,
        description: String
    )
}