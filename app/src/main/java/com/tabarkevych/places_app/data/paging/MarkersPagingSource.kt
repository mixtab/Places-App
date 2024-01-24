package com.tabarkevych.places_app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.tabarkevych.places_app.data.repository.FirebaseMarkersRepository.Companion.CHILD_MARKERS
import com.tabarkevych.places_app.data.mapper.toMarker
import com.tabarkevych.places_app.data.repository.FirebaseMarkersRepository.Companion.CHILD_USERS
import com.tabarkevych.places_app.domain.model.Marker
import kotlinx.coroutines.tasks.await

class MarkersPagingSource(
    private val db: DatabaseReference,
    private val userId:String
) : PagingSource<DataSnapshot, Marker>() {

    override fun getRefreshKey(state: PagingState<DataSnapshot, Marker>): DataSnapshot? = null

    override suspend fun load(params: LoadParams<DataSnapshot>) = try {
        val queryProductNames = db.child(CHILD_USERS).child(userId).child(CHILD_MARKERS).orderByKey().limitToFirst(10)
        val currentPage = params.key ?: queryProductNames.get().await()
        val lastVisibleProductKey = currentPage.children.last().key
        val nextPage = queryProductNames.startAfter(lastVisibleProductKey).get().await()
        val markers = currentPage.children.map { snapshot -> snapshot.toMarker() }

        LoadResult.Page(
            data = markers,
            prevKey = null,
            nextKey = nextPage
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}

