package com.example.moviereview.features.review.domain.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favourites(
    val byline: String,
    val dateUpdated: String,
    val displayTitle: String,
    val headline: String,
    @PrimaryKey
    val url: String,
    val src: String?,
    val publicationDate: String,
    val description: String,
)
