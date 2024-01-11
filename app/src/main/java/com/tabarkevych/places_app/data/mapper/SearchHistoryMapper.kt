package com.tabarkevych.places_app.data.mapper

import com.tabarkevych.places_app.data.database.entity.SearchHistoryEntity
import com.tabarkevych.places_app.domain.model.SearchHistory
import java.time.Instant


fun SearchHistoryEntity.toDomain() = SearchHistory(
    this.timestamp,
    this.id,
    this.title,
    this.subtitle
)

fun List<SearchHistoryEntity>.toDomain() = this.map { it.toDomain() }

fun SearchHistory.toEntity() = SearchHistoryEntity(
    timestamp = Instant.now().toEpochMilli(),
    this.id,
    this.title,
    this.subtitle
)

fun List<SearchHistory>.toEntity() = this.map { it.toEntity() }