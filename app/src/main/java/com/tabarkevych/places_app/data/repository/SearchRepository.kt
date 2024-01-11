package com.tabarkevych.places_app.data.repository

import com.tabarkevych.places_app.data.database.dao.SearchHistoryDao
import com.tabarkevych.places_app.data.mapper.toDomain
import com.tabarkevych.places_app.data.mapper.toEntity
import com.tabarkevych.places_app.domain.model.SearchHistory
import com.tabarkevych.places_app.domain.repository.ISearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class SearchRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : ISearchRepository {
    override fun getSearchHistory(): Flow<List<SearchHistory>> {
        return searchHistoryDao.getSearchHistory().map { it.toDomain() }
    }

    override suspend fun addSearchHistory(item: SearchHistory) {
        searchHistoryDao.insert(item.toEntity())
    }

    override suspend fun removeSearchHistory() {
        searchHistoryDao.deleteAll()
    }

}